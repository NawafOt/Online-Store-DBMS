package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.PageManager;

import java.util.Objects;

public class HomePage {

    @FXML
    private ImageView iconLogoutView;
    @FXML
    private ImageView iconCartView;

    @FXML
    public void initialize() {
        try {
            Image iconLogout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/log-out.png")));
            iconLogoutView.setImage(iconLogout);
            Image iconCart = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/cart.png")));
            iconCartView.setImage(iconCart);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }
    }

    @FXML
    public void handleProfile() {
        PageManager.loadPage("customer/pages/profile.fxml");
    }

    public void handleLogout() {
        PageManager.loadPage("login.fxml");
    }
}
