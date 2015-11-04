package solutions.gutta.weatheradvisory;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import solutions.gutta.communication.twilio.Calls;
import solutions.gutta.communication.twilio.Messages;
import solutions.gutta.weatheradvisory.api.Phone;
import solutions.gutta.weatheradvisory.api.ZipcodeToPhone;

public class BroadCastWeatherTask implements Runnable {
	String taskAttributes;
	
	String workerAttributes;
	
	final JsonParser parser = new JsonParser();
	
	public BroadCastWeatherTask(String taskAttributes, String workerAttributes) {
		this.taskAttributes = taskAttributes;
		this.workerAttributes = workerAttributes;
	}
	
	@Override
	public void run() {
		final String zipcode = parse(taskAttributes, "zipcode");
		
		//Get list of phone numbers for the zipcode in question
		List<Phone> phones = ZipcodeToPhone.ref.get(zipcode);
		
		if (phones.size() == 0) {
			System.out.println("No phone numbers found for zip code: " + zipcode);
			return;
		}
		
		final String voice = parse(workerAttributes, "voice");
		final String sms = parse(workerAttributes, "sms");
		
		//Let's loop through the phone numbers and launch calls
		for (int i=0; i<phones.size(); i++) {
			if (phones.get(i).canSendVoice()) {
				//let's save the message to play
				WeatherAdvisoryTwilioResource.addMessage(phones.get(i).getNumber(), voice);
				
				System.out.println(String.format("Dialing %s from %s", phones.get(i).getNumber(), phones.get(i).getFrom()));
				call(phones.get(i).getFrom(), phones.get(i).getNumber());
			}
			
			if (phones.get(i).canSendSMS()) {
				System.out.println(String.format("Texting %s from %s with message %s", phones.get(i).getNumber(), phones.get(i).getFrom(), sms));
				sms(phones.get(i).getFrom(), phones.get(i).getNumber(), sms);
			}
		}
	}

	void call(String from, String to) {
		Calls call = new Calls();
		call.setFromNumber(from).setToNumber(to);
		call.setApplicationUrl(WeatherAdvisoryTwilioResource.getUrl() + "/weather-forecast/twiml").setMethod("GET");
		call.setStatusCallbackUrl(WeatherAdvisoryTwilioResource.getUrl() + "/weather-forecast/twiml/status").setStatusCallbackMethod("GET");
		String sid = call.launch(WeatherAdvisoryTwilioResource.createTwilioRestClient());
		
		System.out.println("callSid = " + sid);
	}
	
	void sms(String from, String to, String message) {
		Messages messages = new Messages();
		String sid = messages.setFromNumber(from).setToNumber(to).setBody(message).send(WeatherAdvisoryTwilioResource.createTwilioRestClient());
		
		System.out.println("smsSid = " + sid);
	}
	
	String parse(String json, String key) {
		JsonElement element = parser.parse(json);
		
		return element.getAsJsonObject().get(key).getAsString();
	}
}
