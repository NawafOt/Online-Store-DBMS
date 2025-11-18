package dao;

import model.ShippingCompany;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ShippingDAO extends BaseDAO<ShippingCompany> {

    @Override
    protected String getTableName() {
        return "ShippingCompany";
    }

    @Override
    protected ShippingCompany mapResultSetToEntity(ResultSet rs) throws SQLException {
        ShippingCompany shipping = new ShippingCompany();
        shipping.setSid(rs.getInt("Sid"));
        shipping.setName(rs.getString("Name"));
        shipping.setPhoneNumber(rs.getString("PhoneNumber"));
        return shipping;
    }

    @Override
    public int insert(ShippingCompany shipping) {
        String query = "INSERT INTO ShippingCompany (Name, PhoneNumber) VALUES (?, ?)";
        return executeInsertWithGeneratedKey(
                query,
                shipping.getName(),
                shipping.getPhoneNumber()
        );
    }

    @Override
    public boolean update(ShippingCompany shipping) {
        String query = "UPDATE ShippingCompany SET Name = ?, PhoneNumber = ? WHERE Sid = ?";
        int rowsAffected = executeUpdate(
                query,
                shipping.getName(),
                shipping.getPhoneNumber(),
                shipping.getSid()
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int sid) {
        String query = "DELETE FROM ShippingCompany WHERE Sid = ?";
        int rowsAffected = executeUpdate(query, sid);
        return rowsAffected > 0;
    }

    @Override
    public ShippingCompany getById(int sid) {
        String query = "SELECT * FROM ShippingCompany WHERE Sid = ?";
        List<ShippingCompany> results = executeQuery(query, sid);
        return results.isEmpty() ? null : results.getFirst();
    }

    @Override
    public List<ShippingCompany> getAll() {
        String query = "SELECT * FROM ShippingCompany";
        return executeQuery(query);
    }


    public List<ShippingCompany> searchByName(String name) {
        String query = "SELECT * FROM ShippingCompany WHERE Name LIKE ?";
        return executeQuery(query, "%" + name + "%");
    }


    public ShippingCompany findByName(String name) {
        String query = "SELECT * FROM ShippingCompany WHERE Name = ?";
        List<ShippingCompany> results = executeQuery(query, name);
        return results.isEmpty() ? null : results.getFirst();
    }


    public ShippingCompany findByPhoneNumber(String phoneNumber) {
        String query = "SELECT * FROM ShippingCompany WHERE PhoneNumber = ?";
        List<ShippingCompany> results = executeQuery(query, phoneNumber);
        return results.isEmpty() ? null : results.getFirst();
    }
}