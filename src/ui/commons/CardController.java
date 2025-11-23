package ui.commons;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Controller for a generic card UI component.
 *
 * <p>The card displays a title and two lines of text and can attach
 * a dropdown menu with actions. The controller is generic over T;
 * callers provide a mapper to convert a T to a String[] with up to
 * three display lines: title, line1, line2.</p>
 *
 * @param <T> domain object type used to populate the card
 */
public class CardController<T> {
    @FXML public VBox root;
    @FXML private Label titleLabel;
    @FXML private Label line1;
    @FXML private Label line2;
    @FXML private MenuButton menuBtn;

    /**
     * Populate the card's labels and build its menu.
     *
     * @param data    the domain object for this card
     * @param mapper  function that maps the domain object to a String array:
     *                [title, line1, line2] (elements may be missing)
     * @param actions list of card actions to show in the menu
     */
    public void setData(T data, Function<T, String[]> mapper, List<CardAction> actions) {
        loadImages();

        String[] parts = mapper.apply(data); // expecting [title, line1, line2]
        titleLabel.setText(parts.length>0 ? parts[0] : "");
        line1.setText(parts.length>1 ? parts[1] : "");
        line2.setText(parts.length>2 ? parts[2] : "");

        menuBtn.getItems().clear(); //erase observable list se we can replace it
        for (CardAction a : actions) {
            MenuItem mi = new MenuItem(a.text); //get the actions and place them within the menu drop downbbutton
            mi.setOnAction(e -> a.action.run());
            menuBtn.getItems().add(mi);
        }
    }

    /**
     * Load icon images used by the card. Safe to call multiple times.
     * Any image-loading failures are handled by logging to stderr.
     */
    private void loadImages() {
        try {
            Image icon = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/images/icons/menu-icon.png")
            ));
            ImageView iconView = new ImageView(icon);
            iconView.setFitWidth(20);
            iconView.setFitHeight(20);
            iconView.setPreserveRatio(true);

            menuBtn.setGraphic(iconView);

            makeMenuButton();
        } catch (Exception e) {
            System.err.println(e + ": image not found");
        }
    }

    /**
     * Apply CSS classes and popup configuration to the menu button.
     */
    private void makeMenuButton() {
        menuBtn.getStyleClass().add("primary-btn");
        menuBtn.setPopupSide(javafx.geometry.Side.BOTTOM);
        menuBtn.getStyleClass().addAll("primary-btn", "icon-button");
    }

}
