package dao;

import config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Data Access Object class providing common database operations.
 * All specific DAO classes should extend this class to inherit
 * standard CRUD functionality and database connection management.
 */
public abstract class BaseDAO<T> {

    /**
     * Gets a database connection from the DatabaseConfig.
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    protected Connection getConnection() throws SQLException {
        return DatabaseConfig.getConnection();
    }

    /**
     * Closes database resources safely.
     *
     * @param conn Connection to close
     * @param stmt Statement to close
     * @param rs ResultSet to close
     */
    protected void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Closes connection and statement.
     *
     * @param conn Connection to close
     * @param stmt Statement to close
     */
    protected void closeResources(Connection conn, Statement stmt) {
        closeResources(conn, stmt, null);
    }

    /**
     * Closes connection only.
     *
     * @param conn Connection to close
     */
    protected void closeResources(Connection conn) {
        closeResources(conn, null, null);
    }

    // Abstract methods that child classes must implement

    /**
     * Inserts a new record into the database.
     *
     * @param entity the entity to insert
     * @return the generated ID, -1 on failure
     */
    public abstract int insert(T entity);

    /**
     * Updates an existing record in the database.
     *
     * @param entity the entity with updated data
     * @return true if update successful, false otherwise
     */
    public abstract boolean update(T entity);

    /**
     * Deletes a record from the database by ID, For single PK
     *
     * @param id the primary key of the record to delete
     * @return true if deletion successful, false otherwise
     */
    public abstract boolean delete(int id);

    /**
     * Deletes a record from the database by ID, For composite PK
     * @param key1 part of the primary key of the record to delete
     * @param key2 part of the primary key of the record to delete
     * @return true if deletion successful, false otherwise
     */

    public boolean deleteComposite(int key1, int key2) {
        return false; // Default implementation, override if needed
    }

    /**
     * Retrieves a single record by its primary key, For single PK
     *
     * @param id the primary key
     * @return the entity if found, null otherwise
     */
    public abstract T getById(int id);

    /**
     * Retrieves a single record by its primary key, For composite PK
     *
     * @param id1 part of the primary key
     * @param id2 part of the primary key
     * @return the entity if found, null otherwise
     */
    public T getByIdComposite(int id1, int id2){
        return null; // Default implementation, override if needed
    }

    /**
     * Retrieves all records from the table.
     *
     * @return List of all entities
     */
    public abstract List<T> getAll();

    /**
     * Maps a ResultSet row to an entity object.
     * This method must be implemented by each DAO to handle
     * the specific mapping for its entity type.
     *
     * @param rs the ResultSet positioned at a row
     * @return the mapped entity object
     * @throws SQLException if database access error occurs
     */
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    /**
     * Gets the table name for this DAO.
     *
     * @return the database table name
     */
    protected abstract String getTableName();

    /**
     * Generic search method that executes a query and returns results.
     * Useful for implementing custom search functionality.
     *
     * @param query the SQL query to execute
     * @param params parameters for the prepared statement
     * @return List of entities matching the query
     */
    protected List<T> executeQuery(String query, Object... params) {
        List<T> results = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                T entity = mapResultSetToEntity(rs);
                results.add(entity);
            }

        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return results;
    }

    /**
     * Generic update method for INSERT, UPDATE, DELETE operations.
     *
     * @param query the SQL query to execute
     * @param params parameters for the prepared statement
     * @return number of rows affected
     */
    protected int executeUpdate(String query, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rowsAffected = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt);
        }

        return rowsAffected;
    }

    /**
     * Executes an insert and returns the generated key.
     * Useful for getting auto-generated IDs after insertion.
     *
     * @param query the SQL INSERT query
     * @param params parameters for the prepared statement
     * @return the generated key, or -1 if failed
     */
    protected int executeInsertWithGeneratedKey(String query, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int generatedKey = -1;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedKey = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error executing insert: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return generatedKey;
    }
}