package model;


public class ShippingCompany {
    private int sid;
    private String name;
    private String phoneNumber;

    public ShippingCompany() {
    }

    public ShippingCompany(int sid, String name, String phoneNumber) {
        this.sid = sid;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public ShippingCompany(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
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

    @Override
    public String toString() {
        return "ShippingCompany{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getDisplayName() {
        return name + " (" + phoneNumber + ")";
    }
}