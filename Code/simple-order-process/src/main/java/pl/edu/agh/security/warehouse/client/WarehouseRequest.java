package pl.edu.agh.security.warehouse.client;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="registerTransaction")
@XmlType(name = "registerTransaction", namespace = "http://warehouse.deps.security.agh.edu.pl/")
public class WarehouseRequest {
    private String product;
    private int count;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
