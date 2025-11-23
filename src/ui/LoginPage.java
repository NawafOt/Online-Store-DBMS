package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the login view.
 *
 * <p>Contains simple navigation handlers used by the login screen.
 * Authentication logic is out of scope for this controller.</p>
 */
public class LoginPage {


    /**
     * Continue to the application as an authenticated customer (placeholder).
     * Navigates to the customer home page.
     */
    @FXML
    public void handleContinue() {
        PageManager.loadPage("customer/pages/home.fxml");
    }

    /**
     * Navigate to the registration page.
     */
    public void handleRegister() {
        PageManager.loadPage("customer/pages/register.fxml");
    }

    /**
     * Handle guest access flow. Implementation intentionally left as a stub.
     */
    public void handleGuest() {
        //logic for guest stuff
    }
}
