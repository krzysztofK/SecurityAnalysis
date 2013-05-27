package pl.edu.agh.security.store.state.service.client;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public class StoreStateRequest {

	@QueryParam("count")
	private int count;

	@PathParam("name")
	private String productName;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@PathParam("name")
	public String getProductName() {
		return productName;
	}

	@PathParam("name")
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
