package benchmark.selenium;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

public class SessionManager {
	private Supplier<WebDriver> driverFactory;
	private Environment env;
	AccountTypeEnum type = AccountTypeEnum.corpBenchmark;
	
	private Logger log = LoggerFactory.getLogger(SessionManager.class);
	
	public SessionManager(Supplier<WebDriver> driverFactory, Environment env){
		this.driverFactory = driverFactory;
		this.env = env;
	}
	
	/**
	 * the simply selenium automation to get the session id, just violent, ugly selenium process
	 * @return
	 */
	public String getSessionid(){
		log.info("start selenium ui automation to get jessionid");
		
		// throw exception it self and halt test
		final String loginURL = env.getRequiredProperty("UrlHeader").trim()+"/app/login/login.jsp";
		final String ticker = env.getRequiredProperty("targetCompany").trim(); // such as "EBAY"
		final String username = env.getRequiredProperty(type.getAccountType().getUsernameTag(), String.class);
		final String password = env.getRequiredProperty(type.getAccountType().getPasswordTag(), String.class);
		final int ELEMENT_WAIT_TIME = Integer.parseInt(env.getRequiredProperty("ELEMENT_WAIT_TIME").trim());
		
		WebDriver driver = driverFactory.get();
		String jsessionid = null;
		
		driver.manage().timeouts().implicitlyWait(ELEMENT_WAIT_TIME, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(loginURL);
		
		driver.findElement(By.id("j_username")).sendKeys(username);
		log.info("1");
		driver.findElement(By.id("j_password")).sendKeys(password);
		log.info("2");
		driver.findElement(By.cssSelector(".loginButton")).click();
		log.info("3");
		driver.findElement(By.cssSelector("#governance > li")).click();
		log.info("4");
		driver.findElement(By.id("p4pLink")).click();
		
		pause(5);
		
		log.info("5");
		driver.findElement(By.id("ribbonTickerTxt")).clear();
		driver.findElement(By.id("ribbonTickerTxt")).sendKeys(ticker);
		log.info("6");
		driver.findElement(By.id("searchBtnId")).click();
		log.info("7");
		
		pause(5);
		log.info("Complete selenium automation.");

		Set<Cookie> cookies = driver.manage().getCookies();
		boolean isSuccess = false;
		for(Cookie temp:cookies){
			log.debug("*** Cookie:"+temp.toString());
			if(temp.getName().equals("JSESSIONID")){
				jsessionid = temp.getValue();
				isSuccess = true;
				break;
			}
		}
		
		if(driver!=null){
			driver.close();
			driver.quit();
		}
		
		if(!isSuccess||jsessionid==null){
			log.error("ERROR, didn't get jessionid sucessfully!"); 
		}
		
		log.info("*** Get JessionID:"+ jsessionid);
		return jsessionid;
	}
	
	/**
	 * system pause seconds
	 * @param seconds
	 */
	public static void pause(int seconds){
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
