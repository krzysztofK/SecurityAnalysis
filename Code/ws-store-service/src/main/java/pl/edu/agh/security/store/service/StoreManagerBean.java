package pl.edu.agh.security.store.service;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import org.jboss.ejb3.annotation.SecurityDomain;

import pl.edu.agh.security.store.interfaces.IStoreManager;

/**
 * @author Krzysztof
 * 
 */
@Stateless
@WebService(name = "StoreManager", serviceName = "StoreManagerService")
@SOAPBinding(parameterStyle = ParameterStyle.BARE)
@SecurityDomain("ws-ldap-saml")
@HandlerChain(file = "/META-INF/handlers.xml")
public class StoreManagerBean implements IStoreManager {

	@RolesAllowed({ "magister", "doktor" })
	@WebMethod
	public StoreStateResponse getStoreState(StoreStateRequest storeStateRequest) {
		return new StoreStateResponse(storeStateRequest.getProduct(), 22);
	}
}
