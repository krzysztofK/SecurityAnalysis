package pl.edu.agh.security.deps.warehouse;

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
