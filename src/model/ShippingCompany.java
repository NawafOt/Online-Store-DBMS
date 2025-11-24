package model;

public class ShippingCompany {
    private int sid;
    private String name;
    private String phoneNumber;
    private double cost;

    public ShippingCompany() {
    }

    public ShippingCompany(int sid, String name, String phoneNumber, double cost) {
        this.sid = sid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cost = cost;
    }

    public ShippingCompany(int sid, String name, String phoneNumber) {
        this.sid = sid;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public ShippingCompany(String name, String phoneNumber, double cost) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cost = cost;
    }

    // Getters and Setters
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", cost);
    }
}
