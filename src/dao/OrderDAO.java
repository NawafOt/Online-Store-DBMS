package dao;

import model.Order;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO extends BaseDAO<Order> {

    @Override
    protected String getTableName() {
        return "`Order`"; // Order is SQL reserved word
    }

    @Override
    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOid(rs.getInt("Oid"));
        order.setStatus(Order.Status.valueOf(rs.getString("Status").toUpperCase()));
        order.setDate(rs.getDate("Date"));
        order.setCustomerId(rs.getInt("CustomerID"));
        order.setShippingId(rs.getInt("ShippingID"));
        order.setShippingCost(rs.getDouble("ShippingCost"));
        try {
            order.setCustomerName(rs.getString("CustomerName"));
        } catch (SQLException e) {
            // Column doesn't exist, that's okay
        }
        return order;
    }

    @Override
    public int insert(Order order) {
        String query = "INSERT INTO `Order` (Status, Date, CustomerID, ShippingID, ShippingCost) VALUES (?, ?, ?, ?, ?)";
        return executeInsertWithGeneratedKey(
                query,
                order.getStatus().name(),
                order.getDate(),
                order.getCustomerId(),
                order.getShippingId(),
                order.getShippingCost()
        );
    }

    @Override
    public boolean update(Order order) {
        String query = "UPDATE `Order` SET Status = ?, Date = ?, CustomerID = ?, ShippingID = ?, ShippingCost = ? WHERE Oid = ?";
        int rowsAffected = executeUpdate(
                query,
                order.getStatus().name(),
                order.getDate(),
                order.getCustomerId(),
                order.getShippingId(),
                order.getShippingCost(),
                order.getOid()
        );
        return rowsAffected > 0;
    }

    public boolean updateStatus(int oid, Order.Status newStatus) {
        String query = "UPDATE `Order` SET Status = ? WHERE Oid = ?";
        int rowsAffected = executeUpdate(query, newStatus.name(), oid);
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int oid) {
        String query = "DELETE FROM `Order` WHERE Oid = ?";
        int rowsAffected = executeUpdate(query, oid);
        return rowsAffected > 0;
    }

    @Override
    public Order getById(int oid) {
        String query = "SELECT * FROM `Order` WHERE Oid = ?";
        List<Order> results = executeQuery(query, oid);
        return results.isEmpty() ? null : results.getFirst();
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM `Order`";
        return executeQuery(query);
    }

    public List<Order> getByCustomerId(int customerId) {
        String query = "SELECT * FROM `Order` WHERE CustomerID = ? ORDER BY Date DESC";
        return executeQuery(query, customerId);
    }

    public List<Order> getByStatus(Order.Status status) {
        String query = "SELECT * FROM `Order` WHERE Status = ?";
        return executeQuery(query, status.name());
    }

    public List<Order> getByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        String query = "SELECT * FROM `Order` WHERE Date BETWEEN ? AND ?";
        return executeQuery(query, startDate, endDate);
    }

    public List<Order> getAllWithCustomerDetails() {
        String query = "SELECT o.*, c.Name as CustomerName " +
                "FROM `Order` o " +
                "JOIN Customer c ON o.CustomerID = c.Cid " +
                "ORDER BY o.Date DESC";
        return executeQuery(query);
    }
}
