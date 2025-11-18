package model;

import java.sql.Date;


public class Order {
    private int oid;
    private String status;
    private Date date;
    private int customerId;
    private int shippingId;

    // For display purposes (not stored in database)
    private String customerName;
    private String shippingCompanyName;

    public Order() {
    }

    public Order(int oid, String status, Date date, int customerId, int shippingId) {
        this.oid = oid;
        this.status = status;
        this.date = date;
        this.customerId = customerId;
        this.shippingId = shippingId;
    }

    public Order(String status, Date date, int customerId, int shippingId) {
        this.status = status;
        this.date = date;
        this.customerId = customerId;
        this.shippingId = shippingId;
    }

    // Getters and Setters
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShippingCompanyName() {
        return shippingCompanyName;
    }

    public void setShippingCompanyName(String shippingCompanyName) {
        this.shippingCompanyName = shippingCompanyName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid=" + oid +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", customerId=" + customerId +
                ", shippingId=" + shippingId +
                '}';
    }

    public String getDisplayName() {
        return "Order #" + oid + " - " + status + " (" + date + ")";
    }
}