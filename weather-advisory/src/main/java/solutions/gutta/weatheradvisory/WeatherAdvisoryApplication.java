package solutions.gutta.weatheradvisory;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import solutions.gutta.weatheradvisory.health.WeatherHealthCheck;
import solutions.gutta.weatheradvisory.resources.TaskRouterResource;

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
		// TODO Auto-generated method stub
		super.initialize(bootstrap);
	}
	
	@Override
	public void run(WeatherAdvisoryConfiguration configuration, Environment environment) throws Exception {
		final TaskRouterResource taskResource = new TaskRouterResource();
		final WeatherHealthCheck healthCheck = new WeatherHealthCheck();
		
		environment.jersey().register(taskResource);
		environment.healthChecks().register("WeatherAdvisory", healthCheck);
	}

}
