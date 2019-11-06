package View;

import Controller.Buttons;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class ViewController {
    @FXML
    private MenuItem open;

    @FXML
    private void handleOpenAction(ActionEvent event)
    {
        Buttons.openImage();
    }
}
