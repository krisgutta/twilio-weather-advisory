package solutions.gutta.weatheradvisory;

import java.util.HashMap;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioTaskRouterClient;

public class WeatherAdvisoryTwilioResource {
	private static String aSid;
	private static String aToken;
	private static String url;
	
	private static HashMap<String, String> voiceMessage = new HashMap<String, String>();
	
	public static void init(String accountSid, String authToken, String urlPrefix) {
		aSid = accountSid;
		aToken = authToken;
		url = urlPrefix;
	}
	
	public static TwilioRestClient createTwilioRestClient() {
		return new TwilioRestClient(aSid, aToken);
	}
	
	public static TwilioTaskRouterClient createTwilioTaskRouterClient() {
		return new TwilioTaskRouterClient(aSid, aToken);
	}
	
	public static String getUrl() {
		return url;
	}
	
	public static synchronized void addMessage(String phoneNumber, String message) {
		voiceMessage.put(phoneNumber, message);
	}
	
	public static synchronized String getMessage(String phoneNumber) {
		return voiceMessage.remove(phoneNumber);
	}
}
