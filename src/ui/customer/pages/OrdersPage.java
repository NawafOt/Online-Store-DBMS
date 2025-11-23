package ui.customer.pages;

import dao.OrderDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Order;
import model.Session;
import ui.PageManager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import java.util.Objects;

public class OrdersPage {
    @FXML private ImageView iconReturn;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox cardContainer;
    private final ArrayList<Order> orderList = new ArrayList<>();
    private final OrderDAO orderDAO = new OrderDAO();
    private final int customerID = Session.getInstance().getCustomerId();


    @FXML
    public void initialize() {
        loadImages();
        orderList.add(new Order(10, "PENDING", Date.valueOf("2020-1-2"), 2, 30));
        orderList.add(new Order(10, "SHIPPED", Date.valueOf("2023-6-20"), 2, 30));
        orderList.add(new Order(10, "CANCELLED", Date.valueOf("2077-3-10"), 2, 30));
        orderList.add(new Order(73, "DELIVERED", Date.valueOf("2019-8-30"), 1337, 10));

        //TODO UNCOMMENT THIS IF CONNECTION IS ESTABLISHED
        //orderList.addAll(orderDAO.getByCustomerId(customerID)); //just add all the damn elements

        Function<Order, String[]> orderMap = getOrderFunction();

        for (Order order: orderList)
            addCardForOrder(order, orderMap);

    }

    private Function<Order, String[]> getOrderFunction() {
        return order -> new String[]{
                String.format("%d", order.getOid()),
                order.getDate().toString(),
                order.getStatus()
        };
    }

    private void addCardForOrder(Order order, Function<Order, String[]> mapper) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/customer/pages/Card.fxml"));
            Node cardNode = loader.load(); //card nodes we will insert tinto the scroll pane
            CardController<Order> controller = loader.getController();

            // define view detail for this card type.
            List<CardAction> actions = List.of(
                    new CardAction("View Order Details", () -> {
                        //TODO ADD LOGIC PLEASE!!!!!
                    })
            );

            // initial fill
            controller.setData(order, mapper, actions);

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
        }
    }


    @FXML
    public void handleBack() {
        PageManager.loadPage("customer/pages/home.fxml");
    }
}
