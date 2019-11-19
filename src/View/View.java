package View;

import Model.ImageChanger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View extends Application {

    static Stage stage;
    static private ImageView img;
    static private ImageChanger imageChanger;

    public static void start(String[] args, ImageChanger _imageChanger) {
        imageChanger = _imageChanger;
        launch(args);
    }

    public static void read() {
        img.setImage(imageChanger.getImage());
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        VBox root = fxmlLoader.load(getClass().getResource("layout.fxml").openStream());
        ViewController viewController = fxmlLoader.getController();
        stage = primaryStage;
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1600, 900));
        primaryStage.show();
        AnchorPane field = viewController.pane;
        img = new ImageView(imageChanger.getImage());
        System.out.println(field);
        field.getChildren().add(img);
        img.setFitWidth(1366);
        img.setFitHeight(768);
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}
