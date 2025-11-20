package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class RegisterPage {

    @FXML
    private ImageView iconReturnView;

    @FXML
    public void initialize() {
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/undoWhite.png")));
            iconReturnView.setImage(icon);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }
    }
}
