package ui.customer.pages;

import dao.CustomerDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Customer;
import model.Session;
import ui.PageManager;
import utils.ValidationUtils;

import java.util.Objects;

/**
 * Controller for the customer's profile page.
 * Allows viewing and editing of the logged-in customer's profile information.
 */
public class ProfilePage {

    @FXML
    private ImageView iconReturn;
    @FXML
    private ImageView iconProfileCircle;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label feedbackLabel;

    private final Session session = Session.getInstance();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private boolean isEditMode = false;

    /**
     * FXML initialization: load images and populate the user's profile data.
     */
    @FXML
    public void initialize() {
        loadImages();
        populateProfileData();
        feedbackLabel.setText("");
    }

    /**
     * Populates the text fields with the current customer's information from the session.
     */
    private void populateProfileData() {
        Customer currentCustomer = session.getCurrentCustomer();
        if (currentCustomer != null) {
            nameField.setText(currentCustomer.getName());
            emailField.setText(currentCustomer.getEmail());
            phoneField.setText(currentCustomer.getPhoneNumber());
            addressField.setText(currentCustomer.getAddress());
        }
    }

    /**
     * Load icons used on the profile page.
     */
    private void loadImages() {
        try {
            Image icon1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/undoWhite.png")));
            iconReturn.setImage(icon1);
            Image icon2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/profile-circle.png")));
            iconProfileCircle.setImage(icon2);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Toggles the edit mode for the profile fields.
     * Makes the fields editable and swaps the 'Edit' button for a 'Save' button.
     */
    @FXML
    private void handleEdit() {
        isEditMode = true;
        setEditMode(true);
        feedbackLabel.setText("You can now edit your profile.");
    }

    /**
     * Saves the updated profile information to the database and session.
     * After saving, it reverts the UI back to view mode.
     */
    @FXML
    private void handleSave() {
        Customer currentCustomer = session.getCurrentCustomer();
        if (currentCustomer == null) {
            feedbackLabel.setText("Error: No customer session found.");
            return;
        }

        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();

        if (nameField.getText().isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || addressField.getText().isEmpty()) {
            feedbackLabel.setText("Error: All fields are required.");
            return;
        }
        if (!ValidationUtils.isEmailValid(newEmail)) {
            feedbackLabel.setText("Invalid email format.");
            return;
        }
        if (!ValidationUtils.isPhoneNumberValid(newPhone)) {
            feedbackLabel.setText("Phone number must contain only digits.");
            return;
        }

        // Check for uniqueness only if the email or phone has been changed.
        if (!newEmail.equals(currentCustomer.getEmail()) && customerDAO.emailExists(newEmail)) {
            feedbackLabel.setText("Error: This email address is already in use.");
            return;
        }
        if (!newPhone.equals(currentCustomer.getPhoneNumber()) && customerDAO.phoneExists(newPhone)) {
            feedbackLabel.setText("Error: This phone number is already in use.");
            return;
        }

        // Proceed
        currentCustomer.setName(nameField.getText());
        currentCustomer.setEmail(newEmail);
        currentCustomer.setPhoneNumber(newPhone);
        currentCustomer.setAddress(addressField.getText());

        // Save the updated customer to the database.
        if (customerDAO.update(currentCustomer)) {
            // If the database update is successful, update the session.
            session.updateCustomer(currentCustomer);
            feedbackLabel.setText("Profile updated successfully!");
        } else {
            feedbackLabel.setText("Error: Could not update profile.");
        }

        isEditMode = false;
        setEditMode(false);
    }

    /**
     * Helper method to switch the UI between view and edit mode.
     * @param editable True to enable editing, false to disable.
     */
    private void setEditMode(boolean editable) {
        nameField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);
        addressField.setEditable(editable);

        saveButton.setVisible(editable);
        editButton.setVisible(!editable);
    }

    /**
     * Navigate back to the customer home page.
     */
    @FXML
    public void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
