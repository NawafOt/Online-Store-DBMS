package dao;

import model.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO extends BaseDAO<Product> {

    @Override
    protected String getTableName() {
        return "Product";
    }

    @Override
    protected Product mapResultSetToEntity(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setPid(rs.getInt("Pid"));
        product.setName(rs.getString("Name"));
        product.setUnitPrice(rs.getDouble("UnitPrice"));
        product.setCategory(rs.getString("Category"));
        product.setStock(rs.getInt("Stock"));
        product.setHidden(rs.getBoolean("Hide"));
        return product;
    }

    @Override
    public int insert(Product product) {
        String query = "INSERT INTO Product (Name, UnitPrice, Category, Stock) VALUES (?, ?, ?, ?)";
        return executeInsertWithGeneratedKey(
                query,
                product.getName(),
                product.getUnitPrice(),
                product.getCategory(),
                product.getStock()
        );
    }

    @Override
    public boolean update(Product product) {
        String query = "UPDATE Product SET Name = ?, UnitPrice = ?, Category = ?, Stock = ? WHERE Pid = ?";
        int rowsAffected = executeUpdate(
                query,
                product.getName(),
                product.getUnitPrice(),
                product.getCategory(),
                product.getStock(),
                product.getPid()
        );
        return rowsAffected > 0;
    }

    public boolean updateHide(Product product){
        String query = "UPDATE Product SET Hide = ?";
        int rowsAffected = executeUpdate(
                query,
                product.isHidden()
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int pid) {
        String query = "UPDATE Product SET Hide = 1 WHERE Pid = ?";
        int rowsAffected = executeUpdate(query, pid);
        return rowsAffected > 0;
    }

    @Override
    public Product getById(int pid) {
        String query = "SELECT * FROM Product WHERE Pid = ?";
        List<Product> results = executeQuery(query, pid);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM Product WHERE Hide = 0";
        return executeQuery(query);
    }

    public List<Product> getHidden() {
        String query = "SELECT * FROM Product WHERE Hide = 1";
        return executeQuery(query);
    }

    /**
     * Retrieves all products that are on a specific customer's wishlist.
     * @param customerId The ID of the customer.
     * @return A list of Product objects.
     */
    public List<Product> getProductsByWishlistCustomerId(int customerId) {
        String query = "SELECT p.* FROM Product p " +
                       "JOIN Wishlist w ON p.Pid = w.ProductID " +
                       "WHERE w.CustomerID = ?";
        return executeQuery(query, customerId);
    }


    public  List<Product> getLimited(int limit, int offset, boolean descending) {
        if (limit < 1)
            throw new IllegalArgumentException("limit must be greater than 0");
        if (offset < 0)
            throw new IllegalArgumentException("offset must be non-negative");

        String orderType = (descending ? "DESC" : "ASC");

        String query = String.format(
                "SELECT * " +
                "FROM Product " +
                "ORDER BY Name %s" +
                "LIMIT %d offset %d",
                orderType, limit, offset); // classical method because why not?

        return executeQuery(query);
    }

    public List<Product> searchByNameAdmin(String name) {
        String query = "SELECT * FROM Product WHERE Name LIKE ?";
        return executeQuery(query, "%" + name + "%");
    }

    public List<Product> searchByName(String name) {
        String query = "SELECT * FROM Product WHERE Name LIKE ? AND Hide = 0";
        return executeQuery(query, "%" + name + "%");
    }

    public List<Product> getByCategory(String category) {
        String query = "SELECT * FROM Product WHERE Category = ? AND Hide = 0";
        return executeQuery(query, category);
    }


    public List<Product> getByPriceRange(double minPrice, double maxPrice) {
        String query = "SELECT * FROM Product WHERE UnitPrice BETWEEN ? AND ?";
        return executeQuery(query, minPrice, maxPrice);
    }


    public List<Product> getInStockProducts() {
        String query = "SELECT * FROM Product WHERE Stock > 0 AND Hide = 0";
        return executeQuery(query);
    }


    public List<Product> getLowStockProducts(int threshold) {
        String query = "SELECT * FROM Product WHERE Stock <= ? AND Stock > 0";
        return executeQuery(query, threshold);
    }


    public boolean reduceStock(int pid, int quantity) {
        String query = "UPDATE Product SET Stock = Stock - ? WHERE Pid = ? AND Stock >= ?";
        int rowsAffected = executeUpdate(query, quantity, pid, quantity);
        return rowsAffected > 0;
    }


    public boolean increaseStock(int pid, int quantity) {
        String query = "UPDATE Product SET Stock = Stock + ? WHERE Pid = ?";
        int rowsAffected = executeUpdate(query, quantity, pid);
        return rowsAffected > 0;
    }


    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT Category FROM Product WHERE Hide = 0 ORDER BY Category";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String categoryName = rs.getString("Category");
                categories.add(categoryName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return categories;
    }
}
