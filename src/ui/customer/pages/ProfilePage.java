package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.PageManager;

import java.util.Objects;

public class ProfilePage {

    @FXML
    private ImageView iconReturn;
    @FXML
    private ImageView iconProfileCircle;

    @FXML
    public void initialize() {
        loadImages();

    }

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

    @FXML
    public void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
