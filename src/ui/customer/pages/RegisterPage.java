package ui.customer.pages;

import dao.CustomerDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Customer;
import ui.PageManager;
import utils.ValidationUtils;

import java.util.Objects;

/**
 * Controller for the registration page.
 *
 * <p>On initialize this controller logs out any existing session and
 * prepares the view. Navigation back to the login page is provided.</p>
 */
public class RegisterPage {

    @FXML
    private ImageView iconReturnView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField addressField;
    @FXML
    private Label errorLabel;

    private final CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    public void initialize() {
        loadImages();
        errorLabel.setText("");
    }

    private void loadImages() {
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/undoWhite.png")));
            iconReturnView.setImage(icon);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleReturn() {
        PageManager.loadPage("login.fxml");
    }

    @FXML
    public void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String address = addressField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || address.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }
        if (!ValidationUtils.isEmailValid(email)) {
            errorLabel.setText("Invalid email format.");
            return;
        }
        if (!ValidationUtils.isPhoneNumberValid(phone)) {
            errorLabel.setText("Phone number must contain only digits.");
            return;
        }
        if (customerDAO.emailExists(email)) {
            errorLabel.setText("This email address is already registered.");
            return;
        }
        if (customerDAO.phoneExists(phone)) {
            errorLabel.setText("This phone number is already registered.");
            return;
        }

        Customer newCustomer = new Customer();
        newCustomer.setName(name);
        newCustomer.setEmail(email);
        newCustomer.setPhoneNumber(phone);
        newCustomer.setPassword(password);
        newCustomer.setAddress(address);

        int customerId = customerDAO.insert(newCustomer);
        if (customerId != -1) {
            PageManager.loadPage("login.fxml", email);
        } else {
            errorLabel.setText("Registration failed. Please try again.");
        }
    }
}
