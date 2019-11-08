import View.View;
import Model.Model;
import Controller.Controller;

public class Main {
    public static void main(String args[])
    {
        View.start(args);
        Model.start();
        Controller.start();
    }
}
