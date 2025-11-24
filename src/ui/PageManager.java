package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Utility responsible for switching scenes on the primary stage.
 *
 * <p>This class manages all page/scene navigation for the application. It holds a
 * reference to the primary Stage and provides methods to load FXML pages. It also
 * includes a mechanism for passing data between controllers during navigation by using
 * the {@link DataReceiver} interface.</p>
 */
public class PageManager {
    private static Stage primaryStage;
    /**
     * Path to the global CSS stylesheet applied to loaded scenes.
     */
    private static final String CSS_PATH = "/styles/app.css";

    /**
     * Initializes the PageManager with the application's primary stage.
     * This must be called once at application startup.
     *
     * @param stage the primary Stage
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Loads an FXML page without passing any data.
     * This is a convenience method that calls the main data-passing loadPage method with null data.
     *
     * @param fxmlFile path to the FXML resource relative to the ui package
     */
    public static void loadPage(String fxmlFile) {
        loadPage(fxmlFile, null);
    }

    /**
     * Loads an FXML page and optionally passes a data object to its controller.
     *
     * This method handles the core logic of scene switching. After loading the FXML,
     * it retrieves the controller instance. If the controller implements the {@link DataReceiver}
     * interface and the provided {@code data} object is not null, it calls the controller's
     * {@code receiveData} method. This allows state to be passed between scenes.
     *
     * @param fxmlFile path to the FXML resource relative to the ui package.
     * @param data The data object to pass to the new controller. Can be null if no data is needed.
     */
    public static void loadPage(String fxmlFile, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(PageManager.class.getResource(fxmlFile)));
            Parent root = loader.load();

            // If data is provided, pass it to the controller.
            if (data != null) {
                Object controller = loader.getController();
                if (controller instanceof DataReceiver) {
                    ((DataReceiver) controller).receiveData(data);
                }
            }

            Scene scene = new Scene(root);
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);

            String css = Objects.requireNonNull(PageManager.class.getResource(CSS_PATH)).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to load FXML page: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
