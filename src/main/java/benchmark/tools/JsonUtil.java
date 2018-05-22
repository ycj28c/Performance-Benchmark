package benchmark.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmark.enumx.TestCategoryEnum;

/* because after jenkins update to v2.6, doesn't support local AJAX 
 * request for security reason, not need this class now 
 */
public class JsonUtil {
	private JSONObject jsonObj;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public JsonUtil(JSONObject jsonObj){
		this.jsonObj = jsonObj;
	}
	/**
	 * write the json object to file in benchmark report folder
	 * @param jsonObj
	 * @param fileName
	 */
	public void writeToJsonFile(String outputPath, String fileName, TestCategoryEnum category){
		log.debug("Begin writeToJsonFile");
		/* print test */
		
		outputPath = outputPath + "/" + category.getValue();
		if(outputPath == null||outputPath.trim().equals("")){
			log.error(" writeToJsonFile: target path should not be empty");
		}
		File fp = new File(outputPath);  
		if (!fp.exists() &&!fp.isDirectory())  //if folder not existed, create one
		{       
			log.info(" folder {} doesn't exist, create new one", outputPath);
			fp.mkdirs();    
		}
		
		String reportPath = outputPath + "/" + fileName + ".json";
		log.info(" report path is {}", reportPath);
		
	    PrintStream printStream = null;
		try {
			printStream = new PrintStream(new FileOutputStream(reportPath));
		} catch (FileNotFoundException e) {
			log.error(" The report output path '{}' is not available", reportPath);
		}
		printStream.println(jsonObj.toString()); 
		
//		try {
//			//FileWriter file = new FileWriter("C:\\Users/ryang/Desktop/html local test/multiOutput.json");
//			FileWriter file = new FileWriter(outputPath + "/multiOutput.json");
//			file.write(jsonObj.toString());
//			file.flush();
//			file.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
