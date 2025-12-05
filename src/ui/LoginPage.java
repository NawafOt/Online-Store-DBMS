package ui;

import dao.CustomerDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Customer;
import model.Session;

/**
 * Controller for the login view.
 *
 * <p>Contains simple navigation handlers used by the login screen.
 * Authentication logic is out of scope for this controller.</p>
 */
public class LoginPage implements DataReceiver {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final CustomerDAO customerDAO = new CustomerDAO();

    // HARDCODED ADMIN CREDENTIALS
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Override
    public void receiveData(Object data) {
        if (data instanceof String) {
            emailField.setText((String) data);
        }
    }

    /**
     * Attempts to log in.
     * 1. Checks if inputs match Admin credentials.
     * 2. If not, checks database for Customer credentials.
     */
    @FXML
    public void handleContinue() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty()) {
            errorLabel.setText("Email cannot be empty.");
            return;
        }

        if(password.isEmpty()){
            errorLabel.setText("Password cannot be empty");
            return;
        }

        // ADMIN LOGIN CHECK
        if (ADMIN_USERNAME.equals(email) && ADMIN_PASSWORD.equals(password)) {
            // Log in as Admin
            Session.getInstance().loginAsAdmin(1, "Administrator");
            System.out.println("Login successful: Admin Mode");

            // Navigate to Admin Dashboard
            PageManager.loadPage("admin/pages/adminhome.fxml");
            return;
        }

        Customer customer = customerDAO.authenticate(email, password);

        if (customer != null) {
            Session.getInstance().loginAsCustomer(customer);
            PageManager.loadPage("customer/pages/home.fxml");
        } else {
            errorLabel.setText("Invalid email or password.");
        }
    }

    /**
     * Navigate to the registration page.
     */
    @FXML
    public void handleRegister() {
        PageManager.loadPage("customer/pages/register.fxml");
    }

    /**
     * Handles guest access. Navigates to the home page with the default GUEST session.
     */
    @FXML
    public void handleGuest() {
        // Ensure session is cleared for guest mode
        Session.getInstance().logout();
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
