package ui.admin.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Session;
import ui.PageManager;
import dao.*;
import model.Payment;

import java.util.List;

/**
 * Controller for the Admin Home/Dashboard page.
 * Provides navigation to all entity management pages.
 */
public class AdminHomePage {

    @FXML
    private Label welcomeLabel;

    @FXML private Label lblTotalRevenue;
    @FXML private Label lblTotalOrders;
    @FXML private Label lblTotalCustomers;

    private final Session session = Session.getInstance();

    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, Admin");
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        // 1. Calculate Total Orders
        int totalOrders = orderDAO.getAll().size();
        lblTotalOrders.setText(String.valueOf(totalOrders));

        // 2. Calculate Total Customers
        int totalCustomers = customerDAO.getAll().size();
        lblTotalCustomers.setText(String.valueOf(totalCustomers));

        // 3. Calculate Total Revenue
        List<Payment> payments = paymentDAO.getAll();
        double totalRevenue = 0.0;

        for (Payment p : payments) {
            totalRevenue += p.getTotalAmount();
        }

        lblTotalRevenue.setText(String.format("$%.2f", totalRevenue));
    }

    @FXML
    private void handleProducts() {
        PageManager.loadPage("admin/pages/admin_products.fxml");
    }

    @FXML
    private void handleCustomers() {
        PageManager.loadPage("admin/pages/admin_customers.fxml");
    }

    @FXML
    private void handleOrders() {
        PageManager.loadPage("admin/pages/admin_orders.fxml");
    }

    @FXML
    private void handleShipping() {
        PageManager.loadPage("admin/pages/admin_shipping.fxml");
    }

    @FXML
    private void handleLogout() {
        session.logout();
        PageManager.loadPage("login.fxml");
    }
}
