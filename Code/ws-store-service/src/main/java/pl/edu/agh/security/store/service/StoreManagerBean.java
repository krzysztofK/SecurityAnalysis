package pl.edu.agh.security.store.service;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import pl.edu.agh.security.store.interfaces.IStoreManager;

/**
 * @author Krzysztof
 * 
 */
@Stateless
@Remote(IStoreManager.class)
@WebService(name = "StoreManager", serviceName = "StoreManager")
@SOAPBinding(parameterStyle = ParameterStyle.BARE)
public class StoreManagerBean implements IStoreManager {

	@WebMethod
	public StoreStateResponse getStoreState(StoreStateRequest storeStateRequest) {
		return new StoreStateResponse(storeStateRequest.getProduct(), 20);
	}
}
