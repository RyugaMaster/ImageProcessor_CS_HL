package View;

import Model.ImageChanger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
    static private BarChart Red;
    static private BarChart Green;
    static private BarChart Blue;
    static private BarChart Gray;
    static private ViewController viewController;

    public static void start(String[] args, ImageChanger _imageChanger) {
        imageChanger = _imageChanger;
        launch(args);
    }

    public static void read() {
        src.setImage(imageChanger.getDefaultImage());
        img.setImage(imageChanger.getImage());
        proc.setImage(imageChanger.getImage());
        XYChart.Series<String, Integer> redData = new XYChart.Series<String, Integer>();
        XYChart.Series<String, Integer> greenData = new XYChart.Series<String, Integer>();
        XYChart.Series<String, Integer> blueData = new XYChart.Series<String, Integer>();
        XYChart.Series<String, Integer> totalData = new XYChart.Series<String, Integer>();
        addall(redData, imageChanger.getHistogramRed(256));
        addall(greenData, imageChanger.getHistogramGreen(256));
        addall(blueData, imageChanger.getHistogramBlue(256));
        addall(totalData, imageChanger.getHistogramTotal(256));
        Red.getData().clear();
        Green.getData().clear();
        Blue.getData().clear();
        Gray.getData().clear();
        Red.getData().add(redData);
        Green.getData().add(greenData);
        Blue.getData().add(blueData);
        Gray.getData().add(totalData);
    }

    private static void addall(XYChart.Series<String, Integer> data, int[] distr) {
        for (int i = 0; i < distr.length; ++i) {
            data.getData().add(new XYChart.Data<String, Integer>(Integer.toString(i), distr[i]));
        }
    }

    public static void reset() {
        viewController.reset();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        VBox root = fxmlLoader.load(getClass().getResource("layout.fxml").openStream());
        viewController = fxmlLoader.getController();
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
        Red = viewController.redChart;
        Green = viewController.greenChart;
        Blue = viewController.blueChart;
        Gray = viewController.allChart;
        // img.setFitWidth(1366);
        //img.setFitHeight(768);
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}
