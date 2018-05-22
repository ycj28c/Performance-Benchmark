package benchmark.pojo;

public class TimeMachineUnitPojo {
	
	public String getTest_name() {
		return test_name;
	}

	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}

	public String getResult_key() {
		return result_key;
	}

	public void setResult_key(String result_key) {
		this.result_key = result_key;
	}

	public Double getMedian() {
		return median;
	}

	public void setMedian(Double median) {
		this.median = median;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	private String test_name;
	private String result_key;
	private Double median;
	private Double max;
	
	public TimeMachineUnitPojo(String testName, String resultKey, Double median, Double max){
		this.test_name = testName;
		this.result_key = resultKey;
		this.median = median;
		this.max = max;
	}
}
