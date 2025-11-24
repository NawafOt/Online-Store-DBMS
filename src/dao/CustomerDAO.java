package dao;

import model.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class CustomerDAO extends BaseDAO<Customer> {

    @Override
    protected String getTableName() {
        return "Customer";
    }

    @Override
    protected Customer mapResultSetToEntity(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCid(rs.getInt("Cid"));
        customer.setName(rs.getString("Name"));
        customer.setEmail(rs.getString("Email"));
        customer.setPassword(rs.getString("Password"));
        customer.setPhoneNumber(rs.getString("PhoneNumber"));
        customer.setAddress(rs.getString("Address"));
        return customer;
    }

    @Override
    public int insert(Customer customer) {
        String query = "INSERT INTO Customer (Name, Email, Password, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?)";
        return executeInsertWithGeneratedKey(
                query,
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getPhoneNumber(),
                customer.getAddress()
        );
    }

    @Override
    public boolean update(Customer customer) {
        String query = "UPDATE Customer SET Name = ?, Email = ?, Password = ?, PhoneNumber = ?, Address = ? WHERE Cid = ?";
        int rowsAffected = executeUpdate(
                query,
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getCid()
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int cid) {
        String query = "DELETE FROM Customer WHERE Cid = ?";
        int rowsAffected = executeUpdate(query, cid);
        return rowsAffected > 0;
    }

    @Override
    public Customer getById(int cid) {
        String query = "SELECT * FROM Customer WHERE Cid = ?";
        List<Customer> results = executeQuery(query, cid);
        return results.isEmpty() ? null : results.getFirst();
    }

    @Override
    public List<Customer> getAll() {
        String query = "SELECT * FROM Customer";
        return executeQuery(query);
    }


    public Customer findByEmail(String email) {
        String query = "SELECT * FROM Customer WHERE Email = ?";
        List<Customer> results = executeQuery(query, email);
        return results.isEmpty() ? null : results.getFirst();
    }

    public Customer findByPhone(String phone) {
        String query = "SELECT * FROM Customer WHERE PhoneNumber = ?";
        List<Customer> results = executeQuery(query, phone);
        return results.isEmpty() ? null : results.getFirst();
    }


    public List<Customer> searchByName(String name) {
        String query = "SELECT * FROM Customer WHERE Name LIKE ?";
        return executeQuery(query, "%" + name + "%");
    }


    public Customer authenticate(String email, String password) {
        String query = "SELECT * FROM Customer WHERE Email = ? AND Password = ?";
        List<Customer> results = executeQuery(query, email, password);
        return results.isEmpty() ? null : results.getFirst();
    }


    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    public boolean phoneExists(String phone) {
        return findByPhone(phone) != null;
    }
}
