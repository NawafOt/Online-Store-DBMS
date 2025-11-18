package dao;

import model.Payment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class PaymentDAO extends BaseDAO<Payment> {

    @Override
    protected String getTableName() {
        return "Payment";
    }

    @Override
    protected Payment mapResultSetToEntity(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setOrderId(rs.getInt("OrderID"));
        payment.setMethod(rs.getString("Method"));
        payment.setStatus(rs.getString("Status"));
        payment.setCustomerId(rs.getInt("CustomerID"));
        try {
            payment.setCustomerName(rs.getString("CustomerName"));
        } catch (SQLException e) {
            // Column doesn't exist, that's okay
        }

        return payment;
    }

    @Override
    public int insert(Payment payment) {
        String query = "INSERT INTO Payment (OrderID, Method, Status, CustomerID) VALUES (?, ?, ?, ?)";
        int rowsAffected = executeUpdate(
                query,
                payment.getOrderId(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCustomerId()
        );
        return rowsAffected > 0 ? payment.getOrderId() : -1;
    }

    @Override
    public boolean update(Payment payment) {
        String query = "UPDATE Payment SET Method = ?, Status = ?, CustomerID = ? WHERE OrderID = ?";
        int rowsAffected = executeUpdate(
                query,
                payment.getMethod(),
                payment.getStatus(),
                payment.getCustomerId(),
                payment.getOrderId()
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int orderId) {
        String query = "DELETE FROM Payment WHERE OrderID = ?";
        int rowsAffected = executeUpdate(query, orderId);
        return rowsAffected > 0;
    }

    @Override
    public Payment getById(int orderId) {
        String query = "SELECT * FROM Payment WHERE OrderID = ?";
        List<Payment> results = executeQuery(query, orderId);
        return results.isEmpty() ? null : results.getFirst();
    }

    @Override
    public List<Payment> getAll() {
        String query = "SELECT * FROM Payment";
        return executeQuery(query);
    }


    public List<Payment> getByCustomerId(int customerId) {
        String query = "SELECT * FROM Payment WHERE CustomerID = ?";
        return executeQuery(query, customerId);
    }


    public List<Payment> getByStatus(String status) {
        String query = "SELECT * FROM Payment WHERE Status = ?";
        return executeQuery(query, status);
    }


    public List<Payment> getByMethod(String method) {
        String query = "SELECT * FROM Payment WHERE Method = ?";
        return executeQuery(query, method);
    }


    public boolean updateStatus(int orderId, String newStatus) {
        String query = "UPDATE Payment SET Status = ? WHERE OrderID = ?";
        int rowsAffected = executeUpdate(query, newStatus, orderId);
        return rowsAffected > 0;
    }


    public List<Payment> getAllWithCustomerDetails() {
        String query = "SELECT p.*, c.Name as CustomerName " +
                "FROM Payment p " +
                "JOIN Customer c ON p.CustomerID = c.Cid";
        return executeQuery(query);
    }


    public List<Payment> getPendingPayments() {
        return getByStatus("Pending");
    }


    public List<Payment> getCompletedPayments() {
        return getByStatus("Completed");
    }


    public boolean existsForOrder(int orderId) {
        Payment payment = getById(orderId);
        return payment != null;
    }


    public Payment getPaymentWithFullDetails(int orderId) {
        String query = "SELECT p.*, c.Name as CustomerName, o.Date, o.Status as OrderStatus " +
                "FROM Payment p " +
                "JOIN Customer c ON p.CustomerID = c.Cid " +
                "JOIN `Order` o ON p.OrderID = o.Oid " +
                "WHERE p.OrderID = ?";
        List<Payment> results = executeQuery(query, orderId);
        return results.isEmpty() ? null : results.getFirst();
    }
}