package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Session;
import ui.PageManager;

import java.util.Objects;

public class RegisterPage {

    @FXML
    private ImageView iconReturnView;

    @FXML
    public void initialize() {
        loadImages();
        Session.getInstance().logout();
    }

    private void loadImages() {
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/undoWhite.png")));
            iconReturnView.setImage(icon);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }
    }

    public void handleReturn() {
        PageManager.loadPage("login.fxml");
    }
}
