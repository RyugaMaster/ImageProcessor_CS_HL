package Controller;

import Model.ImageChanger;
import View.Dialogs;

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
            View.View.reset();
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

    public static void toDefault() {
        if (Dialogs.approveAction("Return to source image?")) {
            View.View.reset();
            imageChanger.toDefault();
            View.View.read();
        }
    }

    public static void toProccesed() {
        if (Dialogs.approveAction("Work on processed image?")) {
            View.View.reset();
            imageChanger.setImage(imageChanger.getImage());
            View.View.read();
        }
    }

    public static void hueChange(double a) {
        imageChanger.setTone(a);
        View.View.read();
    }

    public static void brightnessChange(double a) {
        imageChanger.setBrightness(a);
        View.View.read();
    }

    public static void bnwChange(boolean a) {
        if (a)
            imageChanger.toBV();
        else
            imageChanger.undoBV();
        View.View.read();

    }

    public static void grayscaleChange(boolean a) {
        if (a)
            imageChanger.toGrayScale();
        else
            imageChanger.undoGrayScale();
        View.View.read();

    }
}
