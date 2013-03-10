package pl.edu.agh.security.store.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import pl.edu.agh.security.store.interfaces.IStoreManager;

/**
 * @author Krzysztof
 * 
 */
//@Stateless
//@Remote(IStoreManager.class)
@WebService(name = "StoreManager", serviceName = "StoreManagerService")
//@WebContext(contextRoot="/ws-store-service-0.0.1-SNAPSHOT/StoreManagerService/", urlPattern = "/*", authMethod = "BASIC", transportGuarantee = "NONE", secureWSDLAccess = false)
// @WebContext(authMethod = "BASIC", urlPattern = "/*")
@SOAPBinding(parameterStyle = ParameterStyle.BARE)
//@SecurityDomain("ws-ldap-saml")
//@DeclareRoles({ "magister", "doktor" })
//@RolesAllowed({ "magister", "doktor" })
public class StoreManagerBean implements IStoreManager {

//	@RolesAllowed({ "magister", "doktor" })
	@WebMethod
	public StoreStateResponse getStoreState(StoreStateRequest storeStateRequest) {
		return new StoreStateResponse(storeStateRequest.getProduct(), 22);
	}
}
