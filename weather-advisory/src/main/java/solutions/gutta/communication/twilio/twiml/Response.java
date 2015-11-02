package solutions.gutta.communication.twilio.twiml;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Response {
	@JacksonXmlElementWrapper(useWrapping=false)
	@JacksonXmlProperty(localName="Say")
	List<Object> list;
	
	public Response() {
		list = new ArrayList<Object>();
	}

	public List<Object> getList() {
		return list;
	}

	public Response setList(List<Object> response) {
		this.list = response;
		return this;
	}

}
