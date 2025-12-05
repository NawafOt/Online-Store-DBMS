package ui.admin.pages;

import dao.ShippingDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ShippingCompany;
import ui.PageManager;

public class ShippingManagementPage {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField costField;
    @FXML private Label statusLabel;

    @FXML private TableView<ShippingCompany> shippingTable;
    @FXML private TableColumn<ShippingCompany, Integer> colId;
    @FXML private TableColumn<ShippingCompany, String> colName;
    @FXML private TableColumn<ShippingCompany, String> colPhone;
    @FXML private TableColumn<ShippingCompany, Double> colCost;

    private final ShippingDAO shippingDAO = new ShippingDAO();
    private final ObservableList<ShippingCompany> shippingList = FXCollections.observableArrayList();
    private ShippingCompany selectedCompany = null;

    @FXML
    public void initialize() {
        setupTable();
        loadData();
        setupSelection();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("sid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        shippingTable.setItems(shippingList);
    }

    private void loadData() {
        shippingList.clear();
        shippingList.addAll(shippingDAO.getAll());
    }

    private void setupSelection() {
        shippingTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedCompany = newVal;
                nameField.setText(newVal.getName());
                phoneField.setText(newVal.getPhoneNumber());
                costField.setText(String.valueOf(newVal.getCost()));
            }
        });
    }

    @FXML
    private void handleAdd() {
        if (!validateInput()) return;

        ShippingCompany sc = new ShippingCompany();
        sc.setName(nameField.getText().trim());
        sc.setPhoneNumber(phoneField.getText().trim());
        sc.setCost(Double.parseDouble(costField.getText().trim()));

        if (shippingDAO.insert(sc) > 0) {
            setStatus("Company added.", false);
            loadData();
            handleClear();
        } else {
            setStatus("Failed to add company.", true);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedCompany == null) {
            setStatus("Select a company to update.", true);
            return;
        }
        if (!validateInput()) return;

        selectedCompany.setName(nameField.getText().trim());
        selectedCompany.setPhoneNumber(phoneField.getText().trim());
        selectedCompany.setCost(Double.parseDouble(costField.getText().trim()));

        if (shippingDAO.update(selectedCompany)) {
            setStatus("Company updated.", false);
            loadData();
            handleClear();
        } else {
            setStatus("Failed to update.", true);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedCompany == null) {
            setStatus("Select a company to delete.", true);
            return;
        }

        if (shippingDAO.delete(selectedCompany.getSid())) {
            setStatus("Company deleted.", false);
            loadData();
            handleClear();
        } else {
            setStatus("Deletion failed (Cannot delete if used in existing orders).", true);
        }
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        phoneField.clear();
        costField.clear();
        selectedCompany = null;
        shippingTable.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    private boolean validateInput() {
        if (!utils.ValidationUtils.isPhoneNumberValid(phoneField.getText())) {
            setStatus("Invalid phone number format (digits only).", true);
            return false;
        }

        if (!utils.ValidationUtils.isPositiveDouble(costField.getText())) {
            setStatus("Shipping cost must be a valid positive number.", true);
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