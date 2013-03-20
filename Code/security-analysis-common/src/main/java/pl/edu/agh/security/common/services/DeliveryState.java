package pl.edu.agh.security.common.services;

import java.math.BigDecimal;
import java.util.Date;

public class DeliveryState {
    private String id;
    private Date eta;
    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return String.format("Delivery %s, ETA: %s, Delivery price: %s", id, eta, price);
    }

}
