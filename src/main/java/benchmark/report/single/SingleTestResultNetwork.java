package benchmark.report.single;

public class SingleTestResultNetwork extends SingleTestResult {
	
	protected Double minimumTime;
	protected Double maximumTime; 
	protected Double averageTime;
	protected String pingDetail;
	protected String ipAddress;
	
	public SingleTestResultNetwork(String name, String description){
		super(name, description);
	}

	public Double getMinimumTime() {
		return minimumTime;
	}

	public void setMinimumTime(Double minimumTime) {
		this.minimumTime = minimumTime;
	}

	public Double getMaximumTime() {
		return maximumTime;
	}

	public void setMaximumTime(Double maximumTime) {
		this.maximumTime = maximumTime;
	}

	public Double getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(Double averageTime) {
		this.averageTime = averageTime;
	}

	public String getPingDetail() {
		return pingDetail;
	}

	public void setPingDetail(String pingDetail) {
		this.pingDetail = pingDetail;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
}
