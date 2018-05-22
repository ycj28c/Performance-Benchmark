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
@Table(name = "PERF_BENCHMARK_DET_ATT")
public class PerfBenchmarkDetAtt {
	private long perfBenchmarkDetAttID; //id
	private long attemptOrder;
	private long perfBenchmarkDetailID;
	private double score;
	private String status;
	private Date startTime;
	private Date endTime;
	private Date createDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_PERF_BENCHMARK_DET_ATT")
    @SequenceGenerator(name="SEQ_PERF_BENCHMARK_DET_ATT", sequenceName="SEQ_PERF_BENCHMARK_DET_ATT", allocationSize=1)
	@Column(name = "PERF_BENCHMARK_DET_ATT_ID", unique = true, nullable = false)
	public long getPerfBenchmarkDetAttID() {
		return perfBenchmarkDetAttID;
	}
	public void setPerfBenchmarkDetAttID(long perfBenchmarkDetAttID) {
		this.perfBenchmarkDetAttID = perfBenchmarkDetAttID;
	}
	@Column(name = "ATTEMPT_ORDER", nullable = true)
	public long getAttemptOrder() {
		return attemptOrder;
	}
	public void setAttemptOrder(long attemptOrder) {
		this.attemptOrder = attemptOrder;
	}
	@Column(name = "perf_benchmark_detail_id", nullable = false)
	public long getPerfBenchmarkDetailID() {
		return perfBenchmarkDetailID;
	}
	public void setPerfBenchmarkDetailID(long perfBenchmarkDetailID) {
		this.perfBenchmarkDetailID = perfBenchmarkDetailID;
	}
	@Column(name = "SCORE", nullable = true)
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	@Column(name = "STATUS", nullable = true)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
