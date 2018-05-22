package benchmark.business;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.Hashtable;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import benchmark.tools.TimeUtil;

import benchmark.config.AbstractTest;
import benchmark.config.TestParametersConfig;
import benchmark.dao.BenchmarkPersistDao;
import benchmark.dao.BenchmarkPersistImp;
import benchmark.enumx.TestCategoryEnum;
import benchmark.pojo.TestCaseParameter;
import benchmark.report.single.SingleTestResultBusiness;
import benchmark.report.summary.SummaryReportFactory;
import benchmark.tools.BigDecimalUtil;
import benchmark.tools.ReportUtil;

@ContextConfiguration(classes = { TestParametersConfig.class })
public abstract class AbstractUITest extends AbstractTest{
	
	@Autowired
	protected Hashtable<String, TestCaseParameter> testParametersMaps;

	private Boolean driver_active = false;
	
	@BeforeClass(alwaysRun = true)
	protected void initDriver() throws MalformedURLException {
		destroyDriver();
		if (!driver_active) {	
			super.driver = driverFactory.get();
			driver_active = true;
		}
		driver.manage().window().maximize();
	}
	
	@AfterClass(alwaysRun = true)
	protected void destroyDriver() {
		if (driver_active) {
			this.driver.close();	
			driver.quit();
			driver_active = false;
		}
	}
	
	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestResult testResult) {
		String beginTime = TimeUtil.millisecondsToTime(testResult.getStartMillis());
		String endTime = TimeUtil.millisecondsToTime(testResult.getEndMillis());
		double during = ((double)testResult.getEndMillis()-(double)testResult.getStartMillis())/1000;
		log.info("begin:{}, end:{}, during:{} seconds", beginTime,endTime,during);
	}
	
	@DataProvider(name="test-case-parameter")
	public TestCaseParameter[][] testParameterProvider(Method method) throws Exception {
		String targetMethod = method.getDeclaringClass().getCanonicalName()+"."+method.getName();
		log.info("**testParameterProvider: targetMethod is "+ targetMethod);
		
//		HashMap<String, TestCaseParameter> hash = new HashMap<String, TestCaseParameter>();
//		try {
//			JAXBContext jaxbContext = JAXBContext.newInstance(TestCaseParameters.class);
//		
//			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//			TestCaseParameters tcps = (TestCaseParameters) jaxbUnmarshaller
//					.unmarshal(new File("C:/git/performance_benchmark/src/test/resources/testcaseconfig.xml"));
//	
//			for (TestCaseParameter parameter : tcps.getTestCaseParameter()) {
//				hash.put(parameter.getFullMethodName(), parameter);
////				System.out.println(parameter.toString());
//			}
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//			
//		TestCaseParameter a1 = new TestCaseParameter();
//		TestCaseParameter a2 = new TestCaseParameter();
//		a1.setRetryTimes(2);
//		a2.setRetryTimes(3);
//		
//		hash.put("benchmark.business.ProxyProposalResultsTest.ProxyProposalResultsPerformance10Ticker", a1);
//		hash.put("benchmark.business.ProxyProposalResultsTest.ProxyProposalResultsPerformance50Ticker", a2);
//		dt[0][0] = a1;
//		dt[1][0] = a2;
		
//		for (Entry<?, ?> item : testParametersMaps.entrySet()) {
//			System.out.println(item.getValue().toString() + "!!!!!!!!!!!!!!!!!!!!!!!!");
//		}
	
		TestCaseParameter[][] tcp = new TestCaseParameter[1][1]; //just return the target parameter, tried use custom annotation, but can't refract testNG main
		//TODO, need to fail the test if parameter not correct;
//		if(!hash.containsKey(methodName +"1")){ //
//			throw new Exception("Error!!!!!!!!!!!!!");
//		}
		tcp[0][0] = testParametersMaps.get(targetMethod);
	    return tcp;
	}

	/**
	 * calculate score by during and standard
	 * @param during
	 * @param standard
	 */
	protected Double getScore(double during, double standard){
		double score = new BigDecimal(String.valueOf(standard))
		.divide(new BigDecimal(String.valueOf(during)), 3,
				RoundingMode.HALF_UP)
		.multiply(new BigDecimal("100")).doubleValue();
		
		//maximum 100
		if(score>100){
			score = 100D;
		}
		return score;
	}
	
	/**
	 * get average score of the list
	 * @param during
	 * @param standard
	 */
	protected Double getAverage(Double... list){
		if(list == null||list.length<1){
			return 0D;
		}
		int size = list.length;
		BigDecimal sum = new BigDecimal("0");
		for(int i=0;i<size;i++){
			sum = sum.add(new BigDecimal(String.valueOf(list[i])));
		}
		double score = new BigDecimal(String.valueOf(sum)).divide(
				new BigDecimal(String.valueOf(size)), 3, RoundingMode.HALF_UP)
				.doubleValue();
		return score;
	}
	
	/**
	 * collect the test result information
	 * @param strb
	 * @param methodName
	 * @param retryTimes
	 */
	protected void collectResult(SingleTestResultBusiness strb, String methodName ,int retryTimes){
		/* set current test information */
		strb.setTotal(retryTimes);
		strb.calculateAdjustScore();
		
		/* set history statistics */
		//String env = System.getProperty("env");
		BenchmarkPersistDao bpDao = new BenchmarkPersistImp();
		strb.setAverageHistoryScore(BigDecimalUtil.roundDecimalDouble(bpDao.getAverageHistoryScore(methodName, env),1)); 
		strb.setAverageHistoryFirstAttScore(BigDecimalUtil.roundDecimalDouble(bpDao.getAverageHistoryFirstAttScore(methodName, env),1));
		strb.setLastAttScroe(BigDecimalUtil.roundDecimalDouble(bpDao.getLastAttScroe(methodName, env),1));
		strb.setLastFirstAttScore(BigDecimalUtil.roundDecimalDouble(bpDao.getLastFirstAttScore(methodName, env),1));
		
		JSONObject jsonObj = new JSONObject(strb);
		/* generate single report */
		//ReportUtil.generateSingleUITestReport(jsonObj, methodName, env);
		ReportUtil.generateSingleTestReport(jsonObj, methodName, env, TestCategoryEnum.BUSINESS);
		/* store to the summary result, maybe a little waste of space, but it is not bad*/
		SummaryReportFactory.getBusinessSummary().getSingleTestList().add(strb);
	}
	
}
