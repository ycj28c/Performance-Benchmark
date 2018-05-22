package benchmark.report.summary;

/**
 * the factory contains result sets for each kinds of category
 * @author ryang
 *
 */
public class SummaryReportFactory {
	private static BusinessSummaryReport businessSummaryReport = new BusinessSummaryReport();
	private static NetworkSummaryReport networkSummaryReport = new NetworkSummaryReport();
	
	public static BusinessSummaryReport getBusinessSummary(){
		return businessSummaryReport;
	}
	
	public static NetworkSummaryReport getNetworkSummary(){
		return networkSummaryReport;
	}
}
