package solutions.gutta.communication.twilio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Message;

public class Messages {
	private List<NameValuePair> params = null;
	
	public Messages() {
		params = new ArrayList<NameValuePair>();
	}
	
	public Messages setToNumber(String toNumber) {
		params.add(new BasicNameValuePair("To", toNumber));
		return this;
	}

	public Messages setFromNumber(String fromNumber) {
		params.add(new BasicNameValuePair("From", fromNumber));
		return this;
	}

	public Messages setBody(String applicationUrl) {
		params.add(new BasicNameValuePair("Body", applicationUrl));
		return this;
	}
	
	public String send(final TwilioRestClient client) {	
		try {
			final Message message = client.getAccount().getMessageFactory().create(params);
			
			if (message != null) {
				return message.getSid();
			}
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
		return null;
	}
}
