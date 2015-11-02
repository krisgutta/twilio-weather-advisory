package solutions.gutta.communication.twilio.twiml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Say {
	@JacksonXmlProperty(isAttribute=true)
	private String voice;
	
	@JacksonXmlProperty(isAttribute=true)
	private int loop;
	
	@JacksonXmlProperty(isAttribute=true)
	private String language;

	@JacksonXmlText
	private String message;
	
	public String getVoice() {
		return voice;
	}

	public Say setVoice(String voice) {
		this.voice = voice;
		return this;
	}

	public int getLoop() {
		return loop;
	}

	public Say setLoop(int loop) {
		this.loop = loop;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public Say setLanguage(String language) {
		this.language = language;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Say setMessage(String message) {
		this.message = message;
		return this;
	}
	
	
}