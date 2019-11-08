package Controller;

public class Controller {
    public static void start() {

    }

    public static void open()
    {
        System.out.println(View.Dialogs.openImage().toURI().toString());
    }

    public static void save()
    {

    }

    public static void saveAs()
    {
        System.out.println(View.Dialogs.saveImage().toURI().toString());
    }

    public static void quit()
    {

    }
}
