package solutions.gutta.weatheradvisory.api;

public class Phone {
	public final static int VOICE = 0;
	
	public final static int SMS = 1;
	
	public final static int VOICE_AND_SMS = 2;
	
	//Caller Id to use calling or texting
	private String from;
	
	//phone number
	private String number;
	
	//city of phone
	private String city;
	
	//zipcode associated with the phone
	private String zipcode;
	
	//phone number subscription type
	private int subscription = VOICE;
	
	public String getFrom() {
		return from;
	}

	public Phone setFrom(String from) {
		this.from = from;
		return this;
	}

	public String getNumber() {
		return number;
	}

	public Phone setNumber(String number) {
		this.number = number;
		return this;
	}

	public String getCity() {
		return city;
	}

	public Phone setCity(String city) {
		this.city = city;
		return this;
	}

	public String getZipcode() {
		return zipcode;
	}

	public Phone setZipcode(String zipcode) {
		this.zipcode = zipcode;
		return this;
	}

	public String getSubscription() {
		if (subscription == VOICE_AND_SMS) {
			return "voice,sms";
		} else if (subscription == SMS) {
			return "sms";
		} else {
			return "voice";
		}
	}

	public void subscribe(String message) {
		if ("voice,sms".equalsIgnoreCase(message)) {
			this.subscription = VOICE_AND_SMS;
		} else if (("sms").equalsIgnoreCase(message)) {
			this.subscription = SMS;
		} else {
			this.subscription = VOICE;
		}
 	}

	public boolean canSendVoice() {
		return subscription == VOICE || subscription == VOICE_AND_SMS;
	}
	
	public boolean canSendSMS() {
		return subscription == SMS || subscription == VOICE_AND_SMS;
	}
	
	@Override
	public String toString() {
		return String.format("number=%s,zipcode=%s", number, zipcode);
	}
}
