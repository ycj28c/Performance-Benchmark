package benchmark.report.google.timeline;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import benchmark.enumx.ResultStatusEnum;

public class TimeLineModal {
	private List<TimelineUnit> timeLineUnitList;
	//private Double during;
	private Map<String, Double> duringMap; 
	private Double standardTimeCost; //the standard time cost for test work flow
	private Double score; //the current score
	private String status; //the current score
	
	public TimeLineModal(){
		duringMap = new LinkedHashMap<String, Double>();
	}
	
	public TimeLineModal(List<TimelineUnit> timeLineUnitList, Double stardardTimeCost, Double score, ResultStatusEnum status){
		this.timeLineUnitList = timeLineUnitList;
		this.standardTimeCost = stardardTimeCost;
		this.score = score;
		this.status = status.getValue();
		
		duringMap = new LinkedHashMap<String, Double>();
	}
	
	/** 
	 * add during time value to during set, which will display in final report
	 * @param key
	 * @param value
	 */
	public void addDuring(String key, Double value){
		duringMap.put(key, value);
	}
	
	public Map<String, Double> getDuring(){
		return duringMap;
	}

	public List<TimelineUnit> getTimeLineUnitList() {
		return timeLineUnitList;
	}

	public void setTimeLineUnitList(List<TimelineUnit> timeLineUnitList) {
		this.timeLineUnitList = timeLineUnitList;
	}

	public Double getStandardTimeCost() {
		return standardTimeCost;
	}

	public void setStandardTimeCost(Double standardTimeCost) {
		this.standardTimeCost = standardTimeCost;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
