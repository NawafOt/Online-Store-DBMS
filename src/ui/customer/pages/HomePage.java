package ui.customer.pages;

import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    @FXML
    private ImageView iconLogoutView;
    @FXML
    private ImageView iconCartView;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Accordion categoryAccordion;

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
        loadProductsByCategory();
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
     * Fetches products from the database and groups them by category into an Accordion.
     * Each category gets its own TitledPane containing a ListView of products.
     */
    private void loadProductsByCategory() {
        List<String> categories = productDAO.getAllCategories();

        for (String category : categories) {
            List<Product> productsInCategory = productDAO.getByCategory(category);
            // Convert the standard List into a JavaFX ObservableList, which the ListView can watch for changes.
            ObservableList<Product> observableProducts = FXCollections.observableArrayList(productsInCategory);

            // Create a new ListView for this category's products.
            ListView<Product> productListView = new ListView<>(observableProducts);
            productListView.setCellFactory(createProductCellFactory());

            // Add a listener to handle clicks on products in the list.
            productListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    // When a product is selected, navigate to the details page and pass the product object.
                    PageManager.loadPage("customer/pages/product_details.fxml", newSelection);
                }
            });

            TitledPane titledPane = new TitledPane(category, productListView);
            categoryAccordion.getPanes().add(titledPane);
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
