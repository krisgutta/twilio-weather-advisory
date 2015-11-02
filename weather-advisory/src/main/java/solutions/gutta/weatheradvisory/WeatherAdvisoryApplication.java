package solutions.gutta.weatheradvisory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.yunspace.dropwizard.xml.XmlBundle;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import solutions.gutta.weatheradvisory.health.WeatherHealthCheck;
import solutions.gutta.weatheradvisory.resources.TaskRouterResource;
import solutions.gutta.weatheradvisory.resources.TwimlResource;

public class WeatherAdvisoryApplication extends Application<WeatherAdvisoryConfiguration>{

	public static void main(String[] args) throws Exception {
		 new WeatherAdvisoryApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "weather-advisory";
	}

	@Override
	public void initialize(Bootstrap<WeatherAdvisoryConfiguration> bootstrap) {
		final XmlBundle xmlBundle = new XmlBundle();
        xmlBundle.getXmlMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(Include.NON_NULL);
		
        xmlBundle.getXmlMapper().enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
        
        bootstrap.addBundle(xmlBundle);
	}
	
	@Override
	public void run(WeatherAdvisoryConfiguration configuration, Environment environment) throws Exception {
		final TaskRouterResource taskResource = new TaskRouterResource();
		final TwimlResource twimlResource = new TwimlResource();
		
		final WeatherHealthCheck healthCheck = new WeatherHealthCheck();
		
		environment.jersey().register(taskResource);
		environment.jersey().register(twimlResource);
		environment.healthChecks().register("WeatherAdvisory", healthCheck);
	}

}
