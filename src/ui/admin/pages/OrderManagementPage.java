package ui.admin.pages;

import dao.OrderDAO;
import dao.OrderProductDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;
import model.OrderProduct;
import model.Product;
import model.enums.OrderStatus;
import ui.PageManager;

import java.sql.Date;
import java.util.List;

public class OrderManagementPage {

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colOid;
    @FXML private TableColumn<Order, String> colCustomer;
    @FXML private TableColumn<Order, Date> colDate;
    @FXML private TableColumn<Order, Double> colShippingCost;
    @FXML private TableColumn<Order, Double> colTotal;
    @FXML private TableColumn<Order, String> colStatus;

    @FXML private ComboBox<String> statusCombo;
    @FXML private ComboBox<String> filterCombo;
    @FXML private Label statusLabel;

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderProductDAO orderProductDAO = new OrderProductDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ObservableList<Order> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        setupControls();
        loadOrders();
    }

    private void setupTable() {
        colOid.setCellValueFactory(new PropertyValueFactory<>("oid"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colShippingCost.setCellValueFactory(new PropertyValueFactory<>("shippingCost"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderTable.setItems(orderList);

        // Auto-select status in combobox when a row is clicked
        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                statusCombo.setValue(newVal.getStatus());
            }
        });
    }

    private void setupControls() {
        statusCombo.setItems(FXCollections.observableArrayList(OrderStatus.getAllStatus()));

        ObservableList<String> filters = FXCollections.observableArrayList("All");

        filters.addAll(OrderStatus.getAllStatus());

        filterCombo.setItems(filters);
        filterCombo.getSelectionModel().selectFirst();
    }

    private void loadOrders() {
        orderList.clear();
        orderList.addAll(orderDAO.getAllWithCustomerDetails());
    }

    @FXML
    private void handleUpdateStatus() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        String newStatus = statusCombo.getValue();
        String currentStatus = (selected != null) ? selected.getStatus() : "";

        if (selected == null) {
            setStatusMessage("Please select an order first.", true);
            return;
        }
        if (newStatus == null) {
            setStatusMessage("Please select a new status.", true);
            return;
        }

        if (!isValidTransition(selected.getStatus(), newStatus)) {
            setStatusMessage("Invalid Move: You cannot go from " + selected.getStatus() + " to " + newStatus, true);
            return;
        }

        if (newStatus.equalsIgnoreCase("CANCELLED") && !currentStatus.equalsIgnoreCase("CANCELLED")) {
            restoreStock(selected.getOid());
        }
        else if (currentStatus.equalsIgnoreCase("CANCELLED") && newStatus.equalsIgnoreCase("PENDING")) {
            boolean success = reReserveStock(selected.getOid());
            if (!success) {
                setStatusMessage("Cannot restore order: Not enough stock available.", true);
                return; // Stop the update
            }
        }

        if (orderDAO.updateStatus(selected.getOid(), newStatus)) {
            setStatusMessage("Order #" + selected.getOid() + " updated to " + newStatus, false);
            selected.setStatus(newStatus);
            orderTable.refresh();
        } else {
            setStatusMessage("Update failed due to database error.", true);
        }
    }

    /**
     * Loops through all items in the order and adds their quantity back to the global stock.
     */
    private void restoreStock(int orderId) {
        List<OrderProduct> items = orderProductDAO.getByOrderIdWithDetails(orderId);
        for (OrderProduct op : items) {
            productDAO.increaseStock(op.getProductId(), op.getQuantity());
        }
        System.out.println("Stock restored for Order #" + orderId);
    }

    /**
     * Loops through items to check if we can take them back out of stock.
     * Used when moving from CANCELLED back to PENDING.
     */
    private boolean reReserveStock(int orderId) {
        List<OrderProduct> items = orderProductDAO.getByOrderIdWithDetails(orderId);

        // First Pass: Check if enough stock exists for ALL items
        for (OrderProduct op : items) {
            Product p = productDAO.getById(op.getProductId());
            if (p.getStock() < op.getQuantity()) {
                return false; // Not enough stock to restore this order
            }
        }

        // Second Pass: Actually reduce the stock
        for (OrderProduct op : items) {
            productDAO.reduceStock(op.getProductId(), op.getQuantity());
        }
        return true;
    }

    /**
     * Defines the strict rules for order lifecycle.
     */
    private boolean isValidTransition(String current, String next) {
        // If status is the same, it's technically valid (no change)
        if (current.equalsIgnoreCase(next)) return true;

        return switch (current) {
            case "PENDING" ->
                // Can move forward to SHIPPED or be aborted to CANCELLED
                    next.equalsIgnoreCase("SHIPPING") || next.equalsIgnoreCase("CANCELLED");
            case "SHIPPING" ->
                // Can only move forward to DELIVERED
                    next.equalsIgnoreCase("DELIVERED");
            case "CANCELLED" ->
                // Can be restored back to PENDING
                    next.equalsIgnoreCase("PENDING");
            case "DELIVERED" ->
                // Final state. No changes allowed. (for Admin)
                    false;
            default -> false;
        };
    }

    // Helper
    private void setStatusMessage(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    @FXML
    private void handleFilter() {
        String filter = filterCombo.getValue();

        if (filter == null || filter.equals("All")) {
            loadOrders();
        } else {
            orderList.clear();
            List<Order> all = orderDAO.getAllWithCustomerDetails();

            for(Order o : all)
                if(o.getStatus().equalsIgnoreCase(filter))
                    orderList.add(o);

        }
    }

    @FXML
    private void handleBack() {
        PageManager.loadPage("admin/pages/adminhome.fxml");
    }
}