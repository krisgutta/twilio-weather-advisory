package solutions.gutta.weatheradvisory.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.params.IntParam;
import solutions.gutta.communication.twilio.twiml.Response;
import solutions.gutta.communication.twilio.twiml.Say;


@Path("/weather-forecast/twiml")
public class TwimlResource {

	@GET
	@Produces(MediaType.TEXT_XML)
	public Response start() {
		final Response resp = new Response();
		final String message = "Hello, the current projected weather in your area is 66 degrees";
		resp.getList().add(new Say().setLanguage("en-US").setLoop(1).setVoice("alice").setMessage(message));
		
		return resp;
	}
	
	@Path("/status")
	@GET
	@Timed
	@Produces(MediaType.APPLICATION_JSON)
	public javax.ws.rs.core.Response status(
			@QueryParam("AnsweredBy") final String answeredBy,
			@QueryParam("CallStatus") final String callStatus,
			@QueryParam("CallDuration") final String callDuration,
			@QueryParam("Timestamp") final String timestamp,
			@QueryParam("SequenceNumber") final IntParam sequenceNumber) {
		
		System.out.println(String.format("AnsweredBy:%s, CallStatus:%s, CallDuration:%s, Timestamp:%s, SequenceNumber:%s", answeredBy, callStatus, callDuration, timestamp, sequenceNumber));
		
		return javax.ws.rs.core.Response.ok().build();
	}
}
