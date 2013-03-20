package pl.edu.agh.security.common.services;

import java.math.BigDecimal;
import java.util.Date;

public class DeliveryState {
	private Date eta;
	private BigDecimal price;
	
	public Date getEta() {
		return eta;
	}
	public void setEta(Date eta) {
		this.eta = eta;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
}
