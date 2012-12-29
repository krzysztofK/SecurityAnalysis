package pl.edu.agh.security.provider.ejb.interfaces;

public interface IProvider {

	public String provide();
	
	public String provideForAll();
	
	public String provideForOtherRole();
	
	public String provideDenyForAll() ;
}
