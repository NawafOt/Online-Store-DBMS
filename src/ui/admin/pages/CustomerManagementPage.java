package ui.admin.pages;

import dao.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import ui.PageManager;
import utils.ValidationUtils;

public class CustomerManagementPage {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colAddress;

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private Customer selectedCustomer = null;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCustomers();
        setupTableSelection();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("cid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerTable.setItems(customerList);
    }

    private void loadCustomers() {
        customerList.clear();
        customerList.addAll(customerDAO.getAll());
    }

    private void setupTableSelection() {
        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedCustomer = newVal;
                populateForm(newVal);
            }
        });
    }

    private void populateForm(Customer c) {
        nameField.setText(c.getName());
        emailField.setText(c.getEmail());
        phoneField.setText(c.getPhoneNumber());
        addressField.setText(c.getAddress());
        passwordField.setText(c.getPassword());
    }

    @FXML
    private void handleAdd() {
        if (!validateInput()) return;

        if (customerDAO.emailExists(emailField.getText().trim())) {
            setStatus("Email already exists!", true);
            return;
        }

        Customer c = new Customer();
        c.setName(nameField.getText().trim());
        c.setEmail(emailField.getText().trim());
        c.setPhoneNumber(phoneField.getText().trim());
        c.setAddress(addressField.getText().trim());
        c.setPassword(passwordField.getText().trim());

        if (customerDAO.insert(c) > 0) {
            setStatus("Customer added successfully.", false);
            loadCustomers();
            handleClear();
        } else {
            setStatus("Failed to add customer.", true);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedCustomer == null) {
            setStatus("Select a customer to update.", true);
            return;
        }
        if (!validateInput()) return;

        // Check if email changed and if new email exists
        if (!selectedCustomer.getEmail().equals(emailField.getText().trim()) &&
                customerDAO.emailExists(emailField.getText().trim())) {
            setStatus("New email is already taken.", true);
            return;
        }

        selectedCustomer.setName(nameField.getText().trim());
        selectedCustomer.setEmail(emailField.getText().trim());
        selectedCustomer.setPhoneNumber(phoneField.getText().trim());
        selectedCustomer.setAddress(addressField.getText().trim());
        selectedCustomer.setPassword(passwordField.getText().trim());

        if (customerDAO.update(selectedCustomer)) {
            setStatus("Customer updated successfully.", false);
            loadCustomers();
            handleClear();
        } else {
            setStatus("Failed to update customer.", true);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedCustomer == null) {
            setStatus("Select a customer to delete.", true);
            return;
        }

        if(!utils.Alerts.showConfirmation("Delete Customer")) {
            return;
        }

        if (customerDAO.delete(selectedCustomer.getCid())) {
            setStatus("Customer deleted.", false);
            loadCustomers();
            handleClear();
        } else {
            setStatus("Failed to delete. Customer may have active orders.", true);
        }
    }

    @FXML
    private void handleSearch() {
        String term = searchField.getText().trim();
        if (term.isEmpty()) {
            loadCustomers();
        } else {
            customerList.clear();
            customerList.addAll(customerDAO.searchByName(term));
        }
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        passwordField.clear();
        selectedCustomer = null;
        customerTable.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            setStatus("All fields are required.", true);
            return false;
        }
        if (!ValidationUtils.isEmailValid(emailField.getText())) {
            setStatus("Invalid email format.", true);
            return false;
        }
        if (!utils.ValidationUtils.isPhoneNumberValid(phoneField.getText())) {
            setStatus("Invalid phone number format.", true);
            return false;
        }
        return true;
    }

    private void setStatus(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.getStyleClass().removeAll("error-text", "success-text");
        statusLabel.getStyleClass().add(isError ? "error-text" : "success-text");
    }

    @FXML
    private void handleBack() {
        PageManager.loadPage("admin/pages/adminhome.fxml");
    }
}