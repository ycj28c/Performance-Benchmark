package benchmark.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmark.enumx.TestCategoryEnum;

public class HtmlReportTemplate {
	private String htmlSource;
	private String JSON_KEY = "\\{\\%jsonKey\\%\\}"; //{%jsonKey%}
	private String JSON_CONTENT = "\\{\\%jsonContent\\%\\}"; //{%jsonContent%}
	private String JSON_TIMEMACHINE = "\\{\\%jsonTimeMachine\\%\\}"; //{%jsonTimeMachine%}
	private String JSON_SUMMARY = "\\{\\%jsonSummary\\%\\}"; //{%jsonSummary%}
	private String TITLE_KEY = "\\{\\%titleKey\\%\\}"; //{%titleKey%}
	private String ENV_KEY = "\\{\\%environmentKey\\%\\}"; //{%environmentKey%}
	private String IP_ADDRESS = "\\{\\%ipAddress\\%\\}"; //{%ipAddress%}
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public HtmlReportTemplate(String path){
		this.htmlSource =  getFile(path);
	}
	
	/**
	 * replace the ip address value of the html template {%ipAddress%}
	 * @param name
	 * @return
	 */
	public HtmlReportTemplate setIPAddress(String name){
		replaceValue(IP_ADDRESS, name);
		return this;
	}
	
	/**
	 * replace the json value of the html template {%jsonKey%}
	 * @param name
	 * @return
	 */
	public HtmlReportTemplate setJsonKey(String name){
		replaceValue(JSON_KEY, name);
		return this;
	}
	
	/**
	 * replace the json value of the html template {%jsonContent%}
	 * @param name
	 * @return
	 */
	public HtmlReportTemplate setJsonContent(String name){
		replaceValue(JSON_CONTENT, name);
		return this;
	}
	
	/**
	 * replace the json value of the html template {%jsonTimeMachine%}
	 * @param name
	 * @return
	 */
	public HtmlReportTemplate setJsonTimeMachine(String name){
		replaceValue(JSON_TIMEMACHINE, name);
		return this;
	}
	
	/**
	 * replace the json value of the html template {%jsonSummary%}
	 * @param name
	 * @return
	 */
	public HtmlReportTemplate setSummaryData(String name){
		replaceValue(JSON_SUMMARY, name);
		return this;
	}
	
	/**
	 * replace the title value of the html template {%titleKey%}
	 * @param name
	 * @return
	 */
	public HtmlReportTemplate setTitle(String name){
		replaceValue(TITLE_KEY, name);
		return this;
	}
	
	/**
	 * replace the env value of the html template {%environmentKey%}
	 * @param name
	 * @return
	 */
	public HtmlReportTemplate setEnv(String name){
		replaceValue(ENV_KEY, name);
		return this;
	}
	
	/**
	 * replace the custom flag and value
	 * @param value
	 * @return
	 */
	public HtmlReportTemplate setCustomValue(String key, String value){
		replaceValue(key, value);
		return this;
	}
	
	private void replaceValue(String source, String replacement){
		htmlSource = htmlSource.replaceAll(source, replacement);
	}
	
	/**
	 * get the file from resource folder and convert to string
	 * @param fileName
	 * @return
	 */
	private String getFile(String fileName) {
		File file = new File("src/test/resources/"+fileName); //this seems works
		try {
			String contents = FileUtils.readFileToString(file, "UTF-8");
			return contents;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	  }
	
	/**
	 * write the HTML source code into the HTML file
	 * @param outputPath
	 * @param htmlSource
	 * @param fileName
	 */
	public String writeToHtmlFile(String outputPath, String fileName, TestCategoryEnum category){
		log.debug("Begin writeToHtmlFile");
		/* print test */
		
		outputPath = outputPath + "/" + category.getValue();
		if(outputPath == null||outputPath.trim().equals("")){
			log.error(" writeToHtmlFile: target path should not be empty");
		}
		File fp = new File(outputPath);  
		if (!fp.exists() &&!fp.isDirectory())  //if folder not existed, create one
		{       
			log.info(" folder {} doesn't exist, create new one", outputPath);
			fp.mkdirs();    
		}
		
		//String reportPath = outputPath + "/" + "multiOutput.json";
		String reportPath = outputPath + "/" + fileName + ".html";
		log.info(" report path is {}", reportPath);
		
	    PrintStream printStream = null;
		try {
			printStream = new PrintStream(new FileOutputStream(reportPath));
		} catch (FileNotFoundException e) {
			log.error(" The report output path '{}' is not available", reportPath);
		}
		printStream.println(htmlSource); 
		return outputPath;
	}
}
