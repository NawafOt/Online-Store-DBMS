package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.util.List;
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
}

