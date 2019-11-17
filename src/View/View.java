package View;

import Model.ImageChanger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

public class View extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("ImageProcessor");
        ImageChanger imageChanger = new ImageChanger("file:C:\\Users\\obole\\Downloads\\scenery.jpeg");
        int[] hist = imageChanger.getHistogramRed(8);
        for(int i : hist)
            System.out.print(i + " ");
        ImageView imageViewCurrent = new ImageView(imageChanger.getImage());
        imageViewCurrent.setLayoutX(600);
        ImageView imageViewDefault = new ImageView("file:C:\\Users\\obole\\Downloads\\scenery.jpeg");
        Group root = new Group(imageViewDefault, imageViewCurrent);
        primaryStage.setScene(new Scene(root, 1200, 500));
        primaryStage.show();
    }

    public static void show(String[] args) {launch(args);}

    public static void main(String[] args) {
        launch(args);
    }
}
