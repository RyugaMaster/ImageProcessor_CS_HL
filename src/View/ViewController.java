package View;

import Controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ViewController {
    @FXML
    private MenuItem open;

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem saveAs;

    @FXML
    private MenuItem quit;

    @FXML
    ImageView img;

    @FXML
    ImageView src;

    @FXML
    ImageView proc;

    @FXML
    CheckBox bnw;

    @FXML
    CheckBox gs;

    @FXML
    Slider hue;

    @FXML
    Slider brightness;

    @FXML
    BarChart redChart;

    @FXML
    BarChart greenChart;

    @FXML
    BarChart blueChart;

    @FXML
    BarChart allChart;

    @FXML
    private void handleOpenAction(ActionEvent event)
    {
        Controller.open();
    }

    @FXML
    private void handleSaveAction(ActionEvent event) throws RuntimeException, IOException
    {
        Controller.save();
    }

    @FXML
    private void handleSaveAsAction(ActionEvent event) throws RuntimeException, IOException
    {
        Controller.saveAs();
    }

    @FXML
    private void handleQuitAction(ActionEvent event)
    {
        Controller.quit();
    }

    @FXML
    private void handleToDefaultAction(MouseEvent event) {
        Controller.toDefault();
    }

    @FXML
    private void handleToProcessedAction(MouseEvent event) {
        Controller.toProccesed();
    }

    private void handleHueChangeAction(double a) {
        Controller.hueChange(a);
    }

    ;

    private void handleBrightnessChangeAction(double a) {
        Controller.brightnessChange(a);
    }

    public void initialize() {
        hue.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleHueChangeAction(hue.valueProperty().doubleValue());
            }
        });
        brightness.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleBrightnessChangeAction(brightness.valueProperty().doubleValue());
            }
        });
    }
}
