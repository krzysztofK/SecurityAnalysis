package pl.edu.agh.security.store.interfaces;

import pl.edu.agh.security.store.service.StoreStateRequest;
import pl.edu.agh.security.store.service.StoreStateResponse;

/**
 * 
 * @author Krzysztof
 * 
 */
public interface IStoreManager {

	public StoreStateResponse getStoreState(StoreStateRequest storeStateRequest);
}
