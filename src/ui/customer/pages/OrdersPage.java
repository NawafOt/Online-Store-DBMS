package ui.customer.pages;

import dao.OrderDAO;
import dao.OrderProductDAO;
import dao.PaymentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Order;
import model.OrderProduct;
import model.Payment;
import model.Session;
import ui.PageManager;

import java.util.List;
import java.util.Objects;

/**
 * Controller for displaying a customer's order history.
 * Each order is displayed in a collapsible TitledPane within an Accordion.
 */
public class OrdersPage {

    @FXML
    private ImageView iconReturn;
    @FXML
    private Accordion ordersAccordion;

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderProductDAO orderProductDAO = new OrderProductDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final int customerId = Session.getInstance().getCustomerId();

    /**
     * FXML initialization: loads icons and populates the orders view.
     */
    @FXML
    public void initialize() {
        loadImages();
        loadCustomerOrders();
    }

    /**
     * Fetches and displays the customer's orders in an accordion view.
     */
    private void loadCustomerOrders() {
        // Get all orders for the current customer.
        List<Order> orders = orderDAO.getByCustomerId(customerId);

        if (orders.isEmpty()) {
            ordersAccordion.getPanes().add(new TitledPane("No Orders Found", new Label("You have not placed any orders yet.")));
            return;
        }

        for (Order order : orders) {
            // For each order, create a TitledPane.
            TitledPane pane = createOrderPane(order);
            ordersAccordion.getPanes().add(pane);
        }
    }

    /**
     * Creates a TitledPane for a single order, including its details and the products it contains.
     * @param order The order to display.
     * @return A TitledPane ready to be added to the Accordion.
     */
    private TitledPane createOrderPane(Order order) {
        // Fetch the payment record to get the historically accurate grand total.
        Payment payment = paymentDAO.getById(order.getOid());
        double grandTotal = (payment != null) ? payment.getTotalAmount() : 0.0;

        // Create the title for the pane with summary information.
        String title = String.format("Order #%d  |  Date: %s  |  Status: %s  |  Total: $%.2f",
                order.getOid(), order.getDate().toString(), order.getStatus(), grandTotal);

        // Get the list of products for this order.
        List<OrderProduct> productsInOrder = orderProductDAO.getByOrderIdWithDetails(order.getOid());

        // Create a ListView to display the products.
        ListView<String> productListView = new ListView<>();
        for (OrderProduct op : productsInOrder) {
            String productInfo = String.format("%s (x%d) - $%.2f each",
                    op.getProductName(), op.getQuantity(), op.getPriceAtPurchase());
            productListView.getItems().add(productInfo);
        }

        return new TitledPane(title, productListView);
    }

    /**
     * Load small page icons and set them on the corresponding ImageView.
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
     * Navigate back to the customer home page.
     */
    @FXML
    public void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
