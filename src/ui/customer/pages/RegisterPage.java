package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Session;
import ui.PageManager;

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

    /**
     * FXML initialize: load icons and ensure no active session remains.
     */
    @FXML
    public void initialize() {
        loadImages();
        Session.getInstance().logout();
    }

    /**
     * Load small icons used by the registration view.
     */
    private void loadImages() {
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/undoWhite.png")));
            iconReturnView.setImage(icon);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }
    }

    /**
     * Navigate back to the login page.
     */
    public void handleReturn() {
        PageManager.loadPage("login.fxml");
    }
}
