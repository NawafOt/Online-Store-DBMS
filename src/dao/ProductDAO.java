package dao;

import model.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public boolean delete(int pid) {
        String query = "DELETE FROM Product WHERE Pid = ?";
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
        String query = "SELECT * FROM Product";
        return executeQuery(query);
    }


    public List<Product> searchByName(String name) {
        String query = "SELECT * FROM Product WHERE Name LIKE ?";
        return executeQuery(query, "%" + name + "%");
    }


    public List<Product> getByCategory(String category) {
        String query = "SELECT * FROM Product WHERE Category = ?";
        return executeQuery(query, category);
    }


    public List<Product> getByPriceRange(double minPrice, double maxPrice) {
        String query = "SELECT * FROM Product WHERE UnitPrice BETWEEN ? AND ?";
        return executeQuery(query, minPrice, maxPrice);
    }


    public List<Product> getInStockProducts() {
        String query = "SELECT * FROM Product WHERE Stock > 0";
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
        String query = "SELECT DISTINCT Category FROM Product ORDER BY Category";
        List<Product> products = executeQuery(query);
        return products.stream().map(Product::getCategory).toList();
    }
}