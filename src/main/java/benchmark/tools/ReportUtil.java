package benchmark.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import benchmark.dao.BenchmarkPersistDao;
import benchmark.dao.BenchmarkPersistImp;
import benchmark.enumx.TestCategoryEnum;
import benchmark.pojo.PerfBenchmarkSummary;
import benchmark.pojo.TimeMachineCompare;
import benchmark.pojo.TimeMachineUnitPojo;
import benchmark.report.HtmlReportTemplate;
import benchmark.report.google.diagram.SummaryReportDiagrams;
import benchmark.report.google.diagram.SingleReportDiagrams;
import benchmark.report.google.diagram.basic.Diagram;
import benchmark.report.summary.SummaryReportFactory;

/**
 * We has a HTML report index, every test has it is own HTML.
 * 
 * @author ryang
 *
 */
public class ReportUtil {
	public String reportName;
	public String path;
	
	private final static String resourcePath = "src/test/resources";
	private final static String outputPath = "target/benchmarkReport"; 
	private static final Logger log = LoggerFactory.getLogger(ReportUtil.class);
	
	public static void generateSingleTestReport(JSONObject jsonObj, String currentMethodName, Environment env, TestCategoryEnum category) {
		log.info("begin generateSingleTestReport");
		
//		JsonUtil ju = new JsonUtil(jsonObj);
//		ju.writeToJsonFile(outputPath, currentMethodName, category);
		
		HtmlReportTemplate hrt = null;
		switch(category){
			case BUSINESS:
				hrt = new HtmlReportTemplate("htmlTemplates/businessTestTemplate.html");
				break;
			case NETWORK:
				hrt = new HtmlReportTemplate("htmlTemplates/networkTestTemplate.html");
				break;
			default:
				break;
		}
		
		/* create summary diagram JSON */
		SingleReportDiagrams ggdu = new SingleReportDiagrams(currentMethodName, env, 15);
		String timeMachineStr = "";
		try {
			Diagram diagram = ggdu.getBusinessTimeMachineData();
			JSONObject timeMachineJson = new JSONObject(diagram);
			timeMachineStr = timeMachineJson.toString();
		} catch(Exception e){
			log.error("Generate time machine Json string fail", e);
		}
		
//		hrt.setJsonKey(currentMethodName).setTitle(currentMethodName).setEnv(env.getRequiredProperty("env"));
		hrt.setJsonContent(jsonObj.toString()).setTitle(currentMethodName)
				.setEnv(env.getRequiredProperty("env"))
				.setJsonTimeMachine(timeMachineStr);
		hrt.writeToHtmlFile(outputPath, currentMethodName, category);
	}
	
//	public static void generateSingleUITestReport(JSONObject jsonObj, String currentMethodName, Environment env) {
//		log.info("begin generateSingleUITestReport");
//		
//		JsonUtil ju = new JsonUtil(jsonObj);
//		ju.writeToJsonFile(outputPath, currentMethodName, TestCategoryEnum.BUSINESS);
//		
//		//HtmlReportTemplate hrt = new HtmlReportTemplate("htmlTemplates/uiTestTemplate.html");
//		HtmlReportTemplate hrt = new HtmlReportTemplate("htmlTemplates/businessTestTemplate.html");
//		//HtmlReportTemplate hrt = new HtmlReportTemplate("uiTestTemplate.html");
//		hrt.setJson(currentMethodName).setTitle(currentMethodName).setEnv(env.getRequiredProperty("env"));
//		hrt.writeToHtmlFile(outputPath, currentMethodName, TestCategoryEnum.BUSINESS);
//	}
	
	/**
	 * generate all category report
	 */
	public static void generateReports(String env){
		log.info("begin generateReports, String mode");
		
		generateBuinessSummaryReport(env);
		generateNetworkSummaryReport(env);
		copyCommonDependencyDirectory();
		
		generateSuperPage(); //generate the super index page
	}
	
	/**
	 * copy the dependent CSS/JS/fonts to target folder
	 */
	private static void copyCommonDependencyDirectory(){
		File srcFile = new File(resourcePath + "/" + "htmlTemplates/commonLib");
		File desFile= new File(outputPath);
		
		try {
			FileUtils.copyDirectory(srcFile, desFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * copy the individual dependency folder to target folder
	 * @param des: the destination folder copy to  
	 */
	private static void copyIndividualDependencyToDirectory(String des){
		File srcFile = new File(resourcePath + "/" + "htmlTemplates/individualLib");
		File desFile = new File(des);
		
		try {
			FileUtils.copyDirectory(srcFile, desFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * generate time machine compare report
	 */
	private static void generateTimeMachineCompareReport(String env){
		BenchmarkPersistDao bpd = new BenchmarkPersistImp();
		List<PerfBenchmarkSummary> pbsList = bpd.getRecentSummaryList(3, env, TestCategoryEnum.BUSINESS); //only compare recent 3 records
		
		if(pbsList==null||pbsList.isEmpty()||pbsList.size()<3){
			//TODO if pbsList small than 3, not handle for now
			String templatePath = "htmlTemplates/businessTimeMachineCompare.html";
			HtmlReportTemplate hrt = new HtmlReportTemplate(templatePath);
			hrt.writeToHtmlFile(outputPath, "BusinessTimeMachineCompare", TestCategoryEnum.BUSINESS);
		} else {
			Long summaryIdCurrent = pbsList.get(2).getPerfBenchmarkSummaryID();
			Long summaryIdPrevious = pbsList.get(1).getPerfBenchmarkSummaryID();
			Long summaryIdBeforeLast = pbsList.get(0).getPerfBenchmarkSummaryID();
			List<TimeMachineUnitPojo> current = bpd.getTimeMachineDetailBySummaryId(summaryIdCurrent);
			List<TimeMachineUnitPojo> previous = bpd.getTimeMachineDetailBySummaryId(summaryIdPrevious);
			List<TimeMachineUnitPojo> beforelast = bpd.getTimeMachineDetailBySummaryId(summaryIdBeforeLast);
			TimeMachineCompare tmc = new TimeMachineCompare(current, previous, beforelast);
			JSONObject jsonObj = new JSONObject(tmc);
			
			String templatePath = "htmlTemplates/businessTimeMachineCompare.html";
			HtmlReportTemplate hrt = new HtmlReportTemplate(templatePath);
			hrt.setEnv(env).setJsonContent(jsonObj.toString())
					.setTitle("TimeMachine Compare")
					.setCustomValue("\\{\\%currentDate\\%\\}", TimeUtil.dateToString(pbsList.get(2).getCreateDate(), "MMM dd, yyyy"))
					.setCustomValue("\\{\\%previousDate\\%\\}", TimeUtil.dateToString(pbsList.get(1).getCreateDate(), "MMM dd, yyyy"))
					.setCustomValue("\\{\\%beforeLastDate\\%\\}", TimeUtil.dateToString(pbsList.get(0).getCreateDate(), "MMM dd, yyyy"));
			String reportDirectory = hrt.writeToHtmlFile(outputPath, "BusinessTimeMachineCompare", TestCategoryEnum.BUSINESS);
			
			copyIndividualDependencyToDirectory(reportDirectory);
		}
	}
	
	/**
	 * generate business category report
	 */
	private static void generateBuinessSummaryReport(String env){
		if(SummaryReportFactory.getBusinessSummary().getSingleTestList()==null
				||SummaryReportFactory.getBusinessSummary().getSingleTestList().isEmpty()){
			return;
		}
		/* generate the time machine compare report */
		generateTimeMachineCompareReport(env);
		
		/* in debug mode, set summary information, build number & current score */
		if (SummaryReportFactory.getBusinessSummary().getBuildNumber() != null
				&& !SummaryReportFactory.getBusinessSummary().getBuildNumber().trim().isEmpty()) {
			SummaryReportFactory.getBusinessSummary()
					.setBuildNumber(String.valueOf(SummaryReportFactory.getBusinessSummary().getBuildNumber())); // set builder number in page
		} else { 
			//debug mode don't have build number
			SummaryReportFactory.getBusinessSummary().setBuildNumber("N/A");
		}
		if (SummaryReportFactory.getBusinessSummary().getCurrentScore() == null){
			SummaryReportFactory.getBusinessSummary().calculateSummaryScore();
		}
		
		/* create JSON */
		JSONObject jsonObj = new JSONObject(SummaryReportFactory.getBusinessSummary());
//		JsonUtil ju = new JsonUtil(jsonObj);
//		ju.writeToJsonFile(outputPath, "BusinessIndex", TestCategoryEnum.BUSINESS);
		
		/* create summary diagram JSON */
		SummaryReportDiagrams ggdu = new SummaryReportDiagrams(env, 15);
		Diagram diagram = ggdu.getBusinessDiagramData();
		JSONObject diagramJson = new JSONObject(diagram);
//		JsonUtil diagramOut = new JsonUtil(diagramJson);
//		diagramOut.writeToJsonFile(outputPath, "uiSummaryData", TestCategoryEnum.BUSINESS);
		
		//JSONObject jj = new JSONObject("{\"cols\":[{\"label\":\"Build\",\"type\":\"string\"},{\"label\":\"Adjust Score\",\"type\":\"number\"},{\"label\":\"First Attempt Score\",\"type\":\"number\"},{\"label\":\"Avg Adjust Score\",\"type\":\"number\"}],\"rows\":[{\"c\":[{\"v\":\"#101\"},{\"v\":450},{\"v\":900},{\"v\":380}]},{\"c\":[{\"v\":\"#102\"},{\"v\":288},{\"v\":500},{\"v\":380}]},{\"c\":[{\"v\":\"#103\"},{\"v\":397},{\"v\":600},{\"v\":380}]},{\"c\":[{\"v\":\"#104\"},{\"v\":215},{\"v\":400},{\"v\":380}]},{\"c\":[{\"v\":\"#105\"},{\"v\":366},{\"v\":720},{\"v\":380}]},{\"c\":[{\"v\":\"#106\"},{\"v\":378},{\"v\":750},{\"v\":380}]},{\"c\":[{\"v\":\"#107\"},{\"v\":355},{\"v\":650},{\"v\":380}]},{\"c\":[{\"v\":\"#108\"},{\"v\":299},{\"v\":800},{\"v\":380}]},{\"c\":[{\"v\":\"#109\"},{\"v\":466},{\"v\":700},{\"v\":380}]},{\"c\":[{\"v\":\"#110\"},{\"v\":144},{\"v\":250},{\"v\":380}]}]}");
		//JsonUtil jju = new JsonUtil(jj);
		//jju.writeToJsonFile(outputPath, "uiSummaryDataTest", TestCategoryEnum.BUSINESS);
		
		/* create HTML */
		String templatePath = "htmlTemplates/businessSummaryTemplate.html";
		HtmlReportTemplate hrt = new HtmlReportTemplate(templatePath);
		hrt.setEnv(env).setJsonContent(jsonObj.toString())
				.setSummaryData(diagramJson.toString())
				.setTitle("Business Summary");
		String reportDirectory = hrt.writeToHtmlFile(outputPath, "BusinessIndex", TestCategoryEnum.BUSINESS);
		
		copyIndividualDependencyToDirectory(reportDirectory);
	}
	
	/**
	 * generate network category report
	 */
	private static void generateNetworkSummaryReport(String env){
		if(SummaryReportFactory.getNetworkSummary().getSingleTestList()==null
				||SummaryReportFactory.getNetworkSummary().getSingleTestList().isEmpty()){
			return;
		}
		
		/* create JSON */
		JSONObject jsonObj = new JSONObject(SummaryReportFactory.getNetworkSummary());
//		JsonUtil ju = new JsonUtil(jsonObj);
//		ju.writeToJsonFile(outputPath, "NetworkIndex", TestCategoryEnum.NETWORK);
		
		/* create summary diagram JSON */
		SummaryReportDiagrams ggdu = new SummaryReportDiagrams(env, 15);
		Diagram diagram = ggdu.getNetworkDiagramData();
		JSONObject diagramJson = new JSONObject(diagram);
//		JsonUtil diagramOut = new JsonUtil(diagramJson);
//		diagramOut.writeToJsonFile(outputPath, "networkSummaryData", TestCategoryEnum.NETWORK);
		
		/* create HTML */
		String templatePath = "htmlTemplates/networkSummaryTemplate.html";
		HtmlReportTemplate hrt = new HtmlReportTemplate(templatePath);
		hrt.setEnv(env).setJsonContent(jsonObj.toString())
				.setSummaryData(diagramJson.toString())
				.setTitle("Network Summary");
		hrt.writeToHtmlFile(outputPath, "NetworkIndex", TestCategoryEnum.NETWORK);
		
		//copyIndividualDependencyDirectory(resourcePath + "/" + "htmlTemplates/gstatic", reportDirectory + "/" + "gstatic");
	}
	
	/**
	 * generate super index page for all categories
	 */
	private static void generateSuperPage(){
		
	}
	
}
