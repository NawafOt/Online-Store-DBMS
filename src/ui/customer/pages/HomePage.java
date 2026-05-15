package ui.customer.pages;

import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import model.Product;
import model.Session;
import ui.PageManager;

import java.util.List;
import java.util.Objects;

/**
 * Controller for the customer's home page.
 * This class manages the main view for both guests and logged-in customers,
 * displaying products grouped be category and handling navigation to other pages.
 */
public class HomePage {

    @FXML private ImageView iconLogoutView;
    @FXML private ImageView iconCartView;
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;

    @FXML private Accordion categoryAccordion;
    @FXML private TextField searchField;
    @FXML private ListView<Product> searchResultsList;
    @FXML private Label noResultsLabel;
    @FXML private ProgressIndicator loadingSpinner;

    private final ProductDAO productDAO = new ProductDAO();
    private final Session session = Session.getInstance();

    /**
     * This method is automatically called by JavaFX after the FXML file has been loaded.
     * It acts as the entry point for the controller, orchestrating the setup of the page.
     */
    @FXML
    public void initialize() {
        loadImages();
        setupUserSession();

        searchResultsList.setCellFactory(createProductCellFactory());
        searchResultsList.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) PageManager.loadPage("customer/pages/product_details.fxml", newVal);
        });

        // Load initial data (Async)
        loadProductsAsync();
    }

    /**
     * ASYNC LOADING: Fetches categories and products on a background thread
     * so the UI doesn't freeze.
     */
    private void loadProductsAsync() {
        loadingSpinner.setVisible(true);
        categoryAccordion.setVisible(false);

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() {
                // Database call happens here (Background Thread)
                return productDAO.getAllCategories();
            }
        };

        task.setOnSucceeded(event -> {
            // UI Update happens here (JavaFX Thread)
            List<String> categories = task.getValue();
            populateAccordion(categories);
            loadingSpinner.setVisible(false);
            categoryAccordion.setVisible(true);
        });

        task.setOnFailed(event -> {
            loadingSpinner.setVisible(false);
            System.err.println("Failed to load categories.");
        });

        new Thread(task).start();
    }

    private void populateAccordion(List<String> categories) {
        categoryAccordion.getPanes().clear();
        for (String category : categories) {
            List<Product> products = productDAO.getByCategory(category);
            ListView<Product> lv = new ListView<>(FXCollections.observableArrayList(products));
            lv.setCellFactory(createProductCellFactory());
            lv.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
                if (newVal != null) PageManager.loadPage("customer/pages/product_details.fxml", newVal);
            });
            categoryAccordion.getPanes().add(new TitledPane(category, lv));
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            handleClearSearch();
            return;
        }

        loadingSpinner.setVisible(true);
        categoryAccordion.setVisible(false);
        searchResultsList.setVisible(false);
        noResultsLabel.setVisible(false);

        Task<List<Product>> searchTask = new Task<>() {
            @Override
            protected List<Product> call() {
                return productDAO.searchByName(query);
            }
        };

        searchTask.setOnSucceeded(e -> {
            loadingSpinner.setVisible(false);
            List<Product> results = searchTask.getValue();
            if (results.isEmpty()) {
                noResultsLabel.setVisible(true);
            } else {
                searchResultsList.setItems(FXCollections.observableArrayList(results));
                searchResultsList.setVisible(true);
            }
        });

        new Thread(searchTask).start();
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        searchResultsList.setVisible(false);
        noResultsLabel.setVisible(false);
        categoryAccordion.setVisible(true);
    }

    /**
     * Loads the icons used for the buttons on the home page.
     * It dynamically chooses the login or logout icon based on the current session state.
     */
    private void loadImages() {
        try {
            Image iconCart = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/cart.png")));
            iconCartView.setImage(iconCart);

            // Change logout icon to login icon for guests
            if (session.isGuest()) {
                Image iconLogin = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/log-in.png")));
                iconLogoutView.setImage(iconLogin);
            } else {
                Image iconLogout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/log-out.png")));
                iconLogoutView.setImage(iconLogout);
            }
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configures the UI elements based on the user's session status (Guest vs. Customer).
     * It sets the welcome message and changes the logout button to a login button for guests.
     */
    private void setupUserSession() {
        if (session.isGuest()) {
            welcomeLabel.setText("Welcome, Guest");
            logoutButton.setText("Login"); // Change button text for guests
        } else if (session.isCustomer()) {
            welcomeLabel.setText("Welcome, " + session.getCustomerName());
            logoutButton.setText("Logout");
        }
    }

    /**
     * Creates and returns a CellFactory for a ListView of Products.
     * This factory provides the rendering logic for how each Product should be displayed in a list cell.
     * @return A Callback that can be used as a CellFactory.
     */
    private Callback<ListView<Product>, ListCell<Product>> createProductCellFactory() {
        return param -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null); // If the cell is empty or the item is invalid, show nothing.
                } else {
                    // If the item is valid, format the string to show the product name and price.
                    setText(item.getName() + " - $" + String.format("%.2f", item.getUnitPrice()));
                }
            }
        };
    }

    /**
     * Handles the action of clicking the 'Profile' button.
     * If the user is a guest, it navigates to the restricted page.
     * Otherwise, it navigates to the customer's profile page.
     */
    @FXML
    public void handleProfile() {
        if (session.isGuest()) {
            PageManager.loadPage("commons/restricted.fxml");
        } else {
            PageManager.loadPage("customer/pages/profile.fxml");
        }
    }

    /**
     * Handles the action of clicking the 'Logout' or 'Login' button.
     * If the user is a guest, it navigates to the login page.
     * If the user is a customer, it logs them out and then navigates to the login page.
     */
    @FXML
    public void handleLogout() {
        if (session.isGuest()) {
            PageManager.loadPage("login.fxml");
        } else {
            session.logout();
            PageManager.loadPage("login.fxml");
        }
    }

    /**
     * Handles the action of clicking the 'Wish List' button.
     * If the user is a guest, it navigates to the restricted page.
     * Otherwise, it navigates to the customer's wishlist page.
     */
    @FXML
    public void handleWishList() {
        if (session.isGuest()) {
            PageManager.loadPage("commons/restricted.fxml");
        } else {
            PageManager.loadPage("customer/pages/wishlist.fxml");
        }
    }

    /**
     * Handles the action of clicking the 'Orders' button.
     * If the user is a guest, it navigates to the restricted page.
     * Otherwise, it navigates to the customer's orders page.
     */
    @FXML
    public void handleOrders() {
        if (session.isGuest()) {
            PageManager.loadPage("commons/restricted.fxml");
        } else {
            PageManager.loadPage("customer/pages/orders.fxml");
        }
    }

    /**
     * Handles the action of clicking the 'Cart' button.
     * Navigates to the cart page. This is accessible to both guests and customers.
     */
    @FXML
    public void handleCart() {
        PageManager.loadPage("customer/pages/cart.fxml");
    }
}
