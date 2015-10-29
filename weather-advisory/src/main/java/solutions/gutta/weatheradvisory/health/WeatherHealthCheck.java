package solutions.gutta.weatheradvisory.health;

import com.codahale.metrics.health.HealthCheck;

public class WeatherHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		//for now let's return healthy, but we need to add more to check health
		return Result.healthy("All is well!!");
	}
}
