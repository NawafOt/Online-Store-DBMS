package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {

    public static void showInfo(String message) {
        // Implementation for showing an info alert
    }

    public static void showError(String message) {
        // Implementation for showing an error alert
    }

    public static boolean showConfirmation(String operation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText("Proceed with " + operation);
        alert.setContentText("Are you sure you want to proceed?");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
