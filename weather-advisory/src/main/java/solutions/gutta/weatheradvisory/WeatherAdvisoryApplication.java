package solutions.gutta.weatheradvisory;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class WeatherAdvisoryApplication extends Application<WeatherAdvisoryConfiguration>{

	public static void main(String[] args) {
		
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
		// TODO Auto-generated method stub
		
	}

}
