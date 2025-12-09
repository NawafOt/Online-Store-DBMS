package ui.admin.pages;

import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;
import ui.PageManager;

import java.util.List;

/**
 * Controller for Product Management page.
 * Provides CRUD operations for Product entity.
 */
public class ProductManagementPage {

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField categoryField;
    @FXML private TextField stockField;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    @FXML private ToggleButton viewHiddenToggle;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnRestore;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Integer> colStock;

    private final ProductDAO productDAO = new ProductDAO();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private Product selectedProduct = null;

    @FXML
    public void initialize() {
        setupTableColumns();

        viewHiddenToggle.selectedProperty().addListener((obs, oldVal, isHidden) -> {
            toggleMode(isHidden);
            loadProducts();
        });

        loadProducts();
        setupTableSelection();
    }

    private void toggleMode(boolean isArchiveMode) {
        // 1. Swap Buttons
        btnAdd.setVisible(!isArchiveMode);
        btnAdd.setManaged(!isArchiveMode);

        btnUpdate.setVisible(!isArchiveMode);
        btnUpdate.setManaged(!isArchiveMode);

        btnDelete.setVisible(!isArchiveMode);
        btnDelete.setManaged(!isArchiveMode);

        btnRestore.setVisible(isArchiveMode);
        btnRestore.setManaged(isArchiveMode);

        // 2. Lock/Unlock Text Fields
        nameField.setDisable(isArchiveMode);
        priceField.setDisable(isArchiveMode);
        categoryField.setDisable(isArchiveMode);
        stockField.setDisable(isArchiveMode);

        // Clear selection to avoid confusion
        handleClear();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productTable.setItems(productList);
    }

    private void loadProducts() {
        productList.clear();
        if (viewHiddenToggle.isSelected()) {
            // Mode 1: Load Hidden Products
            productList.addAll(productDAO.getHidden());
            statusLabel.setText("Viewing Archived Products");
            statusLabel.setStyle("-fx-text-fill: orange;");
        } else {
            // Mode 2: Load Active Products
            productList.addAll(productDAO.getAll());
            productTable.setStyle("");
        }
    }

    @FXML
    private void handleRestore() {
        if (selectedProduct == null) {
            setStatus("Select a product to restore", true);
            return;
        }

        selectedProduct.setHidden(false);

        if (productDAO.updateHide(selectedProduct)) {
            setStatus("Product Restored to Shop!", false);
            loadProducts();
        } else {
            setStatus("Restore failed", true);
        }
    }

    private void setupTableSelection() {
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedProduct = newSelection;
                        populateForm(newSelection);
                    }
                }
        );
    }

    private void populateForm(Product product) {
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getUnitPrice()));
        categoryField.setText(product.getCategory());
        stockField.setText(String.valueOf(product.getStock()));
    }

    @FXML
    private void handleAdd() {
        if (!validateInput()) return;

        Product product = new Product();
        product.setName(nameField.getText().trim());
        product.setUnitPrice(Double.parseDouble(priceField.getText().trim()));
        product.setCategory(categoryField.getText().trim());
        product.setStock(Integer.parseInt(stockField.getText().trim()));

        int generatedId = productDAO.insert(product);
        if (generatedId > 0) {
            setStatus("Product added successfully (ID: " + generatedId + ")", false);
            loadProducts();
            clearForm();
        } else {
            setStatus("Failed to add product", true);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedProduct == null) {
            setStatus("Please select a product to update", true);
            return;
        }
        if (!validateInput()) return;

        selectedProduct.setName(nameField.getText().trim());
        selectedProduct.setUnitPrice(Double.parseDouble(priceField.getText().trim()));
        selectedProduct.setCategory(categoryField.getText().trim());
        selectedProduct.setStock(Integer.parseInt(stockField.getText().trim()));

        if (productDAO.update(selectedProduct)) {
            setStatus("Product updated successfully", false);
            loadProducts();
            clearForm();
        } else {
            setStatus("Failed to update product", true);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedProduct == null) {
            setStatus("Please select a product to delete", true);
            return;
        }

        if(!utils.Alerts.showConfirmation("Delete Product")) {
            return;
        }

        if (productDAO.delete(selectedProduct.getPid())) {
            setStatus("Product deleted successfully", false);
            loadProducts();
            clearForm();
        } else {
            setStatus("Failed to delete product", true);
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadProducts();
            return;
        }

        productList.clear();
        List<Product> results = productDAO.searchByNameAdmin("%" + searchTerm + "%");
        productList.addAll(results);
        setStatus("Found " + results.size() + " product(s)", false);
    }

    @FXML
    private void handleClear() {
        clearForm();
        searchField.clear();
        loadProducts();
    }

    private void clearForm() {
        nameField.clear();
        priceField.clear();
        categoryField.clear();
        stockField.clear();
        selectedProduct = null;
        productTable.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            setStatus("Product name is required", true);
            return false;
        }
        if (!utils.ValidationUtils.isPositiveDouble(priceField.getText())) {
            setStatus("Price must be a valid positive number.", true);
            return false;
        }

        if (!utils.ValidationUtils.isPositiveInteger(stockField.getText())) {
            setStatus("Stock must be a valid positive whole number.", true);
            return false;
        }
        return true;
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("error-text", "success-text");
        statusLabel.getStyleClass().add(isError ? "error-text" : "success-text");
    }

    @FXML
    private void handleBack() {
        PageManager.loadPage("admin/pages/adminhome.fxml");
    }
}