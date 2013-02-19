package pl.edu.agh.security.receiver;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.security.auth.callback.ObjectCallback;
import org.picketlink.identity.federation.core.wstrust.SamlCredential;

public class AssertionCallbackHandler implements CallbackHandler {

	private static SamlCredential samlCredential;

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			if (callback instanceof ObjectCallback) {
				ObjectCallback objectCallback = (ObjectCallback) callback;
				objectCallback.setCredential(samlCredential);
			} else if (callback instanceof PasswordCallback) {
				PasswordCallback passwordCallback = (PasswordCallback) callback;
				passwordCallback.setPassword(samlCredential
						.getAssertionAsString().toCharArray());
			} else if (callback instanceof NameCallback) {
				NameCallback nameCallback = (NameCallback) callback;
				nameCallback.setName("magister");
			} else {
				throw new UnsupportedCallbackException(callback);
			}
		}
	}

	public static void setSamlCredential(SamlCredential samlCredentialToSet) {
		samlCredential = samlCredentialToSet;
	}
}
