package benchmark.report.google.diagram.basic;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class Row {
	
	private List<Unit> c;
	
	public Row(){
		this.c = new ArrayList<Unit>();
	}

	public List<Unit> getC() {
		return c;
	}

	public void setC(List<Unit> c) {
		this.c = c;
	}

	public Row addUnit(Unit unit){
		this.c.add(unit);
		return this;
	}	
}
