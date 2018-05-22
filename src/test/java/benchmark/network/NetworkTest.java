package benchmark.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.junit.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import benchmark.config.AbstractTest;
import benchmark.dao.BenchmarkPersistDao;
import benchmark.dao.BenchmarkPersistImp;
import benchmark.enumx.TestCategoryEnum;
import benchmark.report.single.SingleTestResult;
import benchmark.report.single.SingleTestResultNetwork;
import benchmark.report.summary.SummaryReportFactory;
import benchmark.tools.BigDecimalUtil;
import benchmark.tools.ReportUtil;
import benchmark.tools.TimeUtil;

/**
 * this part will focus on network speed, such as ping, big ping and small ping,
 * calculate and something
 * 
 * @author ryang
 *
 */
public class NetworkTest extends AbstractTest{
	
	@AfterMethod(alwaysRun = true)
	private void setSummaryParameter(ITestResult testResult) throws Exception{
		log.info("setSummaryParameter...");
		Date beginTime = TimeUtil.millisecondsToCalendarTime(testResult.getStartMillis());
		Date endTime = TimeUtil.millisecondsToCalendarTime(testResult.getEndMillis());
		String currentName = testResult.getName();
		//String testName = testResult.getInstanceName()+"."+testResult.getName();
		//gtr.setDescription(testResult.getMethod().getDescription());
		
		/* consider about run multiple thread, the target test maybe not the last check one by one here */
		SingleTestResult strObj = SummaryReportFactory.getNetworkSummary().getSingleTestResultByName(currentName);
		if(strObj != null){
			strObj.setBeginTime(beginTime);
			strObj.setEndTime(endTime);
		}
	}

	@Test(enabled = true, description = "test the network performance in regular ping")
	public void smallMTUPingTest(Method method) {
		Test test = method.getAnnotation(Test.class);
		String description = test.description();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName(); //not sure it works in multiple thread
		String ip = env.getRequiredProperty("jboss.ip.address").trim();
		int tryTimes = Integer.parseInt(env.getRequiredProperty("ping.retry").trim());
		Double goal = 25D; //25ms
		//int tryTimes = 10; 
		if(ip == null || ip.trim().isEmpty()){
			Assert.fail("Could not get the test IP address, please check your properties 'jboss.ip.address' configuration");
		}
		
		SingleTestResultNetwork strb = new SingleTestResultNetwork(methodName, description);
		StringBuffer pingResult = new StringBuffer();;
		String pingCmd = "ping " + ip + " -n " + tryTimes;
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				log.info(inputLine);
				//System.out.println(inputLine);
				pingResult.append(inputLine);
				pingResult.append("<br>");
			}
			in.close();
			//log.debug(pingResult.toString());
			
		} catch (IOException e) {
			log.error("ERROR", e);
		}
		
		String pingResultStr = pingResult.toString();
		strb.setIpAddress(ip);
		strb.setPass(getReceivedFromPing(pingResultStr));
		strb.setFail(getLostFromPing(pingResultStr));
		strb.setTotal(tryTimes);
		
		/* summary bar */
		strb.setMinimumTime(getMinimumFromPing(pingResultStr));
		strb.setMaximumTime(getMaximumFromPing(pingResultStr));
		strb.setAverageTime(getAverageFromPing(pingResultStr));
		strb.setPingDetail(pingResultStr);
		
		/* history data */
		BenchmarkPersistDao bpDao = new BenchmarkPersistImp();
		Double avg = getAverageFromPing(pingResultStr);
		if(avg.isNaN() || avg == 0D){
//			strb.setAdjustScore(200D); //TODO, need more scientific score calculation
			strb.setAdjustScore(100D);
		} else {
//			strb.setAdjustScore(BigDecimalUtil.roundDecimalDouble((goal/avg)*100, 1));
			strb.setAdjustScore(calAdjustScore(goal, avg));
		}
		strb.setAverageHistoryScore(BigDecimalUtil.roundDecimalDouble(bpDao.getAverageHistoryScore(methodName, env),1)); 
		JSONObject jsonObj = new JSONObject(strb);
		
		/* generate single report */
		ReportUtil.generateSingleTestReport(jsonObj, methodName, env, TestCategoryEnum.NETWORK);
		/* store to the summary result, maybe a little waste of space, but it is not bad*/
		SummaryReportFactory.getNetworkSummary().getSingleTestList().add(strb);
	}
	
	@Test(enabled = true, description = "test the network performance in large mtu")
	public void largeMTUPingTest(Method method) {
		Test test = method.getAnnotation(Test.class);
		String description = test.description();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName(); //not sure it works in multiple thread
		String ip = env.getRequiredProperty("jboss.ip.address").trim();
		int tryTimes = Integer.parseInt(env.getRequiredProperty("ping.retry").trim());
		Double goal = 30D; //30 ms
		//int tryTimes = 10; 
		int mtuSize = 1500;
		if(ip == null || ip.trim().isEmpty()){
			Assert.fail("Could not get the test IP address, please check your properties 'jboss.ip.address' configuration");
		}
		
		SingleTestResultNetwork strb = new SingleTestResultNetwork(methodName, description);
		StringBuffer pingResult = new StringBuffer();;
		String pingCmd = "ping " + ip + " -n " + tryTimes + " -l " + mtuSize;
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				log.info(inputLine);
				//System.out.println(inputLine);
				pingResult.append(inputLine);
				pingResult.append("<br>");
			}
			in.close();
			//log.debug(pingResult.toString());
			
		} catch (IOException e) {
			log.error("ERROR", e);
		}
		
		String pingResultStr = pingResult.toString();
		strb.setIpAddress(ip);
		strb.setPass(getReceivedFromPing(pingResultStr));
		strb.setFail(getLostFromPing(pingResultStr));
		strb.setTotal(tryTimes);
		
		/* summary bar */
		strb.setMinimumTime(getMinimumFromPing(pingResultStr));
		strb.setMaximumTime(getMaximumFromPing(pingResultStr));
		strb.setAverageTime(getAverageFromPing(pingResultStr));
		strb.setPingDetail(pingResultStr);
		
		/* history data */
		BenchmarkPersistDao bpDao = new BenchmarkPersistImp();
		Double avg = getAverageFromPing(pingResultStr);
		if(avg.isNaN() || avg == 0D){
			//strb.setAdjustScore(200D); //TODO, need more scientific score calculation
			strb.setAdjustScore(100D);
		} else {
			//strb.setAdjustScore(BigDecimalUtil.roundDecimalDouble((goal/avg)*100, 1));
			strb.setAdjustScore(calAdjustScore(goal, avg));
		}
		strb.setAverageHistoryScore(BigDecimalUtil.roundDecimalDouble(bpDao.getAverageHistoryScore(methodName, env),1)); 
		JSONObject jsonObj = new JSONObject(strb);
		
		/* generate single report */
		ReportUtil.generateSingleTestReport(jsonObj, methodName, env, TestCategoryEnum.NETWORK);
		/* store to the summary result, maybe a little waste of space, but it is not bad*/
		SummaryReportFactory.getNetworkSummary().getSingleTestList().add(strb);
	}
	
	private Double calAdjustScore(Double goal, Double actual) {
		Double adjustScore = (goal/actual)*100;
		if(adjustScore>100){
			adjustScore = 100D;
		}
		return BigDecimalUtil.roundDecimalDouble(adjustScore, 1);
	}

	/**
	 * get the lost # from ping, if not get null, return -1
	 * @param pingStr
	 * @return
	 */
	private int getLostFromPing(String pingStr){
		if(pingStr == null || pingStr.trim().isEmpty()){
			return -1;
		}
		
		String pattern = "(Lost)(\\s=\\s)(.*)(\\()"; //Lost = 0 (0% loss),
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(pingStr);
		if(m.find()){
			return Integer.parseInt(m.group(3).trim());
		} else {
			return -1;
		}
	}
	
	/**
	 * get the received # from ping, if not get null, return -1
	 * @param pingStr
	 * @return
	 */
	private int getReceivedFromPing(String pingStr){
		if(pingStr == null || pingStr.trim().isEmpty()){
			return -1;
		}
		
		String pattern = "(Received)(\\s=\\s)(.*)(,\\sLost)"; //Received = 10,
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(pingStr);
		if(m.find()){
			return Integer.parseInt(m.group(3).trim());
		} else {
			return -1;
		}
	}
	
	/**
	 * get the Minimum milliseconds from ping, if not get null, return -1
	 * @param pingStr
	 * @return
	 */
	private Double getMinimumFromPing(String pingStr){
		if(pingStr == null || pingStr.trim().isEmpty()){
			return -1D;
		}
		
		String pattern = "(Minimum)(\\s=\\s)(.*)(ms,\\sMaximum)"; //Minimum = 0ms
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(pingStr);
		if(m.find()){
			return Double.parseDouble(m.group(3).trim());
		} else {
			return -1D;
		}
	}
	
	/**
	 * get the Maximum milliseconds from ping, if not get null, return -1
	 * @param pingStr
	 * @return
	 */
	private Double getMaximumFromPing(String pingStr){
		if(pingStr == null || pingStr.trim().isEmpty()){
			return -1D;
		}
		
		String pattern = "(Maximum)(\\s=\\s)(.*)(ms,\\sAverage)"; //Maximum = 0ms,
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(pingStr);
		if(m.find()){
			return Double.parseDouble(m.group(3).trim());
		} else {
			return -1D;
		}
	}
	
	/**
	 * get the Average milliseconds from ping, if not get null, return -1
	 * @param pingStr
	 * @return
	 */
	private Double getAverageFromPing(String pingStr){
		if(pingStr == null || pingStr.trim().isEmpty()){
			return -1D;
		}
		
		String pattern = "(Average)(\\s=\\s)(.*)(ms)"; //Average = 0ms
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(pingStr);
		if(m.find()){
			return Double.parseDouble(m.group(3).trim());
		} else {
			return -1D;
		}
	}
	
}
