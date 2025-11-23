package ui.customer.pages;

import dao.ProductDAO;
import dao.WishlistDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import model.Product;
import model.Session;
import ui.PageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import java.util.Objects;

public class WishlistPage {
    @FXML private ImageView iconReturn;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox cardContainer;
    private final ArrayList<Product> wishListedProds = new ArrayList<>();
    private static final ProductDAO productDAO = new ProductDAO();
    private static final WishlistDAO wishlistDAO = new WishlistDAO();
    private final int customerID = Session.getInstance().getCustomerId();


    @FXML
    public void initialize() {
        loadImages();

        wishListedProds.add(new Product(10, "Phone", 2000, "Electronic", 15));
        wishListedProds.add(new Product(30, "Terrible food", 120, "food", 19));
        wishListedProds.add(new Product(10, "Adolf Hitler", 2000, "War Criminals", 80));

        /*wishlistDAO.getByCustomerId(customerID).stream()
                .map(wishElem -> productDAO.getById(wishElem.getProductId()))
                .forEach(wishListedProds::add); //just add all the damn elements
                TODO FINISH THIIISSSS
         */

        Function<Product, String[]> prodMap = getProductFunction();

        for (Product p : wishListedProds)
            addCardForProduct(p, prodMap);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/customer/pages/Card.fxml"));
            Node cardNode = loader.load(); //card nodes we will insert tinto the scroll pane
            CardController<Product> controller = loader.getController();

            // define unfavorite for this card type.
            List<CardAction> actions = List.of(
                    new CardAction("Remove from Wishlist", () -> {
                        //wishlistDAO.deleteComposite(customerID, prod.getPid()); //TODO UNCOMMENT THIS!! delete from DB
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
