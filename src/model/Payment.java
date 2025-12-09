package model;

public class Payment {

    public static enum Method {
        CREDIT_CARD,
        BANK_TRANSFER,
        CASH
    }

    private int orderId;
    private Method method;
    private int customerId;
    private double totalAmount;

    // For display purposes
    private String customerName;

    public Payment() {
    }

    public Payment(int orderId, Method method, int customerId, double totalAmount) {
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

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
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
