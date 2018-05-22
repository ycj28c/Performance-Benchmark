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
@Table(name = "PERF_BENCHMARK_SUMMARY")
public class PerfBenchmarkSummary {
	private long PerfBenchmarkSummaryID;
	private String environment;
	private double adjustScore;
	private String categoryName;
	private Date createDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_PERF_BENCHMARK_SUMMARY")
    @SequenceGenerator(name="SEQ_PERF_BENCHMARK_SUMMARY", sequenceName="SEQ_PERF_BENCHMARK_SUMMARY", allocationSize=1)
	@Column(name = "PERF_BENCHMARK_SUMMARY_ID", unique = true, nullable = false)
	public long getPerfBenchmarkSummaryID() {
		return PerfBenchmarkSummaryID;
	}
	public void setPerfBenchmarkSummaryID(long perfBenchmarkSummaryID) {
		PerfBenchmarkSummaryID = perfBenchmarkSummaryID;
	}
	@Column(name = "ENVIRONMENT", nullable = true)
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	@Column(name = "ADJUST_SCORE", nullable = true)
	public double getAdjustScore() {
		return adjustScore;
	}
	public void setAdjustScore(double adjustScore) {
		this.adjustScore = adjustScore;
	}
	@Column(name = "CATEGORY_NAME", nullable = true)
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
