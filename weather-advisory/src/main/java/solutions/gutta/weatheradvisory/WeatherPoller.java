package solutions.gutta.weatheradvisory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.twilio.sdk.TwilioTaskRouterClient;
import com.twilio.sdk.resource.instance.taskrouter.Task;
import com.twilio.sdk.resource.instance.taskrouter.Workflow;
import com.twilio.sdk.resource.instance.taskrouter.Workspace;
import com.twilio.sdk.resource.list.taskrouter.WorkflowList;
import com.twilio.sdk.resource.list.taskrouter.WorkspaceList;

import solutions.gutta.weatheradvisory.api.ZipcodeToPhone;

public class WeatherPoller extends Thread {
	private static final WeatherPoller instance = new WeatherPoller();
	
	private long sleepTime = TimeUnit.SECONDS.toMillis(60);
	
	private final Object waitObj = new Object();
	
	private boolean shutdown = false;

	String workspaceSid;
	
	String workflowSid;
	
	TwilioTaskRouterClient client; 

	private HashMap<String, Long> zipdialtime = null;
	
	private int min = 80;
	private int max = 110;
	
	private WeatherPoller() {
		setName("WeatherPoller");
		
		zipdialtime = new HashMap<String, Long>();
		
		client = WeatherAdvisoryTwilioResource.createTwilioTaskRouterClient();
		
		//Let's get the workspace sid
		final WorkspaceList list = client.getWorkspaces();
		
		for (Workspace workspace : list) {
			if ("WeatherAlert".equals(workspace.getFriendlyName())) {
				workspaceSid = workspace.getSid();
				break;
			}
		}
		
		System.out.println("Workspace sid = " + workspaceSid);

		final WorkflowList workflows = client.getWorkflows(workspaceSid);
		
		for (Workflow workflow : workflows) {
			if (("WeatherFlow").equals(workflow.getFriendlyName())) {
				workflowSid = workflow.getSid();
				break;
			}
		}
		
		System.out.println("Workflow sid = " + workflowSid);
	}
	
	public static WeatherPoller getInstance() {
		return instance;
	}
	
	@Override
	public void run() {
		System.out.println("WeatherPoller loop started");
		
		while (!shutdown) {
			pause();
			
			final long current = System.currentTimeMillis();
			
			//Get list of zip codes
			String[] zipcodes = ZipcodeToPhone.ref.getZipcodes();
			
			for (String zipcode : zipcodes) {
				//if there are no phone numbers for the zip code, let's skip
				if (ZipcodeToPhone.ref.size(zipcode) <= 0) {
					continue;
				}
				
				//If it's been too soon since we last dialed zipcode, let's skip
				if (zipdialtime.containsKey(zipcode) && current - zipdialtime.get(zipcode).longValue() < TimeUnit.HOURS.toMillis(1)) {
					continue;
				}
				
				//Let's get the weather for the zipcode
				int projected = getWeather(zipcode);
				
				System.out.println(String.format("Projected temperature for zipcode %s is %s", zipcodes, projected));
				
				//Create a task
				createTask(zipcode, projected);
			}
		}
		
		System.out.println("WeatherPoller loop terminated");
	}
	
	/**
	 * Returns current weather for zipcode
	 * 
	 * @param zipcode
	 * @return
	 */
	int getWeather(String zipcode) {
		//for now let's just do random values
		return java.util.concurrent.ThreadLocalRandom.current().nextInt(min, max);
	}
	
	
	void createTask(String zipcode, int projected) {
		String attributes = String.format("{\"zipcode\":%s,\"projected\":%s}", zipcode, projected);
		
		Map<String, String> params = new HashMap<String, String>();
	    params.put("WorkflowSid", workflowSid);
	    params.put("Attributes", attributes);
		
	    try {
	    	Task task = client.createTask(workspaceSid, params);
	    	
	    	zipdialtime.put(zipcode, Long.valueOf(System.currentTimeMillis()));
	    	
	    	System.out.println("TaskSid:" + task.getSid());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public void high() {
		min = 80;
		max = 110;
		
		reset();
	}
	
	public void low() {
		min = -20;
		max = 32;
		
		reset();
	}
	
	/**
	 * Resets the map and wakes up the thread so that we can dial again
	 * 
	 */
	public void reset() {
		synchronized(zipdialtime) {
			zipdialtime.clear();
		}
		
		synchronized(waitObj) {
			waitObj.notifyAll();
		}
	}
	
	void pause() {
		try {
			synchronized (waitObj) {
				waitObj.wait(sleepTime);
			}
		} catch (Exception e) {
		}
	}
	
	void shutdown() {
		shutdown = true;
		
		synchronized(waitObj) {
			waitObj.notifyAll();
		}
	}
}
