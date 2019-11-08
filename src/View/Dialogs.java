package View;

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
}
