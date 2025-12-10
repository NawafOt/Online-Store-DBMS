package ui.customer.pages;

import dao.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import model.*;
import model.enums.OrderStatus;
import model.enums.PaymentMethod;
import ui.PageManager;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckoutPage {

    @FXML private Label addressLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label shippingLabel;
    @FXML private Label paymentFeeLabel;
    @FXML private Label taxesLabel;
    @FXML private Label grandTotalLabel;
    @FXML private ChoiceBox<ShippingCompany> shippingChoiceBox;
    @FXML private ChoiceBox<String> paymentChoiceBox;
    @FXML private Label errorLabel;

    private final Session session = Session.getInstance();
    private final ShippingDAO shippingDAO = new ShippingDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderProductDAO orderProductDAO = new OrderProductDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final ProductDAO productDAO = new ProductDAO();

    private static final double TAX_RATE = 0.15;
    private static final double CASH_ON_DELIVERY_FEE = 5.00;
    private double subtotal = 0.0;

    @FXML
    public void initialize() {
        Customer customer = session.getCurrentCustomer();
        List<Product> cartItems = session.getCart();
        if (customer == null || cartItems.isEmpty()) {
            errorLabel.setText("Error: Cannot proceed to checkout.");
            return;
        }

        addressLabel.setText(customer.getAddress());
        subtotal = cartItems.stream().mapToDouble( p -> {
            return p.getCount() * p.getUnitPrice();
        }).sum();

        List<ShippingCompany> shippingCompanies = shippingDAO.getAll();
        shippingChoiceBox.setItems(FXCollections.observableArrayList(shippingCompanies));
        paymentChoiceBox.setItems(FXCollections.observableArrayList(PaymentMethod.getAllMethod()));

        shippingChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateCostSummary());
        paymentChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateCostSummary());

        updateCostSummary();
    }

    private void updateCostSummary() {
        double shippingCost = shippingChoiceBox.getValue() != null ? shippingChoiceBox.getValue().getCost() : 0.0;
        double paymentFee = "CASH".equals(paymentChoiceBox.getValue()) ? CASH_ON_DELIVERY_FEE : 0.0;
        double totalBeforeTax = subtotal + shippingCost + paymentFee;
        double taxAmount = totalBeforeTax * TAX_RATE;
        double grandTotal = totalBeforeTax + taxAmount;

        subtotalLabel.setText(String.format("$%.2f", subtotal));
        shippingLabel.setText(String.format("$%.2f", shippingCost));
        paymentFeeLabel.setText(String.format("$%.2f", paymentFee));
        taxesLabel.setText(String.format("$%.2f", taxAmount));
        grandTotalLabel.setText(String.format("$%.2f", grandTotal));
    }

    @FXML
    private void handleConfirmAndPay() {
        ShippingCompany selectedShipping = shippingChoiceBox.getValue();
        String selectedPayment = paymentChoiceBox.getValue(); // Now an enum
        if (selectedShipping == null || selectedPayment == null) {
            errorLabel.setText("Please select shipping and payment options.");
            return;
        }

        // Final Calculations
        double shippingCost = selectedShipping.getCost();
        double grandTotal = subtotal + shippingCost + ("CASH".equals(selectedPayment) ? CASH_ON_DELIVERY_FEE : 0.0);
        grandTotal *= (1 + TAX_RATE);

        // Final Stock Validation
        for (Product product : session.getCart()) {
            if (product.getStock() < product.getCount()) {
                errorLabel.setText("Error: Not enough stock for " + product.getName());
                return;
            }
        }

        // Create the Order
        Order newOrder = new Order();
        newOrder.setCustomerId(session.getCustomerId());
        newOrder.setShippingId(selectedShipping.getSid());
        newOrder.setDate(new Date(System.currentTimeMillis()));
        newOrder.setStatus((OrderStatus.isValidStatus("PENDING")) ? "PENDING" : null); //check against DB!!
        newOrder.setShippingCost(shippingCost);

        int orderId = orderDAO.insert(newOrder);
        if (orderId == -1) {
            errorLabel.setText("Failed to create order. Please try again.");
            return;
        }

        // Link Products and Reduce Stock
        for (Product product : session.getCart()) {
            int quantity = product.getCount();
            OrderProduct orderProduct = new OrderProduct(orderId, product.getPid(), quantity, product.getUnitPrice());
            orderProductDAO.insert(orderProduct);
            productDAO.reduceStock(product.getPid(), quantity);
        }

        Payment payment = new Payment(orderId, selectedPayment, session.getCustomerId(), grandTotal);
        paymentDAO.insert(payment);

        session.getCart().clear();
        PageManager.loadPage("customer/pages/orders.fxml");
    }

    @FXML
    private void handleCancel() {
        PageManager.loadPage("customer/pages/cart.fxml");
    }
}
