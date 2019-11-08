package Controller;

import java.io.File;

public class Controller {
    public static void start() {

    }

    public static void open() {
        File f = View.Dialogs.openImage();
        if (f != null) {
            System.out.println(f.toURI().toString());
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
