package ui.admin.pages;

import dao.OrderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;
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

        if (orderDAO.updateStatus(selected.getOid(), newStatus)) {
            setStatusMessage("Order #" + selected.getOid() + " updated to " + newStatus, false);
            selected.setStatus(newStatus);
            orderTable.refresh();
        } else {
            setStatusMessage("Update failed due to database error.", true);
        }
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