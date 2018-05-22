package benchmark.report.summary;

import benchmark.enumx.TestCategoryEnum;

public class NetworkSummaryReport extends AbstractSummaryReport{

	public NetworkSummaryReport(){
		this.categoryName = TestCategoryEnum.NETWORK.getValue();
	}
	
	//List<HistoryTestResult> historyResults;
}
