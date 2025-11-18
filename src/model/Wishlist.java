package model;


public class Wishlist {
    private int customerId;
    private int productId;

    // For display purposes
    private String customerName;
    private String productName;
    private double productPrice;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "customerId=" + customerId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                '}';
    }
}