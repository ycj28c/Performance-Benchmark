package benchmark.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;

import test.insight.helper.vo.AccountTypeEnum;
import test.insight.ui.pages.HomePage;
import benchmark.business.AbstractUITest;

/**
 * this part will test client communicate with server,
 * such as ask for a image, ask for a resource, page loading time or else
 * @author ryang
 *
 */
public class HttpTest extends AbstractUITest{
	/*
	 * try use browsermobproxy,  dynaTrace ?
	 */
	
	@Test(enabled=false)
	public void testResourceHttpPerformance(){	
		HomePage homePage = HomePage.goIntoHomePage(env, driver, AccountTypeEnum.corpBenchmark, jdbcTemplate);
	    homePage.waitForHomePageToLoad(); 
	    
	    JavascriptExecutor js = (JavascriptExecutor)driver;
//	    Set<String> initiatorSet = new HashSet<String>();
	    
	    /* general page loading performance */
	    long loadEventEnd = (Long)js.executeScript("return window.performance.timing.loadEventEnd;");  
	    long navigationStart = (Long)js.executeScript("return window.performance.timing.navigationStart;");  
	    long responseStart = (Long)js.executeScript("return window.performance.timing.responseStart;");  
	    long connectStart = (Long)js.executeScript("return window.performance.timing.connectStart;"); 
	    log.info("loadEventEnd: {}, navigationStart: {}, responseStart: {}, connectStart: {}", 
			   loadEventEnd, navigationStart, responseStart, connectStart);
	   
	    /* each resource detail loading performance */
	    String getPerformanceResourceEntriesScript = "window.performance.getEntriesByType "
	    		+ "= window.performance.getEntriesByType || window.performance.webkitGetEntriesByType; "
	    		+ "return window.performance.getEntriesByType('resource');";
	    @SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> jsObjects = (ArrayList<Map<String, Object>>)js.executeScript(getPerformanceResourceEntriesScript);
	    for (int i=0; i<jsObjects.size(); i++) {
	    	//get every initiator type that exist in page
			Map<String, Object> entry = jsObjects.get(i);
//			if(!initiatorSet.contains(entry.get("initiatorType"))){
//				initiatorSet.add((String) entry.get("initiatorType"));
//			}
			
			/* do the calculation for xmlhttprequest */
//			if(((String)entry.get("initiatorType")).equals("xmlhttprequest")){
//				//log.info("name: {}, rawTime: {}", entry.get("name"), entry.get("duration"));
//				entryDetail(entry);
//			}
			
			entryDetail(entry);
		}
	   
	    /* see how many kinds of initiatorType */
//	    for(String type: initiatorSet){
//	    	log.info("initiatorSet: {}", type);
//	    }
	}
	
	private void entryDetail(Map<String, Object> entry){
		/* https://github.com/jheiselman/burt/blob/master/burt/src/com/heiselman/burt/ScriptingEngine.java */
		HashMap<String, Double> metrics = new HashMap<String, Double>();
		
		Double duration = Double.parseDouble(entry.get("duration").toString());
		Double redirectStart = Double.parseDouble(entry.get("redirectStart").toString());
		Double redirectEnd = Double.parseDouble(entry.get("redirectEnd").toString());
		Double fetchStart = Double.parseDouble(entry.get("fetchStart").toString());
		Double domainLookupStart = Double.parseDouble(entry.get("domainLookupStart").toString());
		Double domainLookupEnd = Double.parseDouble(entry.get("domainLookupEnd").toString());
		Double connectStart = Double.parseDouble(entry.get("connectStart").toString());
		Double secureConnectionStart = Double.parseDouble(entry.get("secureConnectionStart").toString());
		Double connectEnd = Double.parseDouble(entry.get("connectEnd").toString());
		Double requestStart = Double.parseDouble(entry.get("requestStart").toString());
		Double responseStart = Double.parseDouble(entry.get("responseStart").toString());
		Double responseEnd = Double.parseDouble(entry.get("responseEnd").toString());
		if (connectStart == 0.0 && duration > 0.0) {
			metrics.put("appCacheDuration", duration);
		} else {
			if (redirectEnd > 0) { metrics.put("redirectDuration", redirectEnd - redirectStart); }
			if (fetchStart > 0) { metrics.put("appCacheDuration", domainLookupStart - fetchStart); }
			if (domainLookupStart > 0) { metrics.put("dnsDuration", domainLookupEnd - domainLookupStart); }
			if (connectStart > 0) { metrics.put("connectDuration", connectEnd - connectStart); }
			if (secureConnectionStart > 0) { metrics.put("secureConnectDuration", connectEnd - secureConnectionStart); }
			if (requestStart > 0) { metrics.put("requestDuration", responseStart - requestStart); }
			if (responseStart > 0) { metrics.put("responseDuration", responseEnd - responseStart); }
			if (domainLookupStart > 0 && responseStart > 0) { metrics.put("timeToFirstByte", responseStart - domainLookupStart); } // Network time
			if (domainLookupStart > 0 && responseStart > 0) { metrics.put("timeToLastByte", responseEnd - domainLookupStart); } // Server Time
		}
		
		for(Entry<String, Double> temp:metrics.entrySet()){
			log.info("Show detail: {}, {}ms", temp.getKey(), temp.getValue());
		}
		log.info("==================================================");
	}
	
	/**
	 * Can use two ways
	 * 1. directly request resource url from server, check the time line
	 * 2. use selenium login, then run javascript, use w3c windows.performance to get time line
	 */
	@Test(enabled=false)
	public void testResourceExample(){	

		/**
		 * http://calendar.perfplanet.com/2012/an-introduction-to-the-resource-timing-api/
		 * 
		 * url: document.location.href,
		 * start: 0,
		 * duration: timing.responseEnd - timing.navigationStart,
		 * redirectStart: timing.redirectStart === 0 
		 * ? 0 : timing.redirectStart - timing.navigationStart,
		 * redirectDuration: timing.redirectEnd - timing.redirectStart,
		 * appCacheStart: 0,
		 * appCacheDuration: 0,
		 * dnsStart: timing.domainLookupStart - timing.navigationStart,
		 * dnsDuration: timing.domainLookupEnd - timing.domainLookupStart,
		 * tcpStart: timing.connectStart - timing.navigationStart,
		 * tcpDuration: timing.connectEnd - timing.connectStart,
		 * sslStart: 0,
		 * sslDuration: 0,
		 * requestStart: timing.requestStart - timing.navigationStart,
		 * requestDuration: timing.responseStart - timing.requestStart,
		 * responseStart: timing.responseStart - timing.navigationStart,
		 * responseDuration: timing.responseEnd - timing.responseStart
		 */
		HomePage homePage = HomePage.goIntoHomePage(env, driver, AccountTypeEnum.corpBenchmark, jdbcTemplate);
	    homePage.waitForHomePageToLoad(); 
	    
	    /* page loading time example */
	    Object xxx = ((JavascriptExecutor)driver).executeScript(
	    		"var performance = window.performance || {};" + 
	            "var timings = performance.timing || {};"+
	            "return timings;");
	    log.info("xxxxx: {}", xxx.toString());
	    
	    JavascriptExecutor js = (JavascriptExecutor)driver;

	    long loadEventEnd = (Long)js.executeScript("return window.performance.timing.loadEventEnd;");  
	    long navigationStart = (Long)js.executeScript("return window.performance.timing.navigationStart;");  
	    long responseStart = (Long)js.executeScript("return window.performance.timing.responseStart;");  
	    long connectStart = (Long)js.executeScript("return window.performance.timing.connectStart;"); 
	    
	   
	    log.info("loadEventEnd: {}, navigationStart: {}, responseStart: {}, connectStart: {}", 
			   loadEventEnd, navigationStart, responseStart, connectStart);
	   
	    /* resource loading time example */
	    Set<String> initiatorSet = new HashSet<String>();
	    
	    JavascriptExecutor javascript = (JavascriptExecutor)driver;
	    String getPerformanceResourceEntriesScript = "window.performance.getEntriesByType "
	    		+ "= window.performance.getEntriesByType || window.performance.webkitGetEntriesByType; "
	    		+ "return window.performance.getEntriesByType('resource');";
	    @SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> jsObjects = (ArrayList<Map<String, Object>>)javascript.executeScript(getPerformanceResourceEntriesScript);
	    for (int i=0; i<jsObjects.size(); i++) {
			Map<String, Object> entry = jsObjects.get(i);
			if(!initiatorSet.contains(entry.get("initiatorType"))){
				initiatorSet.add((String) entry.get("initiatorType"));
			}
			/* do the calculation for each resoure here */
			if(((String)entry.get("initiatorType")).equals("xmlhttprequest")){
				log.info("name: {}, rawTime: {}", entry.get("name"), entry.get("duration"));
			}
			/* https://github.com/jheiselman/burt/blob/master/burt/src/com/heiselman/burt/ScriptingEngine.java */
//		    Double duration = Double.parseDouble(entry.get("duration").toString());
//			Double redirectStart = Double.parseDouble(entry.get("redirectStart").toString());
//			Double redirectEnd = Double.parseDouble(entry.get("redirectEnd").toString());
//			Double fetchStart = Double.parseDouble(entry.get("fetchStart").toString());
//			Double domainLookupStart = Double.parseDouble(entry.get("domainLookupStart").toString());
//			Double domainLookupEnd = Double.parseDouble(entry.get("domainLookupEnd").toString());
//			Double connectStart = Double.parseDouble(entry.get("connectStart").toString());
//			Double secureConnectionStart = Double.parseDouble(entry.get("secureConnectionStart").toString());
//			Double connectEnd = Double.parseDouble(entry.get("connectEnd").toString());
//			Double requestStart = Double.parseDouble(entry.get("requestStart").toString());
//			Double responseStart = Double.parseDouble(entry.get("responseStart").toString());
//			Double responseEnd = Double.parseDouble(entry.get("responseEnd").toString());
//			if (connectStart == 0.0 && duration > 0.0) {
//				metrics.put("appCacheDuration", metrics.get("appCacheDuration") + duration);
//			} else {
//				if (redirectEnd > 0) { metrics.put("redirectDuration", metrics.get("redirectDuration") + redirectEnd - redirectStart); }
//				if (fetchStart > 0) { metrics.put("appCacheDuration", metrics.get("appCacheDuration") + domainLookupStart - fetchStart); }
//				if (domainLookupStart > 0) { metrics.put("dnsDuration", metrics.get("dnsDuration") + domainLookupEnd - domainLookupStart); }
//				if (connectStart > 0) { metrics.put("connectDuration", metrics.get("connectDuration") + connectEnd - connectStart); }
//				if (secureConnectionStart > 0) { metrics.put("secureConnectDuration", metrics.get("secureConnectDuration") + connectEnd - secureConnectionStart); }
//				if (requestStart > 0) { metrics.put("requestDuration", metrics.get("requestDuration") + responseStart - requestStart); }
//				if (responseStart > 0) { metrics.put("responseDuration", metrics.get("responseDuration") + responseEnd - responseStart); }
//				if (domainLookupStart > 0 && responseStart > 0) { metrics.put("timeToFirstByte", metrics.get("timeToFirstByte") + responseStart - domainLookupStart); } // Network time
//				if (domainLookupStart > 0 && responseStart > 0) { metrics.put("timeToLastByte", metrics.get("timeToLastByte") + responseEnd - domainLookupStart); } // Server Time
//			}
		}
	   
	    /* see how many kinds of initiatorType */
	    for(String type: initiatorSet){
	    	log.info("initiatorSet: {}", type);
	    }
	    
	    /* how about the Ajax/XHR time ?*/
	    
	}

}
