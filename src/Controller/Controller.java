package Controller;

import javafx.fxml.FXML;

public class Controller {
    public static void start() {

    }
    public static void openImage()
    {
        System.out.println(View.Dialogs.openImage().toURI().toString());
    }
}
