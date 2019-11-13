package View;

import Model.ImageChanger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
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
        VBox root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        stage = primaryStage;
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1600, 900));
        primaryStage.show();
        img = new ImageView(imageChanger.getImage());
        root.getChildren().add(img);
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}
