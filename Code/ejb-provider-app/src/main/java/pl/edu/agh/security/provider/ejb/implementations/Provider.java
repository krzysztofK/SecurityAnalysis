package pl.edu.agh.security.provider.ejb.implementations;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

import pl.edu.agh.security.provider.ejb.interfaces.IProvider;

@Stateless
@SecurityDomain("ejb-ldap")
@Remote(IProvider.class)
@RolesAllowed({ "magister" })
@DeclareRoles({ "magister" })
public class Provider implements IProvider {

	private static final String PROVIDED_TEXT = "Provided text";

	private static final String TEXT_FOR_ALL = "Text for all";

	@RolesAllowed({ "magister" })
	@Override
	public String provide() {
		return PROVIDED_TEXT;
	}

	@PermitAll
	@Override
	public String provideForAll() {
		// TODO Auto-generated method stub
		return TEXT_FOR_ALL;
	}
}
