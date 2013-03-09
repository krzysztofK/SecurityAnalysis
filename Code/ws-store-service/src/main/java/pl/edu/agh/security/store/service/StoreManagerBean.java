package pl.edu.agh.security.store.service;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
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
//@SecurityDomain("ws-ldap-saml")
//@DeclareRoles({ "magister" })
//@RolesAllowed({ "magister" })
@Stateless
@Remote(IStoreManager.class)
@WebService(name = "StoreManager", serviceName = "StoreManagerService")
@SOAPBinding(parameterStyle = ParameterStyle.BARE)
public class StoreManagerBean implements IStoreManager {

	@WebMethod
	public StoreStateResponse getStoreState(StoreStateRequest storeStateRequest) {
		return new StoreStateResponse(storeStateRequest.getProduct(), 22);
	}
}
