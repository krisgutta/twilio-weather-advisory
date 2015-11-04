package solutions.gutta.weatheradvisory;

import java.util.concurrent.TimeUnit;

import com.twilio.sdk.TwilioTaskRouterClient;
import com.twilio.sdk.resource.instance.taskrouter.Activity;
import com.twilio.sdk.resource.instance.taskrouter.Worker;
import com.twilio.sdk.resource.instance.taskrouter.Workspace;
import com.twilio.sdk.resource.list.taskrouter.ActivityList;
import com.twilio.sdk.resource.list.taskrouter.WorkerList;
import com.twilio.sdk.resource.list.taskrouter.WorkspaceList;

public class WorkerAvailabilityUpdater extends Thread {
	private long sleepTime = TimeUnit.SECONDS.toMillis(60);
	
	private final Object waitObj = new Object();
	
	private boolean shutdown = false;
	
	String workspaceSid;
	
	String idleActivitySid;
	
	TwilioTaskRouterClient client; 
	
	public WorkerAvailabilityUpdater(int sleepTime) {
		setName("WorkerAvailabilityUpdater");
		
		this.sleepTime = TimeUnit.SECONDS.toMillis(sleepTime);
		
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
		
		//Let's get the activitySid for Idle activity
		final ActivityList activities = client.getActivities(workspaceSid);
		
		for (Activity activity : activities) {
			if ("Idle".equals(activity.getFriendlyName())) {
				idleActivitySid = activity.getSid();
				break;
			}
		}
		
		System.out.println("Idle Activity sid = " + idleActivitySid);
	}
	
	@Override
	public void run() {
		System.out.println("WorkerAvailabilityUpdater Thread starting the loop");
		
		while (!shutdown) {
			pause();
			
			System.out.println("Getting list of workers");
			//Getting list of workers
			WorkerList workers = client.getWorkers(workspaceSid);
			
			for (Worker worker : workers) {
				if (!worker.isAvailable()) {
					System.out.println("Updating activity for worker " + worker.getFriendlyName());
					try {
						worker.updateActivity(idleActivitySid);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		System.out.println("WorkerAvailabilityUpdater Thread loop terminated");
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
