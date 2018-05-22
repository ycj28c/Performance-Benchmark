package benchmark.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestfulUtil {
	
	private static final Logger log = LoggerFactory.getLogger(RestfulUtil.class);
	private static RestTemplate restTemplate = new RestTemplate();
	
	public static String convertStrToArtii(String str){
		String url = "http://artii.herokuapp.com/make?text=" + str;
		log.info("URL:\n{}", url);
		
		ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
		HttpStatus statusCode = entity.getStatusCode();
		log.info("Status:\n{}\nBody:\n{}", statusCode, entity.getBody().toString());
		
		return entity.getBody().toString();
	}
	

}
