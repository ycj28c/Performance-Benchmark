package benchmark.report.single;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Detail test result of one test (method)
 * @author ryang
 *
 */
public class SingleTestResult {
	protected double SCORE_STANDARD = 100;
	protected String name;
	protected String description;
	protected int pass;
	protected int fail;
	protected int total;
	//int fairData;
	protected String link;
	protected Date beginTime;
	protected Date endTime;
	protected double ajustScore;
	
	/* history data record */
	//protected Double averageHistoryFirstAttScore; //all history average first attempt score
	protected Double averageHistoryScore; //all history average score
	//protected Double lastFirstAttScore; //last score of first attempt
	protected Double lastAttScroe; //last attempt score
	
	protected List<Map<?,?>> passRecords; //please use LinkedHashMap for easy and correct order
	protected List<Map<?,?>> failRecords;
	protected List<Map<?,?>> fairRecords;
	protected List<String> keys;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public SingleTestResult(){
		passRecords = new ArrayList<Map<?,?>>(); //test pass list
		failRecords = new ArrayList<Map<?,?>>(); //test fail list
		fairRecords = new ArrayList<Map<?,?>>(); //HTTP error, test exception, null or other unclear fails
		keys = new ArrayList<>();
	}
	
	public SingleTestResult(String name, String description){
		this.name = name;
		this.description = description;
		this.link = name+".html";
		
		passRecords = new ArrayList<Map<?,?>>(); //test pass list
		failRecords = new ArrayList<Map<?,?>>(); //test fail list
		fairRecords = new ArrayList<Map<?,?>>(); //HTTP error, test exception, null or other unclear fails
		keys = new ArrayList<>();
	}
	
//	public Double getAverageHistoryFirstAttScore() {
//		return averageHistoryFirstAttScore;
//	}
//
//	public void setAverageHistoryFirstAttScore(Double averageHistoryFirstAttScore) {
//		this.averageHistoryFirstAttScore = averageHistoryFirstAttScore;
//	}

	public Double getAverageHistoryScore() {
		return averageHistoryScore;
	}

	public void setAverageHistoryScore(Double averageHistoryScore) {
		this.averageHistoryScore = averageHistoryScore;
	}

//	public Double getLastFirstAttScore() {
//		return lastFirstAttScore;
//	}
//
//	public void setLastFirstAttScore(Double lastFirstAttScore) {
//		this.lastFirstAttScore = lastFirstAttScore;
//	}

	public Double getLastAttScroe() {
		return lastAttScroe;
	}

	public void setLastAttScroe(Double lastAttScroe) {
		this.lastAttScroe = lastAttScroe;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPass() {
		return pass;
	}
	public void setPass(int pass) {
		this.pass = pass;
	}
	public int getFail() {
		return fail;
	}
	public void setFail(int fail) {
		this.fail = fail;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public double getAdjustScore() {
		return ajustScore;
	}
	public void setAdjustScore(double adjustScore) {
		this.ajustScore = adjustScore;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public List<Map<?, ?>> getPassRecords() {
		return passRecords;
	}
	public List<Map<?, ?>> getFailRecords() {
		return failRecords;
	}
	public List<Map<?, ?>> getFairRecords() {
		return fairRecords;
	}
	public List<String> getKeys() {
		return keys;
	}

	@SuppressWarnings("unchecked")
	public void generateKeysFromRecords(){
		//make sure you use LinkedHashMap for easy and correct order
		if(passRecords!=null&&!passRecords.isEmpty()){
			keys.addAll((Collection<? extends String>) passRecords.get(0).keySet()); 
		} else if(failRecords!=null&&!failRecords.isEmpty()){
			keys.addAll((Collection<? extends String>) failRecords.get(0).keySet()); 
		} else if(fairRecords!=null&&!fairRecords.isEmpty()){
			keys.addAll((Collection<? extends String>) failRecords.get(0).keySet()); //haha, it may cause display keys not looks good, emm
		}
		else{
			throw new IllegalArgumentException("Error, either fail list or pass list is empay");
		}
	}

	public void printFailRecords() {
		if(failRecords!=null&&!failRecords.isEmpty()){
			log.info("============Failed Tests Result============");
		}
		
		if(keys==null||keys.isEmpty()){
			throw new IllegalArgumentException("Error, the keys is empty");
		}
		
		for(int i=0;i<failRecords.size();i++){
			for(String key:keys){
				log.info("key: {}, value:{}" , key, failRecords.get(i).get(key));
			}
		}
	}

}
