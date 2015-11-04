package solutions.gutta.communication.twilio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Call;

public class Calls {	
	private List<NameValuePair> params = null;
	
	public Calls() {
		params = new ArrayList<NameValuePair>();
	}

	public Calls setToNumber(String toNumber) {
		params.add(new BasicNameValuePair("To", toNumber));
		return this;
	}

	public Calls setFromNumber(String fromNumber) {
		params.add(new BasicNameValuePair("From", fromNumber));
		return this;
	}

	public Calls setApplicationUrl(String applicationUrl) {
		params.add(new BasicNameValuePair("Url", applicationUrl));
		return this;
	}

	public Calls setMethod(String method) {
		params.add(new BasicNameValuePair("Method", "GET".equalsIgnoreCase(method) ? method : "POST"));
		return this;
	}

	public Calls setStatusCallbackUrl(String statusCallbackUrl) {
		if (statusCallbackUrl != null && !statusCallbackUrl.isEmpty())
			params.add(new BasicNameValuePair("StatusCallback", statusCallbackUrl));
		return this;
	}

	public Calls setStatusCallbackMethod(String statusCallbackMethod) {
		params.add(new BasicNameValuePair("StatusCallbackMethod", "GET".equalsIgnoreCase(statusCallbackMethod) ? statusCallbackMethod : "POST"));
		return this;
	}

	public Calls setIfMachine(String ifMachine) {
		if ("Continue".equals(ifMachine) || "Hangup".equals(ifMachine))			
			params.add(new BasicNameValuePair("IfMachine", ifMachine));
		return this;
	}

	public Calls setRecord(boolean record) {
		params.add(new BasicNameValuePair("Record", record ? "true" : "false"));
		return this;
	}	
	
	public String launch(final TwilioRestClient client) {
		try {
			final Call call = client.getAccount().getCallFactory().create(params);
			
			if (call != null) {
				return call.getSid();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
