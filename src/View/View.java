package View;

import Model.ImageChanger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View extends Application {

    static Stage stage;
    static private ImageView img;
    static private ImageView src;
    static private ImageView proc;
    static private ImageChanger imageChanger;

    public static void start(String[] args, ImageChanger _imageChanger) {
        imageChanger = _imageChanger;
        launch(args);
    }

    public static void read() {
        src.setImage(imageChanger.getDefaultImage());
        img.setImage(imageChanger.getImage());
        proc.setImage(imageChanger.getImage());
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
        img = viewController.img;
        src = viewController.src;
        proc = viewController.proc;
        Slider hue = viewController.hue;
        Slider brightness = viewController.brightness;
        hue.setMax(1);
        hue.setMin(0);
        hue.setValue(0.5);
        brightness.setMin(0.5);
        brightness.setMax(2);
        brightness.setValue(1);
        // img.setFitWidth(1366);
        //img.setFitHeight(768);
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}
