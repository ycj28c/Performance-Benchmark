package benchmark.pojo;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class TimeMachineCompare {

	public List<TimeMachineUnitPojo> getCurrent() {
		return current;
	}

	public void setCurrent(List<TimeMachineUnitPojo> current) {
		this.current = current;
	}

	public List<TimeMachineUnitPojo> getPrevious() {
		return previous;
	}

	public void setPrevious(List<TimeMachineUnitPojo> previous) {
		this.previous = previous;
	}

	public List<TimeMachineUnitPojo> getBeforeLast() {
		return beforeLast;
	}

	public void setBeforeLast(List<TimeMachineUnitPojo> beforeLast) {
		this.beforeLast = beforeLast;
	}

	private List<TimeMachineUnitPojo> current;
	private List<TimeMachineUnitPojo> previous;	
	private List<TimeMachineUnitPojo> beforeLast;
	
	public TimeMachineCompare(List<TimeMachineUnitPojo> current, List<TimeMachineUnitPojo> previous,
			List<TimeMachineUnitPojo> beforeLast) {
		this.current = current;
		this.previous = previous;
		this.beforeLast = beforeLast;
		
		organizeTimeMachine();
	}
	
	private void adjustList(List<TimeMachineUnitPojo> list, HashSet<String> hashset){
		if(hashset.size()!=list.size()){
			for(String name : hashset){
				boolean hit = false;
				for(int i=0;i<list.size();i++){
					if(name.equals(list.get(i).getTest_name() + "," + list.get(i).getResult_key())){
						hit = true;
						break;
					}
				}
				if(!hit){
					TimeMachineUnitPojo tmup = new TimeMachineUnitPojo(name.split(",")[0], name.split(",")[1], -99999999D, -99999999D);
					list.add(tmup);
				}
			}
		}
		
		/* sort the list */
		Collections.sort(list, new Comparator<TimeMachineUnitPojo>(){
            public int compare(TimeMachineUnitPojo arg0, TimeMachineUnitPojo arg1) {
            	return (arg0.getTest_name()+arg0.getResult_key()).compareTo(arg1.getTest_name()+arg1.getResult_key());
            }
        });
	}
	
	public void organizeTimeMachine(){
		if(this.current == null || this.previous == null || this.beforeLast == null) return;
		HashSet<String> hashset = new HashSet<String>();
		for (int i = 0; i < this.current.size(); i++) {
			System.out.println(this.current.get(i).getTest_name() + "," + this.current.get(i).getResult_key());
			hashset.add(this.current.get(i).getTest_name() + "," + this.current.get(i).getResult_key());
		}
		for (int i = 0; i < this.previous.size(); i++) {
			hashset.add(this.previous.get(i).getTest_name() + "," + this.previous.get(i).getResult_key());
		}
		for (int i = 0; i < this.beforeLast.size(); i++) {
			hashset.add(this.beforeLast.get(i).getTest_name() + "," + this.beforeLast.get(i).getResult_key());
		}
		
		adjustList(this.current, hashset);
		adjustList(this.previous, hashset);
		adjustList(this.beforeLast, hashset);
	}
}
