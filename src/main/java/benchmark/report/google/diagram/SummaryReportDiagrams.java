package benchmark.report.google.diagram;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import benchmark.dao.BenchmarkPersistDao;
import benchmark.dao.BenchmarkPersistImp;
import benchmark.enumx.TestCategoryEnum;
import benchmark.pojo.PerfBenchmarkSummary;
import benchmark.report.google.diagram.basic.Column;
import benchmark.report.google.diagram.basic.Diagram;
import benchmark.report.google.diagram.basic.Row;
import benchmark.report.google.diagram.basic.Unit;
import benchmark.tools.BigDecimalUtil;

/**
 * the util to create UI summary diagram of google chart
 * @author ryang
 *
 * Example data:
 * {"rows":[{"c":[{"v":"21"},{"v":1053.42},{"v":393.7},{"v":757.6}]},{"c":[{"v":"22"},{"v":1178.74},{"v":335.9},
 * {"v":757.6}]},{"c":[{"v":"23"},{"v":252.713},{"v":130.4},{"v":757.6}]},{"c":[{"v":"52"},{"v":545.36},{"v":263.2},
 * {"v":757.6}]}],"cols":[{"label":"build","type":"string"},{"label":"Adjust Score","type":"number"},
 * {"label":"First Attempt Score","type":"number"},{"label":"Avg Adjust Score","type":"number"}]}
 *
 *
 */
public class SummaryReportDiagrams {
	//private Diagram diagram;
	private String env;
	private int limit; //how much diagram data will display or query
	
	public SummaryReportDiagrams(String env, int limit){
		this.env = env;
		this.limit = limit;
	}
	
	/**
	 * get business diagram data
	 * @return
	 */
	public Diagram getBusinessDiagramData(){
		Diagram diagram = new Diagram();
		
		BenchmarkPersistDao bpd = new BenchmarkPersistImp();
		List<PerfBenchmarkSummary> pbsList = bpd.getRecentSummaryList(limit, env, TestCategoryEnum.BUSINESS);
		List<Long> summaryIDList = new ArrayList<Long>();
		List<Row> rowList = new ArrayList<Row>();
		Double sum = 0D;
		
		int cnt=0;
		for(;cnt<pbsList.size();cnt++){
			PerfBenchmarkSummary item = pbsList.get(cnt);
			summaryIDList.add(item.getPerfBenchmarkSummaryID());
			sum += item.getAdjustScore();
			
			Row row = new Row();
//			row.addUnit(new Unit<String>(String.valueOf(item.getPerfBenchmarkSummaryID()))).addUnit(new Unit<Double>(item.getAdjustScore()));
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");  // "yyyy/MM/dd HH:mm:ss"
			String createDate = sdf.format(item.getCreateDate());
			row.addUnit(new Unit<String>(createDate + " #" + item.getPerfBenchmarkSummaryID()))
					.addUnit(new Unit<Double>(item.getAdjustScore()));
			
			Double avgFirstAttScore = bpd.getAvgFirstAttScoreBySummaryID(item.getPerfBenchmarkSummaryID(), env);
			row.addUnit(new Unit<Double>(avgFirstAttScore));
			rowList.add(row);
		}
		if(cnt==0){
			return null;
		}
		/* calculate the average score and set to each row */
		sum = BigDecimalUtil.roundDecimalDouble(sum/cnt, 1);
		for(Row item: rowList){
			item.addUnit(new Unit<Double>(sum));
			diagram.addRow(item);
		}
		
		/* set up column */
		Column col1 = new Column("build", "string");
		Column col2 = new Column("Adjust Score", "number");
		Column col3 = new Column("First Attempt Score", "number");
		Column col4 = new Column("Avg Adjust Score", "number");
		diagram.addCol(col1).addCol(col2).addCol(col3).addCol(col4);
		
		return diagram;
	}
	
	/**
	 * get network diagram data
	 * @return
	 */
	public Diagram getNetworkDiagramData(){
		Diagram diagram = new Diagram();
		
		BenchmarkPersistDao bpd = new BenchmarkPersistImp();
		List<PerfBenchmarkSummary> pbsList = bpd.getRecentSummaryList(limit, env, TestCategoryEnum.NETWORK);
		List<Long> summaryIDList = new ArrayList<Long>();
		List<Row> rowList = new ArrayList<Row>();
		Double sum = 0D;
		
		int cnt=0;
		for(;cnt<pbsList.size();cnt++){
			PerfBenchmarkSummary item = pbsList.get(cnt);
			summaryIDList.add(item.getPerfBenchmarkSummaryID());
			sum += item.getAdjustScore();
			
			Row row = new Row();
			row.addUnit(new Unit<String>(String.valueOf(item.getPerfBenchmarkSummaryID())))
				.addUnit(new Unit<Double>(item.getAdjustScore()));
			
			rowList.add(row);
		}
		if(cnt==0){
			return null;
		}
		/* calculate the average score and set to each row */
		sum = BigDecimalUtil.roundDecimalDouble(sum/cnt, 1);
		for(Row item: rowList){
			item.addUnit(new Unit<Double>(sum));
			diagram.addRow(item);
		}
		
		/* set up column */
		Column col1 = new Column("build", "string");
		Column col2 = new Column("Adjust Score", "number");
		Column col3 = new Column("Avg Adjust Score", "number");
		diagram.addCol(col1).addCol(col2).addCol(col3);
		
		return diagram;
	}
	
	
	@SuppressWarnings("unused")
	private void example(){
		/* create summary diagram JSON */
		Diagram diagram = new Diagram();
		Column col1 = new Column("build", "string");
		Column col2 = new Column("Adjust Score", "number");
		diagram.addCol(col1).addCol(col2);
		Row row1 = new Row();
		row1.addUnit(new Unit<String>("#110"))
			.addUnit(new Unit<Integer>(144));
		diagram.addRow(row1);
	}
}
