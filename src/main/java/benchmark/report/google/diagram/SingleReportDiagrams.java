package benchmark.report.google.diagram;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.env.Environment;

import benchmark.dao.BenchmarkPersistDao;
import benchmark.dao.BenchmarkPersistImp;
import benchmark.pojo.PerfBenchmarkDetail;
import benchmark.report.google.diagram.basic.Column;
import benchmark.report.google.diagram.basic.Diagram;
import benchmark.report.google.diagram.basic.Row;
import benchmark.report.google.diagram.basic.Unit;
import benchmark.tools.BigDecimalUtil;

public class SingleReportDiagrams {

	private String testName;
	private Environment env;
	private int limit; //how much diagram data will display or query
	
	public SingleReportDiagrams(String testName, Environment env, int limit){
		this.testName = testName;
		this.env = env;
		this.limit = limit;
	}
	
	/**
	 * get business time machine diagram data
	 * @return
	 */
	public Diagram getBusinessTimeMachineData(){
		Diagram diagram = new Diagram();
		
		BenchmarkPersistDao bpd = new BenchmarkPersistImp();
		List<PerfBenchmarkDetail> pbdList = bpd.getRecentDetailList(limit, testName);
		if(pbdList == null || pbdList.isEmpty()){
			return null;
		}
		List<Long> DetailIDList = new ArrayList<Long>();
		List<Row> rowList = new ArrayList<Row>();
		Double sum = 0D;
		
		int cnt=0;
		for(;cnt<pbdList.size();cnt++){
			PerfBenchmarkDetail item = pbdList.get(cnt);
			DetailIDList.add(item.getPerfBenchmarkDetailID());
			sum += item.getAdjustScore();
			
			Row row = new Row();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");  // "yyyy/MM/dd HH:mm:ss"
			String createDate = sdf.format(item.getCreateDate());
			row.addUnit(new Unit<String>(createDate)).addUnit(new Unit<Double>(item.getAdjustScore()));
			
			/* can display avg without below code? */
			Double avgHistoryScore = bpd.getAverageHistoryScore(testName, env);
			row.addUnit(new Unit<Double>(avgHistoryScore));
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
		Column col2 = new Column("Current Avg. Score", "number");
		Column col3 = new Column("History Avg. Score", "number");
		diagram.addCol(col1).addCol(col2).addCol(col3);
		
		return diagram;
	}
}
