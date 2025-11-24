package model;


public class Wishlist {
    private int customerId;
    private int productId;

    public Wishlist() {
    }

    public Wishlist(int customerId, int productId) {
        this.customerId = customerId;
        this.productId = productId;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "customerId=" + customerId +
                ", productId=" + productId +
                '}';
    }
}
