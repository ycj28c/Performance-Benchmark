package benchmark.tools.slack;

import org.springframework.core.env.Environment;

import benchmark.tools.PropertiesUtil;

public class SlackConnection {
	private String uploadAPI;
	private String token;
	private String webhook;
	private String channel;
	private String user;
	private String displayName;
	
	SlackConnection(){}
	
	public SlackConnection(Environment env) {
		this.uploadAPI = PropertiesUtil.getSlackFileUploadAPI();
		this.token = PropertiesUtil.getSlackToken();
		this.webhook = PropertiesUtil.getSlackWebHookURL();
		this.channel = PropertiesUtil.getSlackChannel();
		this.user = PropertiesUtil.getSlackUser();
		this.displayName = PropertiesUtil.getSlackDisplayName();
	}

	public String getHost() {
		return uploadAPI;
	}
	public void setHost(String host) {
		this.uploadAPI = host;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getUploadAPI() {
		return uploadAPI;
	}

	public void setUploadAPI(String uploadAPI) {
		this.uploadAPI = uploadAPI;
	}

	public String getWebhook() {
		return webhook;
	}

	public void setWebhook(String webhook) {
		this.webhook = webhook;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
