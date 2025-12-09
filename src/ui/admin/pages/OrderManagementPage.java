package ui.admin.pages;

import dao.OrderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;
import model.Payment;
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
    @FXML private TableColumn<Order, Order.Status> colStatus;

    @FXML private ComboBox<Order.Status> statusCombo;
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
        statusCombo.setItems(FXCollections.observableArrayList(Order.Status.values()));

        ObservableList<String> filters = FXCollections.observableArrayList("All");
        for(Order.Status s : Order.Status.values()) {
            filters.add(s.name());
        }
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
        Order.Status newStatus = statusCombo.getValue();

        if (selected == null) {
            statusLabel.setText("Please select an order first.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        if (newStatus == null) {
            statusLabel.setText("Please select a status.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (orderDAO.updateStatus(selected.getOid(), newStatus)) {
            statusLabel.setText("Order #" + selected.getOid() + " updated to " + newStatus);
            statusLabel.setStyle("-fx-text-fill: green;");
            selected.setStatus(newStatus);
            orderTable.refresh();
        } else {
            statusLabel.setText("Update failed.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleFilter() {
        String filter = filterCombo.getValue();
        if (filter == null || filter.equals("All")) {
            loadOrders();
        } else {
            orderList.clear();
            List<Order> all = orderDAO.getAllWithCustomerDetails();
            for(Order o : all) {
                if(o.getStatus().name().equals(filter)) {
                    orderList.add(o);
                }
            }
        }
    }

    @FXML
    private void handleBack() {
        PageManager.loadPage("admin/pages/adminhome.fxml");
    }
}