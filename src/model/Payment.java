package model;

public class Payment {
    private int orderId;
    private String method;
    private String status;
    private int customerId;

    // For display purposes
    private String customerName;

    public Payment() {
    }

    public Payment(int orderId, String method, String status, int customerId) {
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.customerId = customerId;
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

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "orderId=" + orderId +
                ", method='" + method + '\'' +
                ", status='" + status + '\'' +
                ", customerId=" + customerId +
                '}';
    }

    public String getDisplayName() {
        return "Payment for Order #" + orderId + " - " + method + " (" + status + ")";
    }
}