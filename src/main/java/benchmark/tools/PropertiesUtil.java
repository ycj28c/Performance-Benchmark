package benchmark.tools;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {
	
	public static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
	
	/** 
	 * read the configuration file, get the value
	 * @param key
	 * @return the value of the key
	 */
	public static String getPropertyConfig(String key) {
		String envName = "env-" + System.getProperty("env") + ".properties";
        Resource resource = new ClassPathResource(envName);
        Properties props = null;
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
        	log.error("getPropertyConfig {} error:", envName, e);
        }
        String propertyValue = null;
        try{
        	propertyValue = props.getProperty(key);
        } catch(Exception ex){
        	log.error("Can't get property {}:", key, ex);
        }
        return propertyValue;
    }
	
	/**
	 * get the slack file upload api url
	 * @return
	 */
	public static String getSlackFileUploadAPI() {
		String slackFuAPI = PropertiesUtil.getPropertyConfig("slack.files.upload.api");
		if(slackFuAPI == null||slackFuAPI.trim().equals("")){
			log.error("getSlackFileUploadAPI error: slack.files.upload.api didn't config in properties");
			return null;
		}
		return slackFuAPI.trim();
	}
	
	/**
	 * get the slack token
	 * @return
	 */
	public static String getSlackToken() {
		String slackToken = PropertiesUtil.getPropertyConfig("slack.token");
		if(slackToken == null||slackToken.trim().equals("")){
			log.error("getSlackToken error: slack.token didn't config in properties");
			return null;
		}
		return slackToken.trim();
	}
	
	/**
	 * get the webhook url
	 * @return
	 */
	public static String getSlackWebHookURL() {
		String slackWebHook = PropertiesUtil.getPropertyConfig("slack.webhook.url");
		if(slackWebHook == null||slackWebHook.trim().equals("")){
			log.error("getSlackWebHookURL error: slack.webhook.url didn't config in properties");
			return null;
		}
		return slackWebHook.trim();
	}
	
	/**
	 * get the slack channel send message to
	 * @return
	 */
	public static String getSlackChannel() {
		String slackChannel = PropertiesUtil.getPropertyConfig("slack.send.channel");
		if(slackChannel == null||slackChannel.trim().equals("")){
			log.error("getSlackChannel error: slack.send.channel didn't config in properties");
			return null;
		}
		return slackChannel.trim();
	}
	
	/**
	 * get the slack user send message to
	 * @return
	 */
	public static String getSlackUser() {
		String slackUser = PropertiesUtil.getPropertyConfig("slack.send.user");
		if(slackUser == null||slackUser.trim().equals("")){
			log.error("getSlackUser error: slack.send.user didn't config in properties");
			return null;
		}
		return slackUser.trim();
	}
	
	/**
	 * get the slack bot display name
	 * @return
	 */
	public static String getSlackDisplayName() {
		String slackDisplayName= PropertiesUtil.getPropertyConfig("slack.display.name");
		if(slackDisplayName == null||slackDisplayName.trim().equals("")){
			log.error("getSlackDisplayName error: slack.display.name didn't config in properties");
			return null;
		}
		return slackDisplayName.trim();
	}
	
}
