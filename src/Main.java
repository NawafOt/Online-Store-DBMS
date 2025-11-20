import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.PageManager;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        PageManager.initialize(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("/ui/login.fxml"));
        Scene scene = new Scene(root);

        String css = getClass().getResource("/styles/app.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("E-Commerce Shop");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);

        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
