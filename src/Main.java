import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.PageManager;
import utils.EnumLoader;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        EnumLoader.loadAllEnums();

        PageManager.initialize(primaryStage);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/login.fxml")));
        Scene scene = new Scene(root);

        String css = Objects.requireNonNull(getClass().getResource("/styles/app.css")).toExternalForm();
        scene.getStylesheets().add(css);

        Image mainIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/FreeSail-Icon.png")));

        primaryStage.setTitle("Free Sail - Whatever, Whenever!");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.getIcons().add(mainIcon);

        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
