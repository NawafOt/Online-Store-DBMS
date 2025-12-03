package ui.customer.pages;

import dao.ProductDAO;
import dao.WishlistDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Product;
import model.Session;
import ui.PageManager;
import ui.commons.CardAction;
import ui.commons.CardController;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Controller for the customer's wishlist page.
 * Displays products from the user's wishlist and allows removal.
 */
public class WishlistPage {

    @FXML
    private ImageView iconReturn;
    @FXML
    private VBox cardContainer;

    private final ProductDAO productDAO = new ProductDAO();
    private final WishlistDAO wishlistDAO = new WishlistDAO();
    private final int customerId = Session.getInstance().getCustomerId();

    @FXML
    public void initialize() {
        loadImages();
        loadWishlistItems();
    }

    /**
     * Fetches the user's wishlisted products and displays them as cards.
     */
    private void loadWishlistItems() {
        cardContainer.getChildren().clear();

        // Get the list of Product objects from the user's wishlist.
        List<Product> wishlistProducts = productDAO.getProductsByWishlistCustomerId(customerId);

        if (wishlistProducts.isEmpty()) {
            cardContainer.getChildren().add(new Label("Your wishlist is empty."));
            return;
        }

        // This function maps a Product object to the strings needed for the card.
        Function<Product, String[]> mapper = product -> new String[]{
                product.getName(),
                "Price: $" + String.format("%.2f", product.getUnitPrice()),
                "Category: " + product.getCategory()
        };

        for (Product product : wishlistProducts) {
            addCardForProduct(product, mapper);
        }
    }

    /**
     * Creates a card for a given product and adds it to the container.
     * @param product The product to display.
     * @param mapper The function to extract string data for the card.
     */
    private void addCardForProduct(Product product, Function<Product, String[]> mapper) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/commons/Card.fxml"));
            Node cardNode = loader.load();
            CardController<Product> controller = loader.getController();

            // Define the action for removing an item from the wishlist.
            List<CardAction> actions = List.of(
                    new CardAction("Remove", () -> {
                        // Use the product ID and session customer ID to remove the link.
                        if (wishlistDAO.removeFromWishlist(customerId, product.getPid())) {
                            // On successful removal, remove the card from the UI.
                            cardContainer.getChildren().remove(cardNode);
                        } else {
                            System.err.println("Failed to remove item from wishlist.");
                        }
                    })
            );

            controller.setData(product, mapper, actions);
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
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
