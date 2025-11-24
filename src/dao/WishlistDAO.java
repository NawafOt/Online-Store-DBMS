package dao;

import model.Wishlist;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class WishlistDAO extends BaseDAO<Wishlist> {

    @Override
    protected String getTableName() {
        return "Wishlist";
    }

    @Override
    protected Wishlist mapResultSetToEntity(ResultSet rs) throws SQLException {
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(rs.getInt("CustomerID"));
        wishlist.setProductId(rs.getInt("ProductID"));

        return wishlist;
    }

    @Override
    public int insert(Wishlist wishlist) {
        String query = "INSERT INTO Wishlist (CustomerID, ProductID) VALUES (?, ?)";
        int rowsAffected = executeUpdate(
                query,
                wishlist.getCustomerId(),
                wishlist.getProductId()
        );
        return rowsAffected > 0 ? 1 : -1; // Return 1 on success, -1 on failure
    }

    @Override
    public boolean update(Wishlist wishlist) {
        throw new UnsupportedOperationException("Wishlist entries cannot be updated, only inserted or deleted.");
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Use deleteComposite(customerId, productId) instead.");
    }

    @Override
    public boolean deleteComposite(int customerId, int productId) {
        String query = "DELETE FROM Wishlist WHERE CustomerID = ? AND ProductID = ?";
        int rowsAffected = executeUpdate(query, customerId, productId);
        return rowsAffected > 0;
    }

    @Override
    public Wishlist getById(int id) {
        throw new UnsupportedOperationException("Use getByIdComposite(customerId, productId) instead.");
    }

    @Override
    public Wishlist getByIdComposite(int customerId, int productId) {
        String query = "SELECT * FROM Wishlist WHERE CustomerID = ? AND ProductID = ?";
        List<Wishlist> results = executeQuery(query, customerId, productId);
        return results.isEmpty() ? null : results.getFirst();
    }

    @Override
    public List<Wishlist> getAll() {
        String query = "SELECT * FROM Wishlist";
        return executeQuery(query);
    }


    public List<Wishlist> getByCustomerId(int customerId) {
        String query = "SELECT * FROM Wishlist WHERE CustomerID = ?";
        return executeQuery(query, customerId);
    }

    public boolean isInWishlist(int customerId, int productId) {
        return getByIdComposite(customerId, productId) != null;
    }

    public boolean addToWishlist(int customerId, int productId) {
        if (isInWishlist(customerId, productId)) {
            return false; // Item is already in the wishlist
        }

        Wishlist wishlist = new Wishlist(customerId, productId);
        return insert(wishlist) > 0;
    }

    public boolean removeFromWishlist(int customerId, int productId) {
        return deleteComposite(customerId, productId);
    }
}
