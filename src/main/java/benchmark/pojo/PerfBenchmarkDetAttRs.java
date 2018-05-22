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
@Table(name = "PERF_BENCHMARK_DET_ATT_RS")
public class PerfBenchmarkDetAttRs {
	private long perfBenchmarkDetAttRsID;
	private long perfBenchmarkDetAttID;
	private String resultKey;
	private Double resultValueSecond;
	private Double goal;
	private Date createDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_PERF_BENCHMARK_DET_ATT_RS")
    @SequenceGenerator(name="SEQ_PERF_BENCHMARK_DET_ATT_RS", sequenceName="SEQ_PERF_BENCHMARK_DET_ATT_RS", allocationSize=1)
	@Column(name = "PERF_BENCHMARK_DET_ATT_RS_ID", unique = true, nullable = false)
	public long getPerfBenchmarkDetAttRsID() {
		return perfBenchmarkDetAttRsID;
	}
	public void setPerfBenchmarkDetAttRsID(long perfBenchmarkDetAttRsID) {
		this.perfBenchmarkDetAttRsID = perfBenchmarkDetAttRsID;
	}
	@Column(name = "PERF_BENCHMARK_DET_ATT_ID", nullable = false)
	public long getPerfBenchmarkDetAttID() {
		return perfBenchmarkDetAttID;
	}
	public void setPerfBenchmarkDetAttID(long perfBenchmarkDetAttID) {
		this.perfBenchmarkDetAttID = perfBenchmarkDetAttID;
	}
	@Column(name = "RESULT_KEY", nullable = true)
	public String getResultKey() {
		return resultKey;
	}
	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	}
	@Column(name = "RESULT_VALUE_SECOND", nullable = true)
	public Double getResultValueSecond() {
		return resultValueSecond;
	}
	public void setResultValueSecond(Double resultValueSecond) {
		this.resultValueSecond = resultValueSecond;
	}
	@Column(name = "GOAL", nullable = true)
	public Double getGoal() {
		return goal;
	}
	public void setGoal(Double goal) {
		this.goal = goal;
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
