package Model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageChanger extends ImageView {

    private Image defaultImage;
    private ColorAdjust filter;

    public ImageChanger(String url) {
        this.defaultImage = new Image(url);
        this.setImage(defaultImage);
        filter = new ColorAdjust();
    }

    public void toDefault() {
        this.setImage(this.defaultImage);
        this.filter = new ColorAdjust();
        this.setEffect(filter);
    }

    public void toGrayScale() {
        Image sourceImage = this.getImage();

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage grayImage = new WritableImage(width, height);

        PixelWriter pixelWriter = grayImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int grayLevel = (int) (0.2162 * (double)red + 0.7152 * (double)green + 0.0722 * (double)blue) / 3;
                grayLevel = 255 - grayLevel;
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;

                pixelWriter.setArgb(x, y, -gray);
            }
        }
        this.setImage(grayImage);
    }

    public void setContrast(double contrast) {
        this.filter.setContrast(contrast);
        this.setEffect(this.filter);
    }

    public void setBrightness(double brightness) {
        this.filter.setBrightness(brightness);
        this.setEffect(this.filter);
    }
}
