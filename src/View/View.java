package View;

import Model.Dithering;
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
        imageChanger.setImage("file:C:\\Users\\obole\\Downloads\\scenery.jpeg");
        imageChanger.setBrightness(1.2);
        imageChanger.setTone(0.6);
        imageChanger.toGrayScale();
        imageChanger.toBV();
        int sum = 0;
        for(int i = 0; i < 10; i++) {
            long a = System.currentTimeMillis();
            imageChanger.setDitheringType(Dithering.JARVIS);
            sum += System.currentTimeMillis() - a;
            imageChanger.setDitheringType(Dithering.BURKES);
        }
        System.out.println(sum / 10);
        img = new ImageView(imageChanger.getImage());
        field.getChildren().add(img);
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}