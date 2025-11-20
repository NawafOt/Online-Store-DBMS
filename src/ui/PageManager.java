package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PageManager {
    private static Stage primaryStage;
    private static final String CSS_PATH = "/styles/app.css";

    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

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