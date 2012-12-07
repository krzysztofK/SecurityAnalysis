package pl.edu.agh.security.weather.ejb.implementations;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import pl.edu.agh.security.weather.ejb.interfaces.IWeatherService;

@Stateless
@RolesAllowed({"magister"})
public class WeatherService implements IWeatherService {

	public static final String MAGISTER_STRING = "magister";
	
	@RolesAllowed({"magister"})
	@Override
	public String invokeWeatherServiceForMagister() {
		return "W okolicy magistra temperatura wyniesie -5 stopni";
	}

	@PermitAll
	@Override
	public String invokeWeatherServiceForAll() {
		return "W Polsce temperatura wyniesie 0 stopni";
	}

	@DenyAll
	@Override
	public String invokeUnavailableWeatherService() {
		return "W niedostepnych rejonach temperatura wyniesie 35 stopni";
	}

}
