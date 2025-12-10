package ui.customer.pages;

import dao.WishlistDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.Product;
import model.Session;
import ui.DataReceiver;
import ui.PageManager;

import java.util.ArrayList;

/**
 * Controller for the product details page.
 * Displays detailed information and allows adding a specified quantity to the cart or wishlist.
 */
public class ProductDetailsPage implements DataReceiver {

    @FXML
    private Label productNameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label stockLabel;
    @FXML
    private Spinner<Integer> quantitySpinner;
    @FXML
    private Button addToCartButton;
    @FXML
    private Button addToWishlistButton;
    @FXML
    private Label feedbackLabel;

    private Product currentProduct;
    private final Session session = Session.getInstance();
    private final WishlistDAO wishlistDAO = new WishlistDAO();

    @Override
    public void receiveData(Object data) {
        if (data instanceof Product) {
            currentProduct = (Product) data;
            productNameLabel.setText(currentProduct.getName());
            priceLabel.setText(String.format("$%.2f", currentProduct.getUnitPrice()));
            categoryLabel.setText(currentProduct.getCategory());
            stockLabel.setText(String.valueOf(currentProduct.getStock()));

            // Configure the quantity spinner
            if (currentProduct.getStock() > 0) {
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, currentProduct.getStock() - currentProduct.getCount(), 1);
                quantitySpinner.setValueFactory(valueFactory);
                addToCartButton.setDisable(false);
            } else {
                // If no stock, disable spinner and add to cart button
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0);
                quantitySpinner.setValueFactory(valueFactory);
                quantitySpinner.setDisable(true);
                addToCartButton.setDisable(true);
                feedbackLabel.setText("This item is out of stock.");
            }

            // Guests cannot add items to a wishlist.
            if (session.isGuest()) {
                addToWishlistButton.setDisable(true);
            }
        }
    }

    /**
     * Handles adding the selected quantity of the product to the session cart.
     */
    @FXML
    private void handleAddToCart() {
        if (currentProduct != null) {
            int quantity = quantitySpinner.getValue();
            ArrayList<Product> cart = session.getCart();

            if (cart.contains(currentProduct)) {
                int index = cart.indexOf(currentProduct);
                cart.get(index).setCount(currentProduct.getCount() + quantity);
            } else {
                cart.add(currentProduct);
                currentProduct.setCount(quantity);
            }

            feedbackLabel.setText(quantity + " x '" + currentProduct.getName() + "' added to your cart!");
        }
    }

    /**
     * Handles the action of clicking the 'Add to Wishlist' button.
     * Adds the current product to the logged-in customer's wishlist in the database.
     */
    @FXML
    private void handleAddToWishlist() {
        if (currentProduct != null && session.isCustomer()) {
            int customerId = session.getCustomerId();
            int productId = currentProduct.getPid();

            if (wishlistDAO.addToWishlist(customerId, productId)) {
                feedbackLabel.setText("'" + currentProduct.getName() + "' was added to your wishlist!");
            } else {
                feedbackLabel.setText("This item is already in your wishlist.");
            }
        }
    }

    /**
     * Handles the action of clicking the 'Back to Home' button.
     * Navigates the user back to the main home page.
     */
    @FXML
    private void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
