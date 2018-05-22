package benchmark.report.google.diagram.basic;

public class Column {
	String type;
	String label;
	
	public Column(String label, String type){
		this.type = type;
		this.label = label;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
