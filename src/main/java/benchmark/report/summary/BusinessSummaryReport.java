package benchmark.report.summary;

import benchmark.enumx.TestCategoryEnum;

public class BusinessSummaryReport extends AbstractSummaryReport{
	
	public BusinessSummaryReport(){
		this.categoryName = TestCategoryEnum.BUSINESS.getValue();
	}
	
	public double getHistoryAvgScore(){
		//TODO
		return 0;
	}
	
}
