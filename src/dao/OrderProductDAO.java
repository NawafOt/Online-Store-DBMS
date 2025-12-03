package dao;

import model.OrderProduct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderProductDAO extends BaseDAO<OrderProduct> {

    @Override
    protected String getTableName() {
        return "OrderProduct";
    }

    @Override
    protected OrderProduct mapResultSetToEntity(ResultSet rs) throws SQLException {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(rs.getInt("OrderID"));
        orderProduct.setProductId(rs.getInt("ProductID"));
        orderProduct.setQuantity(rs.getInt("Quantity"));
        orderProduct.setPriceAtPurchase(rs.getDouble("PriceAtPurchase"));

        try {
            orderProduct.setProductName(rs.getString("ProductName"));
        } catch (SQLException e) {
            // Columns don't exist, that's okay
        }

        return orderProduct;
    }

    @Override
    public int insert(OrderProduct orderProduct) {
        String query = "INSERT INTO OrderProduct (OrderID, ProductID, Quantity, PriceAtPurchase) VALUES (?, ?, ?, ?)";
        int rowsAffected = executeUpdate(
                query,
                orderProduct.getOrderId(),
                orderProduct.getProductId(),
                orderProduct.getQuantity(),
                orderProduct.getPriceAtPurchase()
        );
        return rowsAffected > 0 ? 1 : -1;
    }

    @Override
    public boolean update(OrderProduct orderProduct) {
        String query = "UPDATE OrderProduct SET Quantity = ? WHERE OrderID = ? AND ProductID = ?";
        int rowsAffected = executeUpdate(
                query,
                orderProduct.getQuantity(),
                orderProduct.getOrderId(),
                orderProduct.getProductId()
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Use deleteComposite(orderId, productId) instead");
    }

    @Override
    public boolean deleteComposite(int orderId, int productId) {
        String query = "DELETE FROM OrderProduct WHERE OrderID = ? AND ProductID = ?";
        int rowsAffected = executeUpdate(query, orderId, productId);
        return rowsAffected > 0;
    }

    @Override
    public OrderProduct getById(int id) {
        throw new UnsupportedOperationException("Use getByCompositeKey(orderId, productId) instead");
    }

    @Override
    public OrderProduct getByIdComposite(int orderId, int productId) {
        String query = "SELECT * FROM OrderProduct WHERE OrderID = ? AND ProductID = ?";
        List<OrderProduct> results = executeQuery(query, orderId, productId);
        return results.isEmpty() ? null : results.getFirst();
    }

    @Override
    public List<OrderProduct> getAll() {
        String query = "SELECT * FROM OrderProduct";
        return executeQuery(query);
    }

    public List<OrderProduct> getByOrderId(int orderId) {
        String query = "SELECT * FROM OrderProduct WHERE OrderID = ?";
        return executeQuery(query, orderId);
    }
    
    public List<OrderProduct> getByOrderIdWithDetails(int orderId) {
        String query = "SELECT op.*, p.Name as ProductName " +
                "FROM OrderProduct op " +
                "JOIN Product p ON op.ProductID = p.Pid " +
                "WHERE op.OrderID = ?";
        return executeQuery(query, orderId);
    }

    public List<OrderProduct> getByProductId(int productId) {
        String query = "SELECT * FROM OrderProduct WHERE ProductID = ?";
        return executeQuery(query, productId);
    }

    public double calculateOrderTotal(int orderId) {
        List<OrderProduct> items = getByOrderIdWithDetails(orderId);
        return items.stream()
                .mapToDouble(OrderProduct::getTotalPrice)
                .sum();
    }

    public boolean deleteByOrderId(int orderId) {
        String query = "DELETE FROM OrderProduct WHERE OrderID = ?";
        int rowsAffected = executeUpdate(query, orderId);
        return rowsAffected > 0;
    }

    public boolean updateQuantity(int orderId, int productId, int newQuantity) {
        String query = "UPDATE OrderProduct SET Quantity = ? WHERE OrderID = ? AND ProductID = ?";
        int rowsAffected = executeUpdate(query, newQuantity, orderId, productId);
        return rowsAffected > 0;
    }

    public boolean existsInOrder(int orderId, int productId) {
        OrderProduct op = getByIdComposite(orderId, productId);
        return op != null;
    }
}