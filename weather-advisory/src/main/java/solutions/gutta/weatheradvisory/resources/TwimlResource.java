package solutions.gutta.weatheradvisory.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.params.IntParam;
import solutions.gutta.communication.twilio.twiml.Response;
import solutions.gutta.communication.twilio.twiml.Say;
import solutions.gutta.weatheradvisory.WeatherAdvisoryTwilioResource;
import solutions.gutta.weatheradvisory.api.Phone;
import solutions.gutta.weatheradvisory.api.ZipcodeToPhone;


@Path("/weather-forecast/twiml")
public class TwimlResource {

	@GET
	@Produces(MediaType.TEXT_XML)
	/**
	 * Resource for returning the initial twiml
	 * 
	 * @return
	 */
	public Response start(
			@QueryParam("Called") final String from) {
		final Response resp = new Response();
		String message = WeatherAdvisoryTwilioResource.getMessage(from);
		resp.getList().add(new Say().setLanguage("en-US").setLoop(1).setVoice("alice").setMessage(message));
		
		return resp;
	}
	
	@Path("/status")
	@GET
	@Timed
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Resource for receiving status messages for voice calls.
	 * 
	 * @param answeredBy
	 * @param callStatus
	 * @param callDuration
	 * @param timestamp
	 * @param sequenceNumber
	 * @return
	 */
	public javax.ws.rs.core.Response status(
			@QueryParam("AnsweredBy") final String answeredBy,
			@QueryParam("CallStatus") final String callStatus,
			@QueryParam("CallDuration") final String callDuration,
			@QueryParam("Timestamp") final String timestamp,
			@QueryParam("SequenceNumber") final IntParam sequenceNumber) {
		
		System.out.println(String.format("AnsweredBy:%s, CallStatus:%s, CallDuration:%s, Timestamp:%s, SequenceNumber:%s", answeredBy, callStatus, callDuration, timestamp, sequenceNumber));
		
		return javax.ws.rs.core.Response.ok().build();
	}
	
	@Path("/message")
	@POST
	@Timed
	@Produces(MediaType.TEXT_PLAIN)
	/**
	 * Resource for handling incoming SMS messages
	 * 
	 * @param messageSid
	 * @param smsSid
	 * @param accountSid
	 * @param messagingServiceSid
	 * @param fromNumber
	 * @param toNumber
	 * @param body
	 * @param fromCity
	 * @param fromZip
	 * @return
	 */
	public String message(
			@FormParam("MessageSid") final String messageSid,
			@FormParam("SmsSid") final String smsSid,
			@FormParam("AccountSid") final String accountSid,
			@FormParam("MessagingServiceSid") final String messagingServiceSid,
			@FormParam("From") final String fromNumber,
			@FormParam("To") final String toNumber,
			@FormParam("Body") final String body,
			@FormParam("FromCity") final String fromCity,
			@FormParam("FromZip") @DefaultValue("94107") final String fromZip
			) {
		
		String zipcode = "94107";
		
		if (fromZip != null && !fromZip.isEmpty()) {
			zipcode = fromZip;
		}
		
		final Map<String, String> map = new HashMap<String, String>();
		
		map.put("MessageSid", messageSid);
		map.put("From", fromNumber);
		map.put("To", toNumber);
		map.put("FromZip", zipcode);
		map.put("Body", body);
		
		System.out.println(map);

		//Let's validate to make sure the body has correct sms messages
		if (!("voice").equalsIgnoreCase(body) && 
			!("voice,sms").equalsIgnoreCase(body) &&
			!("sms,voice").equalsIgnoreCase(body) &&
			!("sms").equalsIgnoreCase(body) &&
			!("remove").equalsIgnoreCase(body)) {
			
			//usage(fromNumber, toNumber);
			
			return "SMS either 'voice', 'sms', 'voice,sms' or 'remove' keywords.";
		}
		
		//If unsubscribe, remove the number from mapping
		if (("remove").equalsIgnoreCase(body)) {
			final Phone phone = ZipcodeToPhone.ref.remove(zipcode, fromNumber);
			
			if (phone == null) {
				return String.format("No subscription found for Phone Number %s", fromNumber);
			} else {
				return String.format("Phone Number %s is unsubscribed from receiving weather forecast", phone);
			}
		}
		
		Phone phone = ZipcodeToPhone.ref.get(zipcode, fromNumber);
		
		if (phone != null) {
			final String cSub = phone.getSubscription();
			
			if (body.equalsIgnoreCase(cSub)) {
				return String.format("Phone %s is already subscribed to %s", fromNumber, body);
			}
						
			phone.subscribe(body);
			
			return String.format("Phone %s subscription changed from %s to %s", fromNumber, cSub, phone.getSubscription());
		}
		
		phone = new Phone();
		phone.setNumber(fromNumber)
			.setFrom(toNumber)
			.setCity(fromCity)
			.setZipcode(zipcode)
			.subscribe(body);
		
		ZipcodeToPhone.ref.add(zipcode, phone);

		return String.format("Phone number %s is subscribed to receive weather forecast", phone.getNumber());
	}	
}
