package benchmark.report.single;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import benchmark.report.google.timeline.TimeLineModal;

public class SingleTestResultBusiness extends SingleTestResult{
	private List<TimeLineModal> timeLineModalList;
	
	protected Double averageHistoryFirstAttScore; //all history average first attempt score
	protected Double lastFirstAttScore; //last score of first attempt
	
	public Double getAverageHistoryFirstAttScore() {
		return averageHistoryFirstAttScore;
	}

	public void setAverageHistoryFirstAttScore(Double averageHistoryFirstAttScore) {
		this.averageHistoryFirstAttScore = averageHistoryFirstAttScore;
	}
	
	public Double getLastFirstAttScore() {
		return lastFirstAttScore;
	}

	public void setLastFirstAttScore(Double lastFirstAttScore) {
		this.lastFirstAttScore = lastFirstAttScore;
	}

	public SingleTestResultBusiness(){
		timeLineModalList = new ArrayList<TimeLineModal>();
	}
	
	public SingleTestResultBusiness(String currentMethodName, String description) {
		super(currentMethodName, description);
		timeLineModalList = new ArrayList<TimeLineModal>();
	}

	public List<TimeLineModal> getTimeLineModalList() {
		return timeLineModalList;
	}

	public void setTimeLineModalList(List<TimeLineModal> timeLineModalList) {
		this.timeLineModalList = timeLineModalList;
	}
	
	/**
	 * score logic for business 
	 */
	public void calculateAdjustScore() {
		if(timeLineModalList==null||timeLineModalList.isEmpty()){
			this.setAdjustScore(SCORE_STANDARD); //ignore this score, 100 is the standard value
		}
		BigDecimal score = new BigDecimal("0");
		for(int i=0;i<timeLineModalList.size();i++){
			score = new BigDecimal(String.valueOf(timeLineModalList.get(i).getScore())).add(score);
		}
		Double adjustScore = score.divide(new BigDecimal(String.valueOf(timeLineModalList.size())), 3, RoundingMode.HALF_UP).doubleValue();
		if(adjustScore>100){
			adjustScore = 100D;
		}
		log.debug("setAdjustScore: adjust score {}", adjustScore);
		this.setAdjustScore(adjustScore);
	}
}
