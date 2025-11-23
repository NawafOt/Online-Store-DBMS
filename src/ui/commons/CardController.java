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

public class CardController<T> {
    @FXML public VBox root;
    @FXML private Label titleLabel;
    @FXML private Label line1;
    @FXML private Label line2;
    @FXML private MenuButton menuBtn;

    // setData fills labels and builds the menu
    // mapper: you pass a function that turns your domain T into simple strings for display
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

    private void makeMenuButton() {
        menuBtn.getStyleClass().add("primary-btn");
        menuBtn.setPopupSide(javafx.geometry.Side.BOTTOM);
        menuBtn.getStyleClass().addAll("primary-btn", "icon-button");
    }

}

