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
        payment.setMethod(Payment.Method.valueOf(rs.getString("Method").toUpperCase()));
        payment.setCustomerId(rs.getInt("CustomerID"));
        payment.setTotalAmount(rs.getDouble("TotalAmount"));
        try {
            payment.setCustomerName(rs.getString("CustomerName"));
        } catch (SQLException e) {
            // Column doesn't exist, that's okay
        }
        return payment;
    }

    @Override
    public int insert(Payment payment) {
        String query = "INSERT INTO Payment (OrderID, Method, CustomerID, TotalAmount) VALUES (?, ?, ?, ?)";
        int rowsAffected = executeUpdate(
                query,
                payment.getOrderId(),
                payment.getMethod().name(),
                payment.getCustomerId(),
                payment.getTotalAmount()
        );
        return rowsAffected > 0 ? payment.getOrderId() : -1;
    }

    @Override
    public boolean update(Payment payment) {
        String query = "UPDATE Payment SET Method = ?, CustomerID = ?, TotalAmount = ? WHERE OrderID = ?";
        int rowsAffected = executeUpdate(
                query,
                payment.getMethod().name(),
                payment.getCustomerId(),
                payment.getTotalAmount(),
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

    public List<Payment> getByMethod(Payment.Method method) {
        String query = "SELECT * FROM Payment WHERE Method = ?";
        return executeQuery(query, method.name());
    }
}
