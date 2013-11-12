package pl.edu.agh.security.rest.interceptor;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.interception.SecurityPrecedence;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

@Provider
@ServerInterceptor
@SecurityPrecedence
public class SecurityInterceptor implements PreProcessInterceptor {
    
    private Logger LOGGER = Logger.getLogger(SecurityInterceptor.class);
		
	@Override
	public ServerResponse preProcess(HttpRequest request, ResourceMethod method)
			throws Failure, WebApplicationException {
		try {
		    LOGGER.info("Trying to authenticate request");
			LoginContext loginContext = new LoginContext("ws-ldap-saml");
			loginContext.login();
		} catch (LoginException e) {
			throw new WebApplicationException();
		}
		return null;
	}

}
