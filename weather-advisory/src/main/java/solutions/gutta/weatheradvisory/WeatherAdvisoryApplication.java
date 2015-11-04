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
import solutions.gutta.weatheradvisory.resources.ZipcodeMappingResource;

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
		//Let's set the configuration from properties
		if (isMissing(configuration.getAccountSid())) {
			configuration.setAccountSid(System.getProperty("accountSid"));
		}
		
		if (isMissing(configuration.getAuthToken())) {
			configuration.setAuthToken(System.getProperty("authToken"));
		}
		
		if (isMissing(configuration.getUrlPrefix())) {
			configuration.setUrlPrefix(System.getProperty("urlPrefix"));
		}

		System.out.println(String.format("accountSid = %s", configuration.getAccountSid()));
		System.out.println(String.format("authToken=%s", configuration.getAuthToken()));
		System.out.println(String.format("urlPrefix = %s", configuration.getUrlPrefix()));

		WeatherAdvisoryTwilioResource.init(configuration.getAccountSid(), configuration.getAuthToken(), configuration.getUrlPrefix());
		
		final TaskRouterResource taskResource = new TaskRouterResource();		
		final TwimlResource twimlResource = new TwimlResource();
		final ZipcodeMappingResource zipcodeResource = new ZipcodeMappingResource();
		
		final WeatherHealthCheck healthCheck = new WeatherHealthCheck();
		
		environment.jersey().register(taskResource);
		environment.jersey().register(twimlResource);
		environment.jersey().register(zipcodeResource);
		
		environment.healthChecks().register("WeatherAdvisory", healthCheck);
		
		WorkerAvailabilityUpdater.getInstance().start();
		
		WeatherPoller.getInstance().start();
		
	}

	boolean isMissing(String str) {
		return str == null || str.isEmpty();
	}
	
}
