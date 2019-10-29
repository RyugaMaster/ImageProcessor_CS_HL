package View;

import Model.ImageChanger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class View extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("ImageProcessor");
        ImageChanger imageChanger = new ImageChanger("file:C:\\Users\\obole\\Downloads\\scenery.jpeg");
        imageChanger.setBrightness(0.5);
        imageChanger.setContrast(2000D);
        imageChanger.setLayoutX(600);
        Image image = imageChanger.getImage();
        ImageView imageView = new ImageView(image);
        Group root = new Group(imageView, imageChanger);
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    public static void show(String[] args) {launch(args);}

    public static void main(String[] args) {
        launch(args);
    }
}
