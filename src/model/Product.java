package model;


public class Product {
    private int pid;
    private String name;
    private double unitPrice;
    private String category;
    private int stock;

    public Product() {
    }

    public Product(int pid, String name, double unitPrice, String category, int stock) {
        this.pid = pid;
        this.name = name;
        this.unitPrice = unitPrice;
        this.category = category;
        this.stock = stock;
    }

    public Product(String name, double unitPrice, String category, int stock) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.category = category;
        this.stock = stock;
    }

    // Getters and Setters
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    public boolean hasEnoughStock(int quantity) {
        return stock >= quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "pid=" + pid +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", category='" + category + '\'' +
                ", stock=" + stock +
                '}';
    }

    public String getDisplayName() {
        return name + " - $" + String.format("%.2f", unitPrice) + " (Stock: " + stock + ")";
    }
}