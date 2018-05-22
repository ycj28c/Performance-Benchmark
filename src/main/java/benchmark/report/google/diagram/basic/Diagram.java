package benchmark.report.google.diagram.basic;

import java.util.ArrayList;
import java.util.List;

public class Diagram {
	List<Row> rows;
	List<Column> cols;
	
	public Diagram(){
		this.rows = new ArrayList<Row>();
		this.cols = new ArrayList<Column>();
	}
	
	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public List<Column> getCols() {
		return cols;
	}

	public void setCols(List<Column> cols) {
		this.cols = cols;
	}
	
	public Diagram addCol(Column col){
		this.cols.add(col);
		return this;
	}
	
	public Diagram addRow(Row row){
		this.rows.add(row);
		return this;
	}
}
