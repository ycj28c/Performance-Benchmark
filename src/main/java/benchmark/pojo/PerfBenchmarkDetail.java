package benchmark.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * don't move the class location, if you do, please also change the hibernate xml in /resources/hbm
 */
@Entity
@Table(name = "PERF_BENCHMARK_DETAIL")
public class PerfBenchmarkDetail {
	private long perfBenchmarkDetailID;
	private String TestName;
	private long perfBenchmarkSummaryID;
	private double adjustScore;
	private double firstAttemptScore;
	private long pass;
	private long fail;
	private long total;
	private String description;
	private Date startTime;
	private Date endTime;
	private Date createDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_PERF_BENCHMARK_DETAIL")
    @SequenceGenerator(name="SEQ_PERF_BENCHMARK_DETAIL", sequenceName="SEQ_PERF_BENCHMARK_DETAIL", allocationSize=1)
	@Column(name = "PERF_BENCHMARK_DETAIL_ID", unique = true, nullable = false)
	public long getPerfBenchmarkDetailID() {
		return perfBenchmarkDetailID;
	}
	public void setPerfBenchmarkDetailID(long perfBenchmarkDetailID) {
		this.perfBenchmarkDetailID = perfBenchmarkDetailID;
	}
	@Column(name = "TEST_NAME", nullable = true)
	public String getTestName() {
		return TestName;
	}
	public void setTestName(String testName) {
		TestName = testName;
	}
	@Column(name = "PERF_BENCHMARK_SUMMARY_ID", nullable = false)
	public long getPerfBenchmarkSummaryID() {
		return perfBenchmarkSummaryID;
	}
	public void setPerfBenchmarkSummaryID(long perfBenchmarkSummaryID) {
		this.perfBenchmarkSummaryID = perfBenchmarkSummaryID;
	}
	@Column(name = "ADJUST_SCORE", nullable = true)
	public double getAdjustScore() {
		return adjustScore;
	}
	public void setAdjustScore(double adjustScore) {
		this.adjustScore = adjustScore;
	}
	@Column(name = "FIRST_ATTEMPT_SCORE", nullable = true)
	public double getFirstAttemptScore() {
		return firstAttemptScore;
	}
	public void setFirstAttemptScore(double firstAttemptScore) {
		this.firstAttemptScore = firstAttemptScore;
	}
	@Column(name = "PASS", nullable = true)
	public long getPass() {
		return pass;
	}
	public void setPass(long pass) {
		this.pass = pass;
	}
	@Column(name = "FAIL", nullable = true)
	public long getFail() {
		return fail;
	}
	public void setFail(long fail) {
		this.fail = fail;
	}
	@Column(name = "TOTAL", nullable = true)
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	@Column(name = "DESCRIPTION", nullable = true)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "START_TIME", nullable = true)
	@Type(type="timestamp")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Column(name = "END_TIME", nullable = true)
	@Type(type="timestamp")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Column(name = "CREATE_DATE", nullable = true)
	@Type(type="timestamp")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
