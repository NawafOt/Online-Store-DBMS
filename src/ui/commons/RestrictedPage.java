package ui.commons;

import javafx.fxml.FXML;
import ui.PageManager;

public class RestrictedPage {

    /**
     * Navigates the user back to the login page.
     */
    @FXML
    private void handleLogin() {
        PageManager.loadPage("login.fxml");
    }
}
