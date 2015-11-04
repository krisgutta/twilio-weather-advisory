package solutions.gutta.communication.twilio.taskrouter;

import java.util.Map;

public class WorkerAttributes {
	private Map<String, String> temp;

	public Map<String, String> getTemp() {
		return temp;
	}

	public void setTemp(Map<String, String> temp) {
		this.temp = temp;
	}
	
	@Override
	public String toString() {
		return temp.toString();
	}
	
}
