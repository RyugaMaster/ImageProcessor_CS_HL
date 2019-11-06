package Controller;

import java.io.File;

public class Buttons {
    public static void openImage()
    {
        System.out.println(View.Dialogs.openImage().toURI().toString());
        //return new Image(file.toURI().toString());
    }
}
