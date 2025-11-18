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
        order.setStatus(rs.getString("Status"));
        order.setDate(rs.getDate("Date"));
        order.setCustomerId(rs.getInt("CustomerID"));
        order.setShippingId(rs.getInt("ShippingID"));
        try {
            order.setCustomerName(rs.getString("CustomerName"));
        } catch (SQLException e) {
            // Column doesn't exist, that's okay
        }
        return order;
    }

    @Override
    public int insert(Order order) {
        String query = "INSERT INTO `Order` (Status, Date, CustomerID, ShippingID) VALUES (?, ?, ?, ?)";
        return executeInsertWithGeneratedKey(
                query,
                order.getStatus(),
                order.getDate(),
                order.getCustomerId(),
                order.getShippingId()
        );
    }

    @Override
    public boolean update(Order order) {
        String query = "UPDATE `Order` SET Status = ?, Date = ?, CustomerID = ?, ShippingID = ? WHERE Oid = ?";
        int rowsAffected = executeUpdate(
                query,
                order.getStatus(),
                order.getDate(),
                order.getCustomerId(),
                order.getShippingId(),
                order.getOid()
        );
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
        return results.isEmpty() ? null : results.get(0);
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



    public List<Order> getByStatus(String status) {
        String query = "SELECT * FROM `Order` WHERE Status = ?";
        return executeQuery(query, status);
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



    public boolean updateStatus(int oid, String newStatus) {
        String query = "UPDATE `Order` SET Status = ? WHERE Oid = ?";
        int rowsAffected = executeUpdate(query, newStatus, oid);
        return rowsAffected > 0;
    }



    public List<Order> getRecentOrders(int limit) {
        String query = "SELECT * FROM `Order` ORDER BY Date DESC LIMIT ?";
        return executeQuery(query, limit);
    }



    public int countByCustomerId(int customerId) {
        List<Order> orders = getByCustomerId(customerId);
        return orders.size();
    }
}