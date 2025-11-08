import config.DatabaseConfig;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully!");

            if (DatabaseConfig.testConnection()) {
                System.out.println("✓ Everything is working!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found!");
            System.err.println("Did you add mysql-connector JAR to your project?");
        }
    }
}