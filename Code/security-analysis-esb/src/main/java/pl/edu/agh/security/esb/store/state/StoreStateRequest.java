package pl.edu.agh.security.esb.store.state;

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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return productName + ": " + count;
	}
}
