package Controller;

import Model.ImageChanger;

import java.io.File;

public class Controller {
    static private ImageChanger imageChanger;

    public static void start(ImageChanger _imageChanger) {
        imageChanger = _imageChanger;
    }

    public static void open() {
        File f = View.Dialogs.openImage();
        if (f != null) {
            imageChanger.setImage(f);
            View.View.read();
        }
    }

    public static void save() {

    }

    public static void saveAs() {
        File f = View.Dialogs.saveImage();
        if (f != null) {
            System.out.println(f.toURI().toString());
        }
    }

    public static void quit() {

    }
}
