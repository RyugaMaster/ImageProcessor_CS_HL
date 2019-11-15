package Controller;

import Model.ImageChanger;

import java.io.File;
import java.io.IOException;

public class Controller {
    static private ImageChanger imageChanger;
    static private File file;

    public static void start(ImageChanger _imageChanger) {
        imageChanger = _imageChanger;
        file = null;
    }

    public static void open() {
        File f = View.Dialogs.openImage();
        if (f != null) {
            file = f;
            imageChanger.setImage(f);
            View.View.read();
        }
    }

    public static void save() throws RuntimeException, IOException {
        if (file != null) {
            imageChanger.saveImage(file);
        }

    }

    public static void saveAs() throws RuntimeException, IOException {
        File f = View.Dialogs.saveImage();
        if (f != null) {
            file = f;
            imageChanger.saveImage(f);
        }
    }

    public static void quit() {

    }
}
