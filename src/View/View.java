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
        //imageChanger.setBrightness(0.5);
        //imageChanger.setTone(120);
        //imageChanger.setTone(1.5);
        imageChanger.toGrayScale();
        imageChanger.saveImage("C:\\Users\\obole\\Downloads\\sceneryGrayscale.png");
        /*int[] hist = imageChanger.getHistogramRed(5);
        for(int i : hist)
            System.out.print(i + " ");
        System.out.println("");
        imageChanger.setTone(100);
        hist = imageChanger.getHistogramRed(5);
        for(int i : hist)
            System.out.print(i + " ");
        System.out.println("");*/
        imageChanger.setLayoutX(600);
        ImageView imageView = new ImageView("file:C:\\Users\\obole\\Downloads\\scenery.jpeg");
        Group root = new Group(imageView, imageChanger);
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    public static void show(String[] args) {launch(args);}

    public static void main(String[] args) {
        launch(args);
    }
}
