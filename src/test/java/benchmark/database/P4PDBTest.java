package benchmark.database;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import benchmark.dao.BenchmarkQueryDao;
import benchmark.dao.BenchmarkQueryImp;

/**
 * This part mainly test about the database performance
 * @author ryang
 *
 */
public class DBTest extends AbstractDBTest{
	
	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestResult testResult) {
		String beginTime = TimeUtil.millisecondsToTime(testResult.getStartMillis());
		String endTime = TimeUtil.millisecondsToTime(testResult.getEndMillis());
		double during = ((double)testResult.getEndMillis()-(double)testResult.getStartMillis())/1000;
		log.info("begin:{}, end:{}, during:{} seconds", beginTime,endTime,during);
	}
	
	@Test(enabled = false)
	public void dbTestSample() throws InterruptedException{
		BenchmarkQueryDao bqd = new BenchmarkQueryImp(jdbcTemplate);
		
	//	long time1 = Calendar.getInstance().getTimeInMillis();
		
		for(int i=0;i<10;i++){
			bqd.benchmarkQuery();
			log.info("** time: {}", i);
			//Thread.sleep(500);
		}
		
//		long time2 = Calendar.getInstance().getTimeInMillis();
//		double time3 = ((double)time2-(double)time1)/1000;
//		
//		String time4 = TimeUtil.millisecondsToTime(time2);
//		log.info("** time3: {}, String: {}", time3, time4);
	}

}
