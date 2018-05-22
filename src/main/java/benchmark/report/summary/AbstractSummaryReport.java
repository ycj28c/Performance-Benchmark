package benchmark.report.summary;

import java.util.ArrayList;
import java.util.List;

import benchmark.report.single.SingleTestResult;
import benchmark.tools.StatisticFormulaUtil;

public abstract class AbstractSummaryReport {
	protected Double SCORE_STANDARD = 100D;
	protected String categoryName;
	protected String buildNumber;
	protected String buildDate;
	protected Double currentScore;
	private List<SingleTestResult> singleTestList = new ArrayList<SingleTestResult>();
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getBuildNumber() {
		return buildNumber;
	}
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}
	public String getBuildDate() {
		return buildDate;
	}
	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}
	public Double getCurrentScore() {
		return currentScore;
	}
	public void setCurrentScore(Double currentScore) {
		this.currentScore = currentScore;
	}
	public List<SingleTestResult> getSingleTestList() {
		return singleTestList;
	}

	/** 
	 * calculate the current score for summary report
	 */
	public Double calculateSummaryScore(){
		if(currentScore !=null&&currentScore!=0D){
			return currentScore;
		}
		
		if(singleTestList == null||singleTestList.isEmpty()){
			this.setCurrentScore(-1D); //if don't have test, set to -1D
		} else {
			List<Double> currentScoreList = new ArrayList<Double>();
			for(int i=0;i<singleTestList.size();i++){
				currentScoreList.add(singleTestList.get(i).getAdjustScore());
			}
			Double avgFirstAttScore = StatisticFormulaUtil.weightAlgorithm(currentScoreList); //weighted algorithm
			this.setCurrentScore(avgFirstAttScore);
		}
		
		return currentScore;
	}
	
//	/** 
//	 * simple calculate the average of score
//	 */
//	public void calculateScore(){
//		if(singleTestList == null||singleTestList.isEmpty()){
//			this.setCurrentScore(SCORE_STANDARD); //ignore the empty 
//		}
//		double score = 0D;
//		for(int i=0;i<singleTestList.size();i++){
//			score += singleTestList.get(i).getAdjustScore();
//		}
//		
//		score = new BigDecimal(String.valueOf(score)).divide(
//				new BigDecimal(String.valueOf(singleTestList.size())), 1,
//				RoundingMode.HALF_UP).doubleValue(); //keep 1 decimal
//		this.setCurrentScore(score);
//	}
	
	/** 
	 * get the target Single Test Result in businessList by test name
	 * @param currentName
	 * @return
	 */
	public SingleTestResult getSingleTestResultByName(String currentName) {
		int size = this.singleTestList.size();
		for(int i=size-1;i>=0;i--){
			String cursorName = this.singleTestList.get(i).getName();
			if(currentName.equals(cursorName)){
				return this.singleTestList.get(i);
			}
		}
		return null;
	}
}
