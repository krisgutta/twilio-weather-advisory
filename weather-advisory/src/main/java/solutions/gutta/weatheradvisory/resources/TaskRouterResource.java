package solutions.gutta.weatheradvisory.resources;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import solutions.gutta.communication.twilio.taskrouter.AcceptInstruction;
import solutions.gutta.communication.twilio.taskrouter.Instruction;
import solutions.gutta.weatheradvisory.BroadCastWeatherTask;

/**
 * 
 * @author kgutta
 *
 * Resource Reponsible for receiving callbacks from TaskRouter
 */
@Path("/weather-forecast/taskrouter/callback")
@Produces(MediaType.APPLICATION_JSON)
public class TaskRouterResource {
	
	@GET
	public Instruction getInstructions(
			@QueryParam("TaskSid") final String taskSid,
			@QueryParam("WorkerSid") final String workerSid) {
		
		System.out.println(String.format("Task %s is reserved with worker %s", taskSid, workerSid));
		
		return new AcceptInstruction();
	}
	
	@POST
	/**
	 * Resource for handling the assignment callback request from TaskRouter 
	 * 
	 * @param taskSid
	 * @param workerSid
	 * @param workspaceSid
	 * @param workflowSid
	 * @param taskQueueSid
	 * @param workerAttributes
	 * @param taskAttributes
	 * @param taskAge
	 * @param taskPriority
	 * @param reservationSid
	 * @return
	 */
	public Instruction postInstructions(
			@FormParam("TaskSid") final String taskSid,
			@FormParam("WorkerSid") final String workerSid,
			@FormParam("WorkspaceSid") final String workspaceSid,
			@FormParam("WorkflowSid") final String workflowSid,
			@FormParam("TaskQueueSid") final String taskQueueSid,
			@FormParam("WorkerAttributes") final String workerAttributes,
			@FormParam("TaskAttributes") final String taskAttributes,
			@FormParam("TaskAge") final LongParam taskAge,
			@FormParam("TaskPriority") final IntParam taskPriority,
			@FormParam("ReservationSid") final String reservationSid) {
		
		System.out.println(String.format("TaskSid=%s, Workersid=%s, WorkspaceSid=%s, WorkflowSid=%s, TaskQueueSid=%s, WorkerAttributes=%s, TaskAttributes=%s, TaskAge=%s, TaskPriority=%s, ReservationSid=%s", taskSid, workerSid, workspaceSid, workflowSid, taskQueueSid, workerAttributes, taskAttributes, taskAge, taskPriority, reservationSid));
		
		final Thread t = new Thread(new BroadCastWeatherTask(taskAttributes, workerAttributes));
		t.start();
		
//		final Thread t = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				Calls call = new Calls();
//				call.setToNumber("+14082307171");
//				call.setFromNumber("+14152148136");
//				call.setApplicationUrl(urlPrefix + "/weather-forecast/twiml");
//				call.setMethod("GET");
//				call.setStatusCallbackUrl(urlPrefix + "/weather-forecast/twiml/status");
//				call.setStatusCallbackMethod("GET");
//				
//				try {
//					call.launch(WeatherAdvisoryTwilioResource.createTwilioRestClient());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//		});
//		
//		t.start();
		
		return new AcceptInstruction();
	}

}
