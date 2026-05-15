package model;

import model.enums.PaymentMethod;

public class Payment {


    private int orderId;
    private String method;
    private int customerId;
    private double totalAmount;

    // For display purposes
    private String customerName;

    public Payment() {
    }

    public Payment(int orderId, String method, int customerId, double totalAmount) {
        this.orderId = orderId;
        this.method = method;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String incomingMethod) {
        if (incomingMethod == null)
            throw new IllegalArgumentException("Method cannot be null");
        if (!PaymentMethod.isValidMethod(incomingMethod))
            throw new IllegalArgumentException("Invalid method " + incomingMethod);

        method = incomingMethod;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
