package View;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;

public class Dialogs {
    public static File openImage()
    {
        FileChooser imgChooser = new FileChooser();
        imgChooser.setTitle("Open Image File");
        imgChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        return imgChooser.showOpenDialog(View.stage);
    }

    public static File saveImage() {
        FileChooser imgChooser = new FileChooser();
        imgChooser.setTitle("Open Image File");
        imgChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        return imgChooser.showSaveDialog(View.stage);
    }

    public static Boolean approveAction(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, s, ButtonType.NO, ButtonType.YES);
        alert.showAndWait();
        return (alert.getResult() == ButtonType.YES);
    }
}
