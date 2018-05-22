package benchmark.dao;

import java.util.List;

import org.springframework.core.env.Environment;

import benchmark.enumx.TestCategoryEnum;
import benchmark.pojo.PerfBenchmarkDetail;
import benchmark.pojo.PerfBenchmarkSummary;
import benchmark.pojo.TimeMachineUnitPojo;

public interface BenchmarkPersistDao {
	
	/**
	 * get the most recent ** results, calculate the history average score of first attempts 
	 * @param methodName
	 * @return
	 */
	public Double getAverageHistoryFirstAttScore(String methodName, Environment env);
	
	/**
	 * get the most recent ** result, calculate the history average score
	 * @param methodName
	 * @return
	 */
	public Double getAverageHistoryScore(String methodName, Environment env);
	
	/**
	 * get the last result, get the score of first attempts
	 * @param methodName
	 * @return
	 */
	public Double getLastFirstAttScore(String methodName, Environment env);
	
	/**
	 * get the last result, get the score
	 * @param methodName
	 * @return
	 */
	public Double getLastAttScroe(String methodName, Environment env);
	
	/**
	 * get the list of recent # of summary data
	 * @param limit
	 * @param env
	 * @param category
	 * @return
	 */
	public List<PerfBenchmarkSummary> getRecentSummaryList(int limit, String env, TestCategoryEnum category);
	
	/**
	 * get the average first attempt score by summary id
	 * @param summaryID
	 * @param env
	 * @return
	 */
	public Double getAvgFirstAttScoreBySummaryID(Long summaryID, String env);
	
	/**
	 * persist all the test result to database
	 * @param env
	 */
	public void persistAllTestResult(String env);

	/**
	 * get the list of recent # of detail data
	 * @param limit
	 * @param testName
	 * @return
	 */
	public List<PerfBenchmarkDetail> getRecentDetailList(int limit, String testName);

	/**
	 * get the time machine compare data by summary id
	 * @param summaryId
	 * @return
	 */
	public List<TimeMachineUnitPojo> getTimeMachineDetailBySummaryId(Long summaryId);
}
