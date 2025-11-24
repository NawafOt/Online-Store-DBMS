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

    @Override
    public void receiveData(Object data) {
        if (data instanceof String) {
            emailField.setText((String) data);
        }
    }

    /**
     * Continue to the application as an authenticated customer (placeholder).
     * Navigates to the customer home page.
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
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
