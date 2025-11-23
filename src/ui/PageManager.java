package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Session;

import java.io.IOException;

/**
 * Utility responsible for switching the primary stage scene to different FXML pages.
 *
 * <p>Holds a reference to the primary Stage and exposes simple load/initialize helpers.
 * The class also applies a global CSS stylesheet to every loaded scene.</p>
 */
public class PageManager {
    private static Stage primaryStage;
    /**
     * Path to the global CSS stylesheet applied to loaded scenes.
     */
    private static final String CSS_PATH = "/styles/app.css";
    private static final Session SESSION = Session.getInstance(); //maybe use this??? idk

    /**
     * Initialize the PageManager with the application's primary stage.
     *
     * @param stage the primary Stage
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Load an FXML page and set it as the current scene on the primary stage.
     * Applies the global stylesheet and sets sensible minimum stage dimensions.
     *
     * @param fxmlFile path to the FXML resource relative to the ui package
     */
    public static void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(PageManager.class.getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);

            // Apply the CSS stylesheet
            String css = PageManager.class.getResource(CSS_PATH).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}