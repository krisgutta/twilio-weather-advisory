package solutions.gutta.weatheradvisory.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import solutions.gutta.weatheradvisory.api.Phone;
import solutions.gutta.weatheradvisory.api.ZipcodeToPhone;

@Path("/weather-forecast/zipcodes")
@Produces(MediaType.APPLICATION_JSON)
public class ZipcodeMappingResource {

	@GET
	@Timed
	public Map<String, ArrayList<Phone>> get() {
		return ZipcodeToPhone.ref.get();
	}

	@GET
	@Timed
	@Path("/{zipcode:[0-9]+}")
	public List<Phone> get(@PathParam("zipcode") final String zipcode) {
		return ZipcodeToPhone.ref.get(zipcode);
	}
	
}
