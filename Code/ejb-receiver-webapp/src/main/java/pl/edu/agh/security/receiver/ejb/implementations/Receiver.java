package pl.edu.agh.security.receiver.ejb.implementations;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import pl.edu.agh.security.provider.ejb.interfaces.IProvider;
import pl.edu.agh.security.receiver.ejb.interfaces.IReceiver;


@Stateless
@Remote(IReceiver.class)
public class Receiver implements IReceiver{

	@EJB
	private IProvider provider;

	@Override
	public String receive() {
		return provider.provide();
	}

	@Override
	public String receiveUnprotected() {
		return provider.provideForAll();
	}
	
}
