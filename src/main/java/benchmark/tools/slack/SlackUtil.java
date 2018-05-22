package benchmark.tools.slack;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmark.tools.RestfulUtil;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackMessage;

public class SlackUtil {
	private static final Logger log = LoggerFactory.getLogger(SlackUtil.class);
	private int SLACK_CHANNEL_MODE = 1;
	private int SLACK_USER_MODE = 2;
	
	private String generateArtiiMessage(ScoreBoardPojo sb){
		/* get art score */
		String artiiScore = null;
		if(sb == null || sb.getCurrentScore() == null || sb.getLastScore().isEmpty()){
			return null;
		} else {
			artiiScore = RestfulUtil.convertStrToArtii(sb.getCurrentScore() + " / 100");
		}
		return artiiScore;
	}
	
	/**
	 * send message to slack template
	 * @throws IOException 
	 */
	private void sendResultToTemplate(ScoreBoardPojo sb, SlackConnection slackCon, int mode) throws IOException{
		if(mode<1||mode>2){
			log.error("Error, mode is {}! The slack sending mode must be 'SLACK_CHANNEL_MODE' 1 or 2 'SLACK_USER_MODE'.", mode);
			return;
		}
		String artiiScore = generateArtiiMessage(sb);
		
		String webhookUrl = slackCon.getWebhook();
		Slack slack = new Slack(webhookUrl);
		
		slack.icon(":congratulations:").displayName(slackCon.getDisplayName());
		if(mode == SLACK_CHANNEL_MODE){
			slack.sendToChannel(slackCon.getChannel());
		}
		if(mode == SLACK_USER_MODE){
			slack.sendToUser(slackCon.getUser());
		}
		// send text use webhook 
		if ((artiiScore != null && !artiiScore.trim().isEmpty())
				&& (sb.getCurrentScore() != null && !sb.getCurrentScore().trim().isEmpty())) {
			if(sb.getLastScore() !=null && !sb.getLastScore().trim().isEmpty()){
				slack.push(new SlackMessage(" *UI Benchmark Score Is:* \n")
						.preformatted(artiiScore)
						.text(" _The score changes from last build score_ *"+ sb.getLastScore() +"* _to current score_ *"+ sb.getCurrentScore()+"* _!_ \n")
						.text(" _Can't wait to see the details? the report link is coming soon!_ \n"));
			} else {
				slack.push(new SlackMessage(" *UI Benchmark Score Is:* \n")
						.preformatted(artiiScore)
						.text(" _Can't wait to see the details? the report link is coming soon!_ \n"));
			}
		} else { //if no test step, don't print steps
			slack.push(new SlackMessage(" _Error:_ \n").preformatted("Didn't find score in your benchmark"));
		}
	}
	
	/**
	 * send test fail result detail to slack channel
	 * @throws IOException 
	 */
	public void sendResultToChannel(ScoreBoardPojo sb, SlackConnection slackCon) throws IOException {
		sendResultToTemplate(sb, slackCon, SLACK_CHANNEL_MODE);
	}

	/**
	 * send test fail result detail to slack user
	 * @throws IOException 
	 */
	public void sendResultToUser(ScoreBoardPojo sb, SlackConnection slackCon) throws IOException {
		sendResultToTemplate(sb, slackCon, SLACK_USER_MODE);		
	}

}
