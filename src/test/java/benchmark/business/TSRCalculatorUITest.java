package benchmark.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import benchmark.enumx.ResultStatusEnum;
import benchmark.pojo.PageTimingPojo;
import benchmark.pojo.TestCaseParameter;
import benchmark.report.google.timeline.TimeLineModal;
import benchmark.report.google.timeline.TimelineUnit;
import benchmark.report.single.SingleTestResult;
import benchmark.report.single.SingleTestResultBusiness;
import benchmark.report.summary.SummaryReportFactory;
import benchmark.tools.BigDecimalUtil;
import benchmark.tools.TimeUtil;
import test.insight.helper.vo.AccountTypeEnum;
import test.insight.ui.pages.HomePage;
import test.insight.ui.pages.tsrcalculator.TsrIndexPage;

/**
 * this part is all about the UI/business Level test
 * 
 * @author ryang
 *
 */
public class TSRCalculatorUITest extends AbstractUITest{
	
	TsrIndexPage tsrIndexPage;
	
	/**
	 * initial session for all the TSR test
	 */
	@BeforeClass(alwaysRun = true)
	public void intialSession(){
		log.info("initial the test class status...");
		
		tsrIndexPage = new TsrIndexPage(driver);
		HomePage homePage = HomePage.goIntoHomePage(env, driver, AccountTypeEnum.corpBenchmark, jdbcTemplate);
		homePage.waitForHomePageToLoad();
	}
	
	@AfterMethod(alwaysRun = true)
	private void setSummaryParameter(ITestResult testResult) throws Exception{
		log.info("setSummaryParameter...");
		Date beginTime = TimeUtil.millisecondsToCalendarTime(testResult.getStartMillis());
		Date endTime = TimeUtil.millisecondsToCalendarTime(testResult.getEndMillis());
		String currentName = testResult.getName();
		
		/* consider about run multiple thread, the target test maybe not the last check one by one here */
		SingleTestResult strObj = SummaryReportFactory.getBusinessSummary().getSingleTestResultByName(currentName);
		if(strObj != null){
			strObj.setBeginTime(beginTime);
			strObj.setEndTime(endTime);
		}
	}
	
	/**
	 * check the TSR calculator update work flow cost
	 */
	@Test(dataProvider="test-case-parameter", enabled = true, priority = 1)
	public void TSRCalculatorPerformance(TestCaseParameter tcp){
//		Test test = method.getAnnotation(Test.class);
//		String description = test.description();
//		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName(); //not sure it works in multiple thread
//		//int sleepInMillis = 100; //sleep interval, milliseconds
//		int retryTimes = Integer.parseInt(env.getProperty("P4P_UI_RETRY"));
//		Double standard = 20D; //standard time, currently hard coding to 10 seconds
//		int timeOutInSeconds = (int) (standard * 10); //total time for driver wait, seconds, 10 times of standard time..
		
//		int sleepInMillis = tcp.getSleepInMillis();
		int retryTimes = tcp.getRetryTimes();
		Double standard = tcp.getStandardTime();
		int timeOutInSeconds = tcp.getTimeoutInSeconds();
		String methodName = tcp.getSimpleMethodName();
		String description = tcp.getDescription();
		
		SingleTestResultBusiness strb = new SingleTestResultBusiness(methodName, description);
		for(int cnt=0;cnt<retryTimes;cnt++){
			long mark1 = Calendar.getInstance().getTimeInMillis();
			try{
				long mark2 = Calendar.getInstance().getTimeInMillis();
		        
				String url = env.getProperty("UrlHeader")+"/app/gc/tsr/index.jsp?p=c&commit=31250";
				driver.get(url);
				long mark3 = Calendar.getInstance().getTimeInMillis();
				
				tsrCalculatorSetting(true, false, false, AccountTypeEnum.corpBenchmark);
				//TODO: add peer group or not?
				long mark4 = Calendar.getInstance().getTimeInMillis();
				
				tsrIndexPage.clickUpdateBtn();
				long mark5 = Calendar.getInstance().getTimeInMillis();
				
				By loadingDivLocator = By.id("peercontainer");
				/* wait the loading spin appear immediately, check every 10 milliseconds */
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					    .withTimeout(5, TimeUnit.SECONDS)
					    .pollingEvery(10, TimeUnit.MILLISECONDS)
					    .ignoring(NoSuchElementException.class);
				wait.until(ExpectedConditions.visibilityOfElementLocated(loadingDivLocator));
				/* now check the loading spin disappear, means the update operate complete */
				new WebDriverWait(driver, timeOutInSeconds)
						.until(ExpectedConditions
								.invisibilityOfElementLocated(loadingDivLocator));
				
				long mark6 = Calendar.getInstance().getTimeInMillis();
				
				/* get the time cost if running successfully */
				double tsrCalculatorCost = (mark6 - mark5)/1000;
				log.info("** tsr calculator time cost: {} ", tsrCalculatorCost);
				
				/* multiple level JSON array*/
				List<TimelineUnit> tlmListUnit = new ArrayList<TimelineUnit>();
				// add page loading detail indicators
				PageTimingPojo ptp = new PageTimingPojo(driver);
				tlmListUnit.addAll(ptp.toTimlineUnitList());
				
				tlmListUnit.add(new TimelineUnit("loadingTime", "loadingTime", mark2, mark3));
				tlmListUnit.add(new TimelineUnit("setTSRCriteria", "SetTSRCriteria", mark3, mark4));
				tlmListUnit.add(new TimelineUnit("clickUpdateButton", "clickUpdateButton", mark4, mark5));
				tlmListUnit.add(new TimelineUnit("tsrCalculatorCost", "tsrCalculatorCost", mark5, mark6));
				
				double tsrCalculatorDuring = BigDecimalUtil.roundDecimalDouble(((double)mark6-(double)mark5)/1000, 1);
				double score = getScore(tsrCalculatorDuring, standard);
				TimeLineModal tlm = new TimeLineModal(tlmListUnit, standard, score, ResultStatusEnum.TEST_PASS);
				tlm.addDuring("TSR Calculator Update time cost", tsrCalculatorDuring);
				
				strb.getTimeLineModalList().add(tlm);
				
				/* add to pass list, currently don't need detail, no need to add to fail list */
				strb.setPass(strb.getPass()+1);
				
			} catch(Exception e){
				log.error("Error", e);
				List<TimelineUnit> tlmListUnit = new ArrayList<TimelineUnit>();
				long markException = Calendar.getInstance().getTimeInMillis();
				tlmListUnit.add(new TimelineUnit("Exception", "Exception", mark1, markException));
				
				double tsrCalculatorDuring = (double)timeOutInSeconds;
				double score = getScore(tsrCalculatorDuring, standard);
				TimeLineModal tlm = new TimeLineModal(tlmListUnit, standard, score, ResultStatusEnum.TEST_FAIL);
				tlm.addDuring("TSR Calculator Update time cost", tsrCalculatorDuring);
				
				strb.getTimeLineModalList().add(tlm);
				
				/* add to fail list, currently don't need detail, no need to add to fail list */
				strb.setFail(strb.getFail()+1);
				
				continue;
			}
		} 
		collectResult(strb, methodName, retryTimes);	
//		/* set current test information */
//		strb.setTotal(retryTimes);
//		strb.calculateAdjustScore();
//		
//		/* set history statistics */
//		//String env = System.getProperty("env");
//		BenchmarkPersistDao bpDao = new BenchmarkPersistImp();
//		strb.setAverageHistoryScore(BigDecimalUtil.roundDecimalDouble(bpDao.getAverageHistoryScore(methodName, env),1)); 
//		strb.setAverageHistoryFirstAttScore(BigDecimalUtil.roundDecimalDouble(bpDao.getAverageHistoryFirstAttScore(methodName, env),1));
//		strb.setLastAttScroe(BigDecimalUtil.roundDecimalDouble(bpDao.getLastAttScroe(methodName, env),1));
//		strb.setLastFirstAttScore(BigDecimalUtil.roundDecimalDouble(bpDao.getLastFirstAttScore(methodName, env),1));
//		JSONObject jsonObj = new JSONObject(strb);
//		
//		/* generate single report */
//		//ReportUtil.generateSingleUITestReport(jsonObj, methodName, env);
//		ReportUtil.generateSingleTestReport(jsonObj, methodName, env, TestCategoryEnum.BUSINESS);
//		/* store to the summary result, maybe a little waste of space, but it is not bad*/
//		SummaryReportFactory.getBusinessSummary().getSingleTestList().add(strb);
	}
	
	/**
	 * check the TSR calculator Top 1000 Companies by Revenue market index performance
	 */
	@Test(dataProvider="test-case-parameter", enabled = true, priority = 1)
	public void TSRCalculatorPerformanceWithMarketIndexTop1000(TestCaseParameter tcp){
		
		int retryTimes = tcp.getRetryTimes();
		Double standard = tcp.getStandardTime();
		int timeOutInSeconds = tcp.getTimeoutInSeconds();
		String methodName = tcp.getSimpleMethodName();
		String description = tcp.getDescription();
		
		SingleTestResultBusiness strb = new SingleTestResultBusiness(methodName, description);
		for(int cnt=0;cnt<retryTimes;cnt++){
			long mark1 = Calendar.getInstance().getTimeInMillis();
			try{
				long mark2 = Calendar.getInstance().getTimeInMillis();
		        
				String url = env.getProperty("UrlHeader")+"/app/gc/tsr/index.jsp?p=c&commit=31250";
				driver.get(url);
				long mark3 = Calendar.getInstance().getTimeInMillis();
				
				tsrCalculatorSetting(true, false, false, AccountTypeEnum.corpBenchmark);
				
				long mark4 = Calendar.getInstance().getTimeInMillis();
				
				tsrIndexPage.chooseTop1000MarketIndex();
				
				long mark5 = Calendar.getInstance().getTimeInMillis();
				
				tsrIndexPage.clickUpdateBtn();
				long mark6 = Calendar.getInstance().getTimeInMillis();
				
				By loadingDivLocator = By.id("peercontainer");
				/* wait the loading spin appear immediately, check every 10 milliseconds */
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					    .withTimeout(5, TimeUnit.SECONDS)
					    .pollingEvery(10, TimeUnit.MILLISECONDS)
					    .ignoring(NoSuchElementException.class);
				wait.until(ExpectedConditions.visibilityOfElementLocated(loadingDivLocator));
				/* now check the loading spin disappear, means the update operate complete */
				new WebDriverWait(driver, timeOutInSeconds)
						.until(ExpectedConditions
								.invisibilityOfElementLocated(loadingDivLocator));
				
				long mark7 = Calendar.getInstance().getTimeInMillis();
				
				/* get the time cost if running successfully */
				double tsrCalculatorCost = (mark7 - mark6)/1000;
				log.info("** tsr calculator time cost: {} ", tsrCalculatorCost);
				
				/* multiple level JSON array*/
				List<TimelineUnit> tlmListUnit = new ArrayList<TimelineUnit>();
				// add page loading detail indicators
				PageTimingPojo ptp = new PageTimingPojo(driver);
				tlmListUnit.addAll(ptp.toTimlineUnitList());
				
				tlmListUnit.add(new TimelineUnit("loadingTime", "loadingTime", mark2, mark3));
				tlmListUnit.add(new TimelineUnit("setTSRCriteria", "SetTSRCriteria", mark3, mark4));
				tlmListUnit.add(new TimelineUnit("chooseTop1000", "chooseTop1000", mark4, mark5));
				tlmListUnit.add(new TimelineUnit("clickUpdateButton", "clickUpdateButton", mark5, mark6));
				tlmListUnit.add(new TimelineUnit("tsrCalculatorCost", "tsrCalculatorCost", mark6, mark7));
				
				double tsrCalculatorDuring = BigDecimalUtil.roundDecimalDouble(((double)mark7-(double)mark6)/1000, 1);
				double score = getScore(tsrCalculatorDuring, standard);
				TimeLineModal tlm = new TimeLineModal(tlmListUnit, standard, score, ResultStatusEnum.TEST_PASS);
				tlm.addDuring("TSR Calculator Top 1000 Update time cost", tsrCalculatorDuring);
				
				strb.getTimeLineModalList().add(tlm);
				
				/* add to pass list, currently don't need detail, no need to add to fail list */
				strb.setPass(strb.getPass()+1);
				
			} catch(Exception e){
				log.error("Error", e);
				List<TimelineUnit> tlmListUnit = new ArrayList<TimelineUnit>();
				long markException = Calendar.getInstance().getTimeInMillis();
				tlmListUnit.add(new TimelineUnit("Exception", "Exception", mark1, markException));
				
				double tsrCalculatorDuring = (double)timeOutInSeconds;
				double score = getScore(tsrCalculatorDuring, standard);
				TimeLineModal tlm = new TimeLineModal(tlmListUnit, standard, score, ResultStatusEnum.TEST_FAIL);
				tlm.addDuring("TSR Calculator Top 1000 Update time cost", tsrCalculatorDuring);
				
				strb.getTimeLineModalList().add(tlm);
				
				/* add to fail list, currently don't need detail, no need to add to fail list */
				strb.setFail(strb.getFail()+1);
				
				continue;
			}
		} 
		collectResult(strb, methodName, retryTimes);	
	}
	
	/**
	 * check the TSR calculator Top 500 Companies by Market Cap market index performance
	 */
	@Test(dataProvider="test-case-parameter", enabled = true, priority = 1)
	public void TSRCalculatorPerformanceWithMarketIndexTop500(TestCaseParameter tcp){
		
		int retryTimes = tcp.getRetryTimes();
		Double standard = tcp.getStandardTime();
		int timeOutInSeconds = tcp.getTimeoutInSeconds();
		String methodName = tcp.getSimpleMethodName();
		String description = tcp.getDescription();
		
		SingleTestResultBusiness strb = new SingleTestResultBusiness(methodName, description);
		for(int cnt=0;cnt<retryTimes;cnt++){
			long mark1 = Calendar.getInstance().getTimeInMillis();
			try{
				long mark2 = Calendar.getInstance().getTimeInMillis();
		        
				String url = env.getProperty("UrlHeader")+"/app/gc/tsr/index.jsp?p=c&commit=31250";
				driver.get(url);
				long mark3 = Calendar.getInstance().getTimeInMillis();
				
				tsrCalculatorSetting(true, false, false, AccountTypeEnum.corpBenchmark);
				
				long mark4 = Calendar.getInstance().getTimeInMillis();
				
				tsrIndexPage.chooseTop1000MarketIndex();
				
				long mark5 = Calendar.getInstance().getTimeInMillis();
				
				tsrIndexPage.clickUpdateBtn();
				long mark6 = Calendar.getInstance().getTimeInMillis();
				
				By loadingDivLocator = By.id("peercontainer");
				/* wait the loading spin appear immediately, check every 10 milliseconds */
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					    .withTimeout(5, TimeUnit.SECONDS)
					    .pollingEvery(10, TimeUnit.MILLISECONDS)
					    .ignoring(NoSuchElementException.class);
				wait.until(ExpectedConditions.visibilityOfElementLocated(loadingDivLocator));
				/* now check the loading spin disappear, means the update operate complete */
				new WebDriverWait(driver, timeOutInSeconds)
						.until(ExpectedConditions
								.invisibilityOfElementLocated(loadingDivLocator));
				
				long mark7 = Calendar.getInstance().getTimeInMillis();
				
				/* get the time cost if running successfully */
				double tsrCalculatorCost = (mark7 - mark6)/1000;
				log.info("** tsr calculator time cost: {} ", tsrCalculatorCost);
				
				/* multiple level JSON array*/
				List<TimelineUnit> tlmListUnit = new ArrayList<TimelineUnit>();
				// add page loading detail indicators
				PageTimingPojo ptp = new PageTimingPojo(driver);
				tlmListUnit.addAll(ptp.toTimlineUnitList());
				
				tlmListUnit.add(new TimelineUnit("loadingTime", "loadingTime", mark2, mark3));
				tlmListUnit.add(new TimelineUnit("setTSRCriteria", "SetTSRCriteria", mark3, mark4));
				tlmListUnit.add(new TimelineUnit("chooseTop500", "chooseTop500", mark4, mark5));
				tlmListUnit.add(new TimelineUnit("clickUpdateButton", "clickUpdateButton", mark5, mark6));
				tlmListUnit.add(new TimelineUnit("tsrCalculatorCost", "tsrCalculatorCost", mark6, mark7));
				
				double tsrCalculatorDuring = BigDecimalUtil.roundDecimalDouble(((double)mark7-(double)mark6)/1000, 1);
				double score = getScore(tsrCalculatorDuring, standard);
				TimeLineModal tlm = new TimeLineModal(tlmListUnit, standard, score, ResultStatusEnum.TEST_PASS);
				tlm.addDuring("TSR Calculator Top 500 Update time cost", tsrCalculatorDuring);
				
				strb.getTimeLineModalList().add(tlm);
				
				/* add to pass list, currently don't need detail, no need to add to fail list */
				strb.setPass(strb.getPass()+1);
				
			} catch(Exception e){
				log.error("Error", e);
				List<TimelineUnit> tlmListUnit = new ArrayList<TimelineUnit>();
				long markException = Calendar.getInstance().getTimeInMillis();
				tlmListUnit.add(new TimelineUnit("Exception", "Exception", mark1, markException));
				
				double tsrCalculatorDuring = (double)timeOutInSeconds;
				double score = getScore(tsrCalculatorDuring, standard);
				TimeLineModal tlm = new TimeLineModal(tlmListUnit, standard, score, ResultStatusEnum.TEST_FAIL);
				tlm.addDuring("TSR Calculator Top 500 Update time cost", tsrCalculatorDuring);
				
				strb.getTimeLineModalList().add(tlm);
				
				/* add to fail list, currently don't need detail, no need to add to fail list */
				strb.setFail(strb.getFail()+1);
				
				continue;
			}
		} 
		collectResult(strb, methodName, retryTimes);	
	}
	
	/**
	 * set criteria for TSR calculator index page
	 */
	private void tsrCalculatorSetting(boolean isReinvest,
			boolean isBeginStockAvg, boolean isEndStockAvg, AccountTypeEnum type) {
		if (isReinvest) {
			tsrIndexPage.enableReinvestBox();
		} else {
			tsrIndexPage.disableReinvestBox();
		}

		// set the check box
		if (isBeginStockAvg) {
			tsrIndexPage.enableBeginStockAvgBox();
		} else {
			tsrIndexPage.disableBeginStockAvgBox();
		}

		if (isEndStockAvg) {
			tsrIndexPage.enableEndStockAvgBox();
		} else {
			tsrIndexPage.disableEndStockAvgBox();
		}

		// enter the random date
		if (isBeginStockAvg) {
			tsrIndexPage.enterRandAvgBeginDate();
		} else {
			tsrIndexPage.enterRandBeginDate();
		}

		if (isEndStockAvg) {
			tsrIndexPage.enterRandAvgEndDate();
		} else {
			tsrIndexPage.enterRandEndDate();
		}

		tsrIndexPage.enterRandDividendDate();
	}

}
