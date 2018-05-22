package benchmark.config;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import benchmark.dao.BenchmarkPersistDao;
import benchmark.dao.BenchmarkPersistImp;
import benchmark.enumx.TestCategoryEnum;
import benchmark.pojo.PerfBenchmarkSummary;
import benchmark.servlet.ServletFactory;
import benchmark.tools.PropertiesUtil;
import benchmark.tools.ReportUtil;
import benchmark.tools.slack.ScoreBoardPojo;
import benchmark.tools.slack.SlackConnection;
import benchmark.tools.slack.SlackUtil;

@ContextConfiguration(classes = { 
	TestConfig.class, 
	ServletConfig.class,
	DatabaseConfig.class,
	SeleniumConfig.class,
})

/**
 * the AbstractTest for the data validation
 * @author ryang
 *
 */
public class AbstractTest extends AbstractTestNGSpringContextTests{ 
	
	protected final static Logger log = LoggerFactory.getLogger(AbstractTest.class);
	
	protected WebDriver driver;
	
	@Autowired
	protected ServletFactory servletFactory;
	
	@Autowired
	protected Environment env;	
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("driveFactory") //use the drivefactory bean
	protected Supplier<WebDriver> driverFactory;
	
	@BeforeSuite(alwaysRun = true)
	protected void beforeSuite(){
		log.info("** \n\n ** Begin Test Suite \n");
	}
	
	/**
	 * send Business benchmark score to slack
	 */
	private void sendBusinessScoreToSlack(){
		log.info("** Sending Business benchmark score to slack \n");
		try {
			/* get score */
			ScoreBoardPojo sbp = new ScoreBoardPojo();
			String envStr = System.getProperty("env"); //current running environment
			BenchmarkPersistImp bpi = new BenchmarkPersistImp();
			List<PerfBenchmarkSummary> recentSummary = bpi.getRecentSummaryList(2, envStr, TestCategoryEnum.BUSINESS);
			if(recentSummary == null || recentSummary.isEmpty()){
				return;
			} else if(recentSummary.size() == 1){
				String currentScore = String.valueOf(recentSummary.get(0).getAdjustScore());
				sbp.setCurrentScore(currentScore);
			} else {
				String currentScore = String.valueOf(recentSummary.get(1).getAdjustScore());
				String lastScore = String.valueOf(recentSummary.get(0).getAdjustScore());
				sbp.setCurrentScore(currentScore);
				sbp.setLastScore(lastScore);
			}
			
			/* slack */
			SlackConnection slackCon = new SlackConnection(env);
			SlackUtil slack = new SlackUtil();
			
			/* send final score to slack channel, currently it means UI score only */
			if (PropertiesUtil.getSlackChannel() != null && !PropertiesUtil.getSlackChannel().trim().isEmpty()) {
				log.info("\n\n ** Slack Benchmark Score To Channel {}!", PropertiesUtil.getSlackChannel());
				slack.sendResultToChannel(sbp, slackCon); //send to channel
			
			}
			if (PropertiesUtil.getSlackUser() != null && !PropertiesUtil.getSlackUser().trim().isEmpty()) {
				log.info("\n\n ** Slack Benchmark Score To User {}!", PropertiesUtil.getSlackUser());
				slack.sendResultToUser(sbp, slackCon); //send to user
			}
		} catch(Exception e){
			log.error("Error, send business benchmark score to slack fails.", e);
		}
	}
	
	@AfterSuite(alwaysRun = true)
	protected void afterSuite(){
		log.info("\n\n ** End Test Suite!");
		String envStr = System.getProperty("env"); //current running environment
		String debugMode = System.getProperty("debug"); //run in debug mode
		
		try {
			if (!(debugMode != null && Boolean.parseBoolean(debugMode))) {
				BenchmarkPersistDao bpd = new BenchmarkPersistImp();
				bpd.persistAllTestResult(envStr);

				/* send score to slack */
				sendBusinessScoreToSlack();
			}
		} catch (Exception e) {
			log.error("Error", e);
		}
		
		/* generate the report
		 * Ps: in the summary page, Current Score and Build Number dependency on the db calculate result
		 */
		try{
			ReportUtil.generateReports(envStr);
		} catch(Exception e){
			log.error("Error when generate report", e);
		}
	}
	
	@BeforeClass(alwaysRun = true)
	protected void beginClass(){
		String currentClass = this.getClass().getName();
		log.info("\n\n** Begin test Class {} \n", currentClass);
	}
	
	@AfterClass(alwaysRun = true)
	protected void endClass() {
		String currentClass = this.getClass().getName();
		log.info("\n\n** End test Class {} \n", currentClass);
	}
	
	@BeforeMethod(alwaysRun = true)
	protected void beforeMethod(Method method) {
		String testName = method.getName(); 
		log.info("\n\n** Begin test Method {} \n", testName);
	}
}
