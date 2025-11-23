package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Session;
import ui.PageManager;

import java.util.Objects;

/**
 * Controller for the customer's home page.
 *
 * <p>Provides navigation actions (profile, wishlist, orders, cart)
 * and handles loading of simple UI icons.</p>
 */
public class HomePage {

    @FXML
    private ImageView iconLogoutView;
    @FXML
    private ImageView iconCartView;

    /**
     * FXML initialize method. Loads icons and performs any startup work.
     */
    @FXML
    public void initialize() {
        loadImages();
    }

    /**
     * Load small icon images used by the page.
     */
    private void loadImages() {
        try {
            Image iconLogout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/log-out.png")));
            iconLogoutView.setImage(iconLogout);
            Image iconCart = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/cart.png")));
            iconCartView.setImage(iconCart);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }
    }

    /**
     * Navigate to the user's profile page.
     */
    @FXML
    public void handleProfile() {
        PageManager.loadPage("customer/pages/profile.fxml");
    }

    /**
     * Log out the current session and navigate to the login page.
     */
    public void handleLogout() {
        PageManager.loadPage("login.fxml");
        Session.getInstance().logout();
    }

    /**
     * Navigate to the wishlist page.
     */
    public void handleWishList() {
        PageManager.loadPage("customer/pages/wishlist.fxml");
    }

    /**
     * Navigate to the orders page.
     */
    public void handleOrders() {
        PageManager.loadPage("customer/pages/orders.fxml");
    }

    /**
     * Navigate to the cart page.
     */
    public void handleCart() {
        PageManager.loadPage("customer/pages/cart.fxml");
    }
}
