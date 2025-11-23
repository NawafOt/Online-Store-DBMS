package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Product;
import model.Session;
import ui.commons.CardAction;
import ui.commons.CardController;
import ui.PageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import java.util.Objects;

public class CartPage {
    @FXML private ImageView iconReturn;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox cardContainer;
    private final ArrayList<Product> cart = Session.getInstance().getCart();
    private final int customerID = Session.getInstance().getCustomerId();


    @FXML
    public void initialize() {
        loadImages();

        cart.add(new Product(30, "Mussolini", 320000, "Dictator", 19));
        cart.add(new Product(1935, "Adolf Hitler", 2000, "War Criminals", 80));
        cart.add(new Product(50, "Hirohito", 2000, " Holds the \"Mandate of Heaven\"..?", 1));


        //TODO UNCOMMENT THIS IF DB-CONNECTION IS ESTABLISHED
        //orderList.addAll(orderDAO.getByCustomerId(customerID)); //just add all the damn elements

        Function<Product, String[]> prodMap = getProductFunction();

        for (Product product : cart)
            addCardForProduct(product, prodMap);

    }


    private Function<Product, String[]> getProductFunction() {
        return product -> new String[]{
                product.getName(),
                String.format("%.2f", product.getUnitPrice()),
                product.getCategory()
        };
    }

    private void addCardForProduct(Product prod, Function<Product, String[]> mapper) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/commons/Card.fxml"));
            Node cardNode = loader.load(); //card nodes we will insert tinto the scroll pane
            CardController<Product> controller = loader.getController();

            // define unfavorite for this card type.
            List<CardAction> actions = List.of(
                    new CardAction("Remove from Cart", () -> {
                        cart.remove(prod);
                        cardContainer.getChildren().remove(cardNode); //remove the current card
                    })
            );

            // initial fill
            controller.setData(prod, mapper, actions);

            cardContainer.getChildren().add(cardNode);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void loadImages() {
        try {
            Image returnIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/undoWhite.png")));
            iconReturn.setImage(returnIcon);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }
    }


    @FXML
    public void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
