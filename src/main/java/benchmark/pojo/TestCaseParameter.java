package benchmark.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * use jaxb to read xml file
 * https://www.mkyong.com/java/jaxb-hello-world-example/
 * http://theopentutorials.com/tutorials/java/jaxb/jaxb-marshalling-and-unmarshalling-list-of-objects/
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "testCaseParameter")
public class TestCaseParameter {

	@Override
	public String toString() {
		return "TestCaseParameter [fullMethodName=" + fullMethodName + ", simpleMethodName=" + simpleMethodName
				+ ", retryTimes=" + retryTimes + ", standardScore=" + standardTime + ", timeoutInSeconds="
				+ timeoutInSeconds + ", sleepInMillis=" + sleepInMillis + ", description=" + description + "]";
	}

	private String fullMethodName;
	private String simpleMethodName;
	private int retryTimes;
	private double standardTime;
	private int timeoutInSeconds;
	private int sleepInMillis;
	private String description;
	
	public String getFullMethodName() {
		return fullMethodName;
	}

	public void setFullMethodName(String fullMethodName) {
		this.fullMethodName = fullMethodName;
	}

	public String getSimpleMethodName() {
		return simpleMethodName;
	}

	public void setSimpleMethodName(String simpleMethodName) {
		this.simpleMethodName = simpleMethodName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public double getStandardTime() {
		return standardTime;
	}

	public void setStandardTime(double standardTime) {
		this.standardTime = standardTime;
	}

	public int getTimeoutInSeconds() {
		return timeoutInSeconds;
	}

	public void setTimeoutInSeconds(int timeoutInSeconds) {
		this.timeoutInSeconds = timeoutInSeconds;
	}

	public int getSleepInMillis() {
		return sleepInMillis;
	}

	public void setSleepInMillis(int sleepInMillis) {
		this.sleepInMillis = sleepInMillis;
	}

}
