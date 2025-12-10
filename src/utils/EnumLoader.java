package utils;

import config.DatabaseConfig;
import model.enums.OrderStatus;
import model.enums.PaymentMethod;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EnumLoader {

    private EnumLoader() throws InstantiationException {
        throw new InstantiationException("cant instantiante this util class!!!");
    }


    public static void loadAllEnums() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            loadOrderStatus(connection);
            loadPaymentMethods(connection);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    private static void loadPaymentMethods(Connection connection) throws SQLException {
        PaymentMethod.clearAllMethod();

        String query = """
       SHOW COLUMNS
       FROM payment
       WHERE Field = 'Method';
       """;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            String paymentMethods = resultSet.getString("Type");
            String[] parsedResult = parseEnums(paymentMethods);

            for (String paymentMethodsToAdd : parsedResult)
                PaymentMethod.register(paymentMethodsToAdd);

        } else
            throw new SQLException("order status not found");
    }


    private static void loadOrderStatus(Connection connection) throws SQLException {
        OrderStatus.clearAllStatus();

        String query = """
       SHOW COLUMNS
       FROM `order`
       WHERE Field = 'Status';
       """;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            String orderStatus = resultSet.getString("Type");
            String[] parsedResult = parseEnums(orderStatus);

            for (String orderStatusToAdd : parsedResult)
                OrderStatus.register(orderStatusToAdd);

        } else
            throw new SQLException("order status not found");



    }

    private static String[] parseEnums(String strToParse) {

        int start = strToParse.indexOf('(') + 1;
        int end = strToParse.lastIndexOf(')');

        String values = strToParse.substring(start, end);

        return values.replace("'", "").split(",");
    }
}
