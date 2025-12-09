package ui.customer.pages;

import dao.OrderDAO;
import dao.OrderProductDAO;
import dao.PaymentDAO;
import dao.ProductDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.*;
import ui.PageManager;
import ui.commons.CardAction;
import ui.commons.CardController;
import utils.Alerts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Controller for displaying a customer's order history using filtered Cards.
 */
public class OrdersPage {

    @FXML private ImageView iconReturn;
    @FXML private VBox cardContainer;
    @FXML private ToggleButton btnActive;
    @FXML private ToggleButton btnPast;

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderProductDAO orderProductDAO = new OrderProductDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final int customerId = Session.getInstance().getCustomerId();

    @FXML
    public void initialize() {
        loadImages();
        setupToggles();
        loadOrders();
    }

    private void setupToggles() {
        ToggleGroup toggleGroup = new ToggleGroup();
        btnActive.setToggleGroup(toggleGroup);
        btnPast.setToggleGroup(toggleGroup);

        // Default to Active
        btnActive.setSelected(true);

        // Reload orders when selection changes
        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadOrders();
            } else {
                // Prevent unselecting both; force one to remain selected
                oldVal.setSelected(true);
            }
        });
    }

    private void loadOrders() {
        cardContainer.getChildren().clear();

        // 1. Fetch all orders
        List<Order> allOrders = orderDAO.getByCustomerId(customerId);

        // 2. Filter based on selected Tab
        boolean showActive = btnActive.isSelected();
        List<Order> filteredOrders = allOrders.stream().filter(order -> {
            String s = order.getStatus();
            if (showActive) {
                // Active = Pending or Shipped
                return s.equalsIgnoreCase("PENDING") || s.equalsIgnoreCase("SHIPPING");
            } else {
                // Past = Delivered or Cancelled
                return s.equalsIgnoreCase("DELIVERED") || s.equalsIgnoreCase("CANCELLED");
            }
        }).toList();

        // 3. Handle Empty State
        if (filteredOrders.isEmpty()) {
            Label emptyLabel = new Label(showActive ? "No active orders." : "No past orders found.");
            emptyLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 14px;");
            cardContainer.getChildren().add(emptyLabel);
            return;
        }

        // 4. Create Cards
        for (Order order : filteredOrders) {
            addCardForOrder(order);
        }
    }

    private void addCardForOrder(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/commons/Card.fxml"));
            Node cardNode = loader.load();
            CardController<Order> controller = loader.getController();

            Function<Order, String[]> mapper = o -> {
                Payment payment = paymentDAO.getById(o.getOid());
                double total = (payment != null) ? payment.getTotalAmount() : 0.0;

                return new String[]{
                        "Order #" + o.getOid(), // Title
                        "Date: " + o.getDate() + "  |  Status: " + o.getStatus(), // Line 1
                        "Total: $" + String.format("%.2f", total) // Line 2
                };
            };

            // Actions: Define what happens when clicking the menu
            List<CardAction> actions = new ArrayList<>();

            // 1. View Items (Always available)
            actions.add(new CardAction("View Items", () -> showOrderDetails(order)));

            // 2. Cancel Order (Only if PENDING)
            if (order.getStatus().equalsIgnoreCase("PENDING")) {
                actions.add(new CardAction("Cancel Order", () -> handleCancel(order)));
            }

            // 3. Re-order (Only for Past Orders)
            if (order.getStatus().equalsIgnoreCase("DELIVERED") || order.getStatus().equalsIgnoreCase("CANCELLED")) {
                actions.add(new CardAction("Re-order Items", () -> handleReorder(order)));
            }

            controller.setData(order, mapper, actions);
            cardContainer.getChildren().add(cardNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCancel(Order order) {
        // 1. Validation
        if (!order.getStatus().equalsIgnoreCase("PENDING")) {
            showAlert("Error", "Only pending orders can be cancelled.");
            return;
        }

        // 2. Confirmation
        boolean confirmed = Alerts.showConfirmation("Cancel Order #" + order.getOid());

        if (confirmed) {
            // Get all items in this order
            List<OrderProduct> items = orderProductDAO.getByOrderIdWithDetails(order.getOid());

            // Loop through and put them back
            for (OrderProduct op : items) {
                productDAO.increaseStock(op.getProductId(), op.getQuantity());
            }

            // 3. Update Status to CANCELLED
            if (orderDAO.updateStatus(order.getOid(), "CANCELLED")) {
                showAlert("Success", "Order has been cancelled and items returned to stock.");
                loadOrders();
            } else {
                showAlert("Error", "Failed to cancel order.");
            }
        }
    }

    private void handleReorder(Order order) {
        List<OrderProduct> oldItems = orderProductDAO.getByOrderIdWithDetails(order.getOid());

        if (oldItems.isEmpty()) {
            showAlert("Error", "This order has no items to re-order.");
            return;
        }

        int totalItemsAdded = 0;
        List<String> reportLog = new ArrayList<>();

        for (OrderProduct op : oldItems) {
            // 1. Get the CURRENT product details
            Product currentProduct = productDAO.getById(op.getProductId());

            // 2. Check validity
            if (currentProduct == null || currentProduct.isHidden()) {
                reportLog.add(op.getProductName() + " (No longer sold)");
                continue;
            }

            int originalQty = op.getQuantity();
            int currentStock = currentProduct.getStock();

            // 3. Try to add original quantity, otherwise take what's left
            if (currentStock <= 0) {
                reportLog.add(currentProduct.getName() + " (Out of Stock)");
            } else {
                // If we want 5 but have 3, we take 3. If we have 10, we take 5.
                int quantityToAdd = Math.min(originalQty, currentStock);

                for (int i = 0; i < quantityToAdd; i++) {
                    Session.getInstance().getCart().add(currentProduct);
                }

                totalItemsAdded += quantityToAdd;

                // Log specific outcomes
                if (quantityToAdd < originalQty) {
                    reportLog.add(currentProduct.getName() + ": Added " + quantityToAdd + " (Only " + currentStock + " left in stock)");
                }
            }
        }

        // 4. Final Feedback
        if (totalItemsAdded > 0) {
            String msg = "Added " + totalItemsAdded + " items to your cart.";

            if (!reportLog.isEmpty()) {
                msg += "\n\nNotes:\n" + String.join("\n", reportLog);
            }

            showAlert("Cart Updated", msg);
            PageManager.loadPage("customer/pages/cart.fxml");
        } else {
            showAlert("Re-order Failed", "None of the items are available right now.\n\n" + String.join("\n", reportLog));
        }
    }

    private void showOrderDetails(Order order) {
        List<OrderProduct> products = orderProductDAO.getByOrderIdWithDetails(order.getOid());
        StringBuilder details = new StringBuilder();
        for (OrderProduct op : products) {
            details.append("• ").append(op.getProductName())
                    .append(" (x").append(op.getQuantity()).append(")")
                    .append(" - $").append(String.format("%.2f", op.getPriceAtPurchase()))
                    .append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Details");
        alert.setHeaderText("Order #" + order.getOid());
        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    // Helper
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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