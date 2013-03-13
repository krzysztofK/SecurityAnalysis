package pl.edu.agh.security.order.process;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import pl.edu.agh.security.common.Utils;
import pl.edu.agh.security.store.state.service.client.IStoreState;

public class ProcessMain {

	private static final String REQUEST_PATH = "http://localhost:8080/stores-state-service/state";
	private static final String USER_NAME = "magister";
	private static final String PASSWORD = "inzynier";

	public static void main(String[] args) {
		System.out.println(prepareTestClient().getStore("woda", 20));
	}

	public static IStoreState prepareTestClient() {
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		return ProxyFactory.create(IStoreState.class, REQUEST_PATH);
	}
}
