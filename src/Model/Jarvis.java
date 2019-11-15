package Model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Jarvis {
    private final static double[] MULT = {0, 7.0 / 48, 5.0 / 48, 3.0 / 48, 1.0 / 48};
    private final static double W_RED = 0.299 * 255;
    private final static double W_GREEN = 0.587 * 255;
    private final static double W_BLUE = 0.114 * 255;

    public static Image process(Image img) {
        int h = (int) img.getHeight(), w = (int) img.getWidth();
        WritableImage res = new WritableImage(w, h);
        PixelWriter writer = res.getPixelWriter();
        PixelReader reader = img.getPixelReader();
        double[][] err = new double[w][h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                //System.out.print("[ " + x + " , " + y + " ] : ");
                Color c = reader.getColor(x, y);
                //System.out.print(c);
                double gray = (c.getRed() * W_RED + c.getGreen() * W_GREEN + c.getBlue() * W_BLUE);
                //System.out.print(gray + ", ");
                gray += err[x][y];
                //System.out.print(", " + err[x][y] + ", " + gray + ", ");

                double cerr;
                if (Math.abs(255 - gray) < Math.abs(gray)) {
                    cerr = Math.min(0, gray - 255);
                    writer.setColor(x, y, Color.WHITE);
                } else {
                    cerr = Math.max(0, gray);
                    writer.setColor(x, y, Color.BLACK);
                }
                //System.out.print(cerr + "; ");
                for (int j = y; j < y + 3 && j < h; ++j)
                    for (int i = x - 2; i < x + 3 && i < w; ++i) {
                        if (j <= y && i <= x || i < 0)
                            continue;
                        err[i][j] += cerr * MULT[Math.abs(j - y) + Math.abs(i - x)];
                    }
            }
            //System.out.println();
        }
        return res;
    }
}
