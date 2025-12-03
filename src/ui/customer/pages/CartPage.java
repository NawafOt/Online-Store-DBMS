package ui.customer.pages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Controller for the customer's shopping cart page.
 * Displays grouped items from the session cart with quantities.
 */
public class CartPage {

    @FXML
    private ImageView iconReturn;
    @FXML
    private VBox cardContainer;
    @FXML
    private Label totalLabel;
    @FXML
    private Button checkoutButton;
    @FXML
    private Button clearAllButton;

    private final Session session = Session.getInstance();

    /**
     * FXML initialize lifecycle method. Loads images and populates the cart view.
     */
    @FXML
    public void initialize() {
        loadImages();
        loadCartItems();
    }

    /**
     * Fetches items, groups them by product, displays them as cards with quantities, and calculates the total.
     */
    private void loadCartItems() {
        cardContainer.getChildren().clear();
        List<Product> cartItems = session.getCart();

        if (cartItems.isEmpty()) {
            cardContainer.getChildren().add(new Label("Your cart is empty."));
            updateTotal(0.0);
            checkoutButton.setDisable(true);
            clearAllButton.setDisable(true);
            return;
        }

        // Ensure buttons are enabled if cart is not empty
        checkoutButton.setDisable(false);
        clearAllButton.setDisable(false);

        // Group products by their ID and count the occurrences (quantity)
        Map<Product, Long> quantities = cartItems.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Mapper function to extract display strings from a Product and its quantity
        Function<Map.Entry<Product, Long>, String[]> mapper = entry -> {
            Product product = entry.getKey();
            long quantity = entry.getValue();
            return new String[]{
                    product.getName() + " (x" + quantity + ")",
                    "Price: $" + String.format("%.2f", product.getUnitPrice()) + " each",
                    "Subtotal: $" + String.format("%.2f", product.getUnitPrice() * quantity)
            };
        };

        double total = 0.0;
        for (Map.Entry<Product, Long> entry : quantities.entrySet()) {
            addCardForProduct(entry, mapper);
            total += entry.getKey().getUnitPrice() * entry.getValue();
        }
        updateTotal(total);
    }

    /**
     * Creates a card UI node for a given product entry (product + quantity).
     */
    private void addCardForProduct(Map.Entry<Product, Long> entry, Function<Map.Entry<Product, Long>, String[]> mapper) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/commons/Card.fxml"));
            Node cardNode = loader.load();
            CardController<Map.Entry<Product, Long>> controller = loader.getController();

            Product productToRemove = entry.getKey();

            // Define the action for removing all instances of this product from the cart
            List<CardAction> actions = List.of(
                    new CardAction("Remove All", () -> {
                        session.getCart().removeIf(p -> p.equals(productToRemove));
                        loadCartItems(); // Reload the entire cart view
                    })
            );

            controller.setData(entry, mapper, actions);
            cardContainer.getChildren().add(cardNode);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Updates the total price label.
     * @param total The new total to display.
     */
    private void updateTotal(double total) {
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    /**
     * Handles the action of clicking the 'Clear All' button.
     * Clears all items from the session cart and refreshes the view.
     */
    @FXML
    private void handleClearAll() {
        session.getCart().clear();
        loadCartItems();
    }

    /**
     * Handles the action of clicking the 'Checkout' button.
     * Navigates to the checkout page if the user is logged in, otherwise to the restricted page.
     */
    @FXML
    private void handleCheckout() {
        if (session.isGuest()) {
            PageManager.loadPage("commons/restricted.fxml");
        } else {
            PageManager.loadPage("customer/pages/checkout.fxml");
        }
    }

    /**
     * Load small icons used on the page.
     */
    private void loadImages() {
        try {
            Image returnIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/undoWhite.png")));
            iconReturn.setImage(returnIcon);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle navigation back to the home page.
     */
    @FXML
    public void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
