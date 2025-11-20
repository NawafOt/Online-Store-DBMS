package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginPage {


    @FXML
    public void handleContinue() {
        PageManager.loadPage("customer/pages/home.fxml");
    }

    public void handleRegister() {
        PageManager.loadPage("customer/pages/register.fxml");
    }

    public void handleGuest() {
        //logic for guest stuff
    }
}
