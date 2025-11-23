package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.PageManager;

import java.util.Objects;

/**
 * Controller for the customer's profile page.
 *
 * <p>Handles loading of profile-related icons and navigation back to home.</p>
 */
public class ProfilePage {

    @FXML
    private ImageView iconReturn;
    @FXML
    private ImageView iconProfileCircle;

    /**
     * FXML initialization: load images and set up the view.
     */
    @FXML
    public void initialize() {
        loadImages();

    }

    /**
     * Load icons used on the profile page. Errors print to stderr.
     */
    private void loadImages() {
        try {
            Image icon1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/undoWhite.png")));
            iconReturn.setImage(icon1);
            Image icon2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/profile-circle.png")));
            iconProfileCircle.setImage(icon2);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }
    }

    /**
     * Navigate back to the customer home page.
     */
    @FXML
    public void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
