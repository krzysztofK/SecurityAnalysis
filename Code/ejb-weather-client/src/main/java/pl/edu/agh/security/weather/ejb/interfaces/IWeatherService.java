package pl.edu.agh.security.weather.ejb.interfaces;

import javax.ejb.Remote;

@Remote
public interface IWeatherService {

	public String invokeWeatherServiceForMagister();

	public String invokeWeatherServiceForAll();

	public String invokeUnavailableWeatherService();
}
