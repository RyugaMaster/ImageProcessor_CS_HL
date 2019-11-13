import Controller.Controller;
import Model.ImageChanger;
import Model.Model;
import View.View;

public class Main {
    private static ImageChanger imageChanger;

    public static void main(String[] args)
    {
        imageChanger = new ImageChanger();
        Model.start();
        Controller.start(imageChanger);
        View.start(args, imageChanger);
    }
}
