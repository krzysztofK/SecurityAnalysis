package pl.edu.agh.security.receiver.ejb.interfaces;

public interface IReceiver {

	public String receive();

	public String receiveUnprotected();

	public String receiveForOtherRole();
	
	public String receiveForbiddenMessage();
}
