package model;

import model.enums.OrderStatus;

import java.sql.Date;

public class Order {


    private int oid;
    private String status;
    private Date date;
    private int customerId;
    private int shippingId;
    private double shippingCost;

    // For display purposes
    private String customerName;
    private double totalAmount;

    public Order() {
        // Default
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

    public void setStatus(String incomingStatus) {
        if (incomingStatus == null)
            throw new NullPointerException("Status cannot be null");
        if (!OrderStatus.isValidStatus(incomingStatus))
            throw new IllegalArgumentException("Invalid Status " + incomingStatus);

        this.status = incomingStatus.toUpperCase();
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

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
