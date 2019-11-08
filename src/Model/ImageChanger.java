package Model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageChanger extends ImageView {

    private Image defaultImage;
    private Filter filter;

    public ImageChanger(String url) {
        super();
        this.setImage(url);
        filter = new Filter();
    }

    //return image to initial state
    public void toDefault() {
        this.setImage(this.defaultImage);
    }

    //apply changes to image
    private void applyFilter() {
        this.toDefault();
        this.applyBrightness(this.filter.getBrightness());
        this.applyTone(this.filter.getTone());
        if(this.filter.isGrayscale()) this.applyGrayScale();
    }

    //change filter to grayscale
    public void toGrayScale() {
        this.filter.setGrayscale(true);
        this.applyFilter();
    }

    //change image back from grayscale
    public void undoGrayScale() {
        this.filter.setGrayscale(false);
        this.applyFilter();
    }

    //change filter brightness
    public void setBrightness(double brightness) {
        this.filter.setBrightness(brightness);
        this.applyFilter();
    }

    //change filter tone
    public void setTone(double tone) {
        this.filter.setTone(tone);
        this.applyFilter();
    }

    //set image to grayscale
    private void applyGrayScale() {
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
                int grayPixel = (grayLevel << 16) + (grayLevel << 8) + grayLevel;

                pixelWriter.setArgb(x, y, -grayPixel);
            }
        }
        this.setImage(grayImage);
    }

    //change brightness of image according to filter
    public void applyBrightness(double brightness) {
        Image sourceImage = this.getImage();

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int)sourceImage.getWidth();
        int height = (int)sourceImage.getHeight();

        WritableImage brightImage = new WritableImage(width, height);

        PixelWriter pixelWriter = brightImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int brightRed = 255 - (int) Math.min((double)red * brightness, 255D);
                int brightGreen = 255 - (int) Math.min((double)green * brightness, 255D);
                int brightBlue = 255 - (int) Math.min((double)blue * brightness, 255D);
                int brightPixel = (brightRed << 16) + (brightGreen << 8) + brightBlue;

                pixelWriter.setArgb(x, y, -brightPixel);
            }
        }
        this.setImage(brightImage);
    }

    //change tone of image according to filter
    private void applyTone(double tone) {
        Image sourceImage = this.getImage();

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int)sourceImage.getWidth();
        int height = (int)sourceImage.getHeight();

        WritableImage TonedImage = new WritableImage(width, height);

        PixelWriter pixelWriter = TonedImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int TonedRed = 255 - Math.max((int)Math.min((double)red * tone, 255D), 0);
                int TonedGreen = 255 - green;
                int TonedBlue = 255 - Math.min((int)Math.max((double)blue / tone, 0D), 255);
                int toned = 255 - (TonedRed << 16) - (TonedGreen << 8) - TonedBlue;

                pixelWriter.setArgb(x, y, toned);
            }
        }
        this.setImage(TonedImage);
    }

    //get distribution of red across specified number of divisions
    public int[] getHistogramRed(int divisions) {
        return getHistogram(divisions, Color.RED);
    }

    //get distribution of green across specified number of divisions
    public int[] getHistogramGreen(int divisions) {
        return getHistogram(divisions, Color.GREEN);
    }

    //get distribution of blue across specified number of divisions
    public int[] getHistogramBlue(int divisions) {
        return getHistogram(divisions, Color.BLUE);
    }

    //get distribution of a color across specified number of divisions
    private int[] getHistogram(int divisions, Color color) {
        Image sourceImage = this.getImage();
        PixelReader pixelReader = sourceImage.getPixelReader();

        int divisionSize = 255 / divisions;

        int width = (int)sourceImage.getWidth();
        int height = (int)sourceImage.getHeight();

        int[] histogram = new int[divisions];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                int colorValue;
                if(color == Color.RED) colorValue = ((pixel >> 16) & 0xff);
                else if(color == Color.GREEN) colorValue = ((pixel >> 8) & 0xff);
                else colorValue = (pixel & 0xff);

                int divisionNumber = colorValue / divisionSize;
                if(divisionNumber == divisions) divisionNumber--;
                histogram[divisionNumber]++;
            }
        }
        return histogram;
    }

    //set image from url
    public void setImage(String url) {
        this.defaultImage = new Image(url);
        this.setImage(defaultImage);
    }

    //save image to file
    public void saveImage(String path) throws RuntimeException, IOException {
        File outputFile = new File(path);
        if(outputFile.createNewFile()) {
            BufferedImage bImage = SwingFXUtils.fromFXImage(this.getImage(), null);
            try {
                ImageIO.write(bImage, "png", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
