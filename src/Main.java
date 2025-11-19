import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/login.fxml")));
        Scene scene = new Scene(root, 900, 600);

        String css = Objects.requireNonNull(getClass().getResource("/styles/app.css")).toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("E-Commerce Shop");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
