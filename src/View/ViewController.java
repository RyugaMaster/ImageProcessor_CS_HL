package View;

import Controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

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
    AnchorPane pane;

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
}
