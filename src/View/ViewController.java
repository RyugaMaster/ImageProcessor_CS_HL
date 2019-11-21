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
    private CheckBox bnw;

    @FXML
    private CheckBox gs;

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

    @FXML
    private void handleToBNWAction(ActionEvent event) {
        Controller.bnwChange(bnw.isSelected());
    }

    @FXML
    private void handleToGrayscaleAction(ActionEvent event) {
        Controller.grayscaleChange(gs.isSelected());
    }

    void reset() {
        hue.setValue(0.5);
        brightness.setValue(1);
        bnw.setSelected(false);
        gs.setSelected(false);
    }

    private void handleHueChangeAction(double a) {
        System.out.println("Hue: " + a);
        Controller.hueChange(a);
    }

    ;

    private void handleBrightnessChangeAction(double a) {
        System.out.println("Brightness: " + a);
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
