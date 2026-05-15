package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connections for the application.
 * Reads configuration from database.properties file.
 *
 * This class uses the Singleton pattern to ensure consistent
 * database configuration across the application.
 */
public class DatabaseConfig {

    private static Properties properties = new Properties();
    private static boolean isInitialized = false;

    // Database connection parameters
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;

    /**
     * Private constructor to prevent instantiation.
     * Loads database properties from file.
     */
    private DatabaseConfig() {
        loadProperties();
    }

    /**
     * Loads database configuration from database.properties file.
     * This method is called automatically when first connection is requested.
     */
    private static void loadProperties() {
        if (isInitialized) {
            return;
        }

        try {
            // Try to load from resources folder
            FileInputStream input = new FileInputStream("resources/database.properties");
            properties.load(input);

            // Build connection URL from properties
            String host = properties.getProperty("db.host");
            String port = properties.getProperty("db.port");
            String dbName = properties.getProperty("db.name");

            // Construct JDBC URL
            DB_URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

            DB_USER = properties.getProperty("db.user");
            DB_PASSWORD = properties.getProperty("db.password");
            DB_DRIVER = properties.getProperty("db.driver");

            // Load JDBC driver
            Class.forName(DB_DRIVER);

            isInitialized = true;
            System.out.println("Database configuration loaded successfully");

            input.close();

        } catch (IOException e) {
            System.err.println("ERROR: Could not find database.properties file!");
            System.err.println("Please create resources/database.properties from the example file");
            System.err.println("Details: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Database driver not found!");
            System.err.println("Make sure MySQL Connector JAR is in your project");
            System.err.println("Details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Establishes and returns a connection to the database.
     *
     * @return Connection object connected to the database
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        // Load properties on first call
        if (!isInitialized) {
            loadProperties();
        }

        // Establish and return connection
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Tests the database connection.
     * Useful for debugging and verification.
     *
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection test successful!");
                System.out.println("Connected to: " + conn.getMetaData().getDatabaseProductName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed!");
            System.err.println("Details: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}