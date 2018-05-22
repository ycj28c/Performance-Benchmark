package benchmark.pojo;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import benchmark.report.google.timeline.TimelineUnit;

public class PageTimingPojo {
	private WebDriver driver;
	
	private long connectEnd;
	private long connectStart;
	private long domComplete;
	private long domContentLoadedEventEnd;
	private long domContentLoadedEventStart;
	private long domInteractive;
	private long domLoading;
	private long domainLookupEnd;
	private long domainLookupStart;
	private long fetchStart;
	private long loadEventEnd;
	private long loadEventStart;
	private long navigationStart;
	private long redirectEnd;
	private long redirectStart;
	private long requestStart;
	private long responseEnd;
	private long responseStart;
	private long secureConnectionStart;
	
	public PageTimingPojo(WebDriver driver){
		this.driver = driver;
		initialAttribute();
	}
	
	private void initialAttribute(){
		JavascriptExecutor js = (JavascriptExecutor)driver;

		this.connectEnd = (Long)js.executeScript("return window.performance.timing.connectEnd;");  
		this.connectStart = (Long)js.executeScript("return window.performance.timing.connectStart;");  
		this.domComplete = (Long)js.executeScript("return window.performance.timing.domComplete;");
		this.domContentLoadedEventEnd = (Long)js.executeScript("return window.performance.timing.domContentLoadedEventEnd;");
		this.domContentLoadedEventStart = (Long)js.executeScript("return window.performance.timing.domContentLoadedEventStart;");
		this.domInteractive = (Long)js.executeScript("return window.performance.timing.domInteractive;");
		this.domLoading = (Long)js.executeScript("return window.performance.timing.domLoading;");
		this.domainLookupEnd = (Long)js.executeScript("return window.performance.timing.domainLookupEnd;"); 
		this.domainLookupStart = (Long)js.executeScript("return window.performance.timing.domainLookupStart;");
		this.fetchStart = (Long)js.executeScript("return window.performance.timing.fetchStart;");
		this.loadEventEnd = (Long)js.executeScript("return window.performance.timing.loadEventEnd;");
		this.loadEventStart = (Long)js.executeScript("return window.performance.timing.loadEventStart;");
		this.navigationStart = (Long)js.executeScript("return window.performance.timing.navigationStart;");
		this.redirectEnd = (Long)js.executeScript("return window.performance.timing.redirectEnd;");
		this.redirectStart = (Long)js.executeScript("return window.performance.timing.redirectStart;");
		this.requestStart = (Long)js.executeScript("return window.performance.timing.requestStart;");
		this.responseEnd = (Long)js.executeScript("return window.performance.timing.responseEnd;");
		this.responseStart = (Long)js.executeScript("return window.performance.timing.responseStart;");
		this.secureConnectionStart = (Long)js.executeScript("return window.performance.timing.secureConnectionStart;");
		
	}
	
	public List<TimelineUnit> toTimlineUnitList(){
		List<TimelineUnit> list = new ArrayList<TimelineUnit>();
		
		//page connection establish
		if(redirectEnd!=0&&redirectStart!=0){
			list.add(new TimelineUnit("Redirect", "Redirect", redirectStart, redirectEnd));
			list.add(new TimelineUnit("Navigation", "Navigation", navigationStart , redirectStart));
		} else {
			list.add(new TimelineUnit("Navigation", "Navigation", navigationStart , fetchStart));
		}
		list.add(new TimelineUnit("App cache", "App cache", fetchStart , domainLookupStart));
		list.add(new TimelineUnit("DNS", "DNS", domainLookupStart , domainLookupEnd));
		list.add(new TimelineUnit("TCP", "TCP", connectStart , connectEnd));
		if(secureConnectionStart!=0){
			list.add(new TimelineUnit("SSL", "SSL", secureConnectionStart, connectEnd));
		}
		list.add(new TimelineUnit("Request", "Request", requestStart, responseStart));
		list.add(new TimelineUnit("Response", "Response", responseStart, responseEnd));
		list.add(new TimelineUnit("Page connection establish", "Page connection establish", navigationStart, responseEnd));
		
		//DOM Processing
		list.add(new TimelineUnit("DOM parse", "DOM parse", domLoading, domInteractive));
		list.add(new TimelineUnit("DOM content load", "DOM content load", domContentLoadedEventStart, domContentLoadedEventEnd));
		list.add(new TimelineUnit("DOM processing", "DOM processing", domLoading, domComplete));
		
		//Load Processing
		list.add(new TimelineUnit("onLoad Event", "onLoad Event", loadEventStart, loadEventEnd));
		
		return list;
	}
	
	public long getConnectEnd() {
		return connectEnd;
	}

	public long getConnectStart() {
		return connectStart;
	}

	public long getDomComplete() {
		return domComplete;
	}

	public long getDomContentLoadedEventEnd() {
		return domContentLoadedEventEnd;
	}

	public long getDomContentLoadedEventStart() {
		return domContentLoadedEventStart;
	}

	public long getDomInteractive() {
		return domInteractive;
	}

	public long getDomLoading() {
		return domLoading;
	}

	public long getDomainLookupEnd() {
		return domainLookupEnd;
	}

	public long getDomainLookupStart() {
		return domainLookupStart;
	}

	public long getFetchStart() {
		return fetchStart;
	}

	public long getLoadEventEnd() {
		return loadEventEnd;
	}

	public long getLoadEventStart() {
		return loadEventStart;
	}

	public long getNavigationStart() {
		return navigationStart;
	}

	public long getRedirectEnd() {
		return redirectEnd;
	}

	public long getRedirectStart() {
		return redirectStart;
	}

	public long getRequestStart() {
		return requestStart;
	}

	public long getResponseEnd() {
		return responseEnd;
	}

	public long getResponseStart() {
		return responseStart;
	}

	public long getSecureConnectionStart() {
		return secureConnectionStart;
	}


}
