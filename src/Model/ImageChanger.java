package Model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageChanger {

    private Image defaultImage;
    private Image currentImage;
    private Filter filter;

    //create ImageChanger of image from specified url
    public ImageChanger(String url) {
        this.setImage(url);
        this.filter = new Filter();
    }

    //create empty ImageChanger
    public ImageChanger() {
        this.filter = new Filter();
    }

    //create imageChanger of specified image
    public ImageChanger(Image image) {
        this.setImage(image);
        this.filter = new Filter();
    }

    //return image to initial state
    public void toDefault() {
        this.filter.setGrayscale(false);
        this.filter.setBrightness(1);
        this.filter.setTone(0.5);
        this.currentImage = defaultImage;
    }

    //apply changes to image
    private void applyFilter() {
        this.currentImage = defaultImage;
        boolean isGrayscale = this.filter.isGrayscale();
        double tone = this.filter.getTone();
        double brightness = this.filter.getBrightness();
        boolean isBV = this.filter.isBV();
        if(tone != 0.5) this.applyTone(tone);
        if(brightness != 1D) this.applyBrightness(brightness);
        if(isGrayscale) this.applyGrayScale();
        if(isBV) this.applyBV();
    }

    //change filter to grayscale
    public void toGrayScale() {
        if(!this.filter.isGrayscale()) {
            this.filter.setGrayscale(true);
            this.applyFilter();
        }
    }

    //change image back from grayscale
    public void undoGrayScale() {
        if(this.filter.isGrayscale()) {
            this.filter.setGrayscale(false);
            this.applyFilter();
        }
    }

    //change filter to black and white
    public void toBV() {
        if(!this.filter.isBV()) {
            this.filter.setBV(true);
            this.applyFilter();
        }
    }

    //change image back from black and white
    public void undoBV() {
        if(this.filter.isBV()) {
            this.filter.setBV(false);
            this.applyFilter();
        }
    }

    //change filter brightness
    public void setBrightness(double brightness) {
        this.filter.setBrightness(brightness);
        this.applyFilter();
    }

    //change filter tone
    public void setTone(double tone) {
        tone = 510 * tone - 255;
        this.filter.setTone(tone);
        this.applyFilter();
    }

    //set image to grayscale
    private void applyGrayScale() {
        Image sourceImage = this.currentImage;

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage grayImage = new WritableImage(width, height);

        PixelWriter pixelWriter = grayImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                int alpha = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int grayLevel = (int) (0.2162 * (double)red + 0.7152 * (double)green + 0.0722 * (double)blue) / 3;
                int grayPixel = (alpha << 24) | (grayLevel << 16) | (grayLevel << 8) | grayLevel;

                pixelWriter.setArgb(x, y, grayPixel);
            }
        }
        this.currentImage = grayImage;
    }

    //change brightness of image according to filter
    private void applyBrightness(double brightness) {
        Image sourceImage = this.currentImage;

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int)sourceImage.getWidth();
        int height = (int)sourceImage.getHeight();

        WritableImage brightImage = new WritableImage(width, height);

        PixelWriter pixelWriter = brightImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                int alpha = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int brightRed = (int) Math.min((double)red * brightness, 255D);
                int brightGreen = (int) Math.min((double)green * brightness, 255D);
                int brightBlue = (int) Math.min((double)blue * brightness, 255D);
                int brightPixel = (alpha << 24) | (brightRed << 16) | (brightGreen << 8) | brightBlue;

                pixelWriter.setArgb(x, y, brightPixel);
            }
        }
        this.currentImage = brightImage;
    }

    //change tone of image according to filter
    private void applyTone(double tone) {
        Image sourceImage = this.getImage();

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int)sourceImage.getWidth();
        int height = (int)sourceImage.getHeight();

        WritableImage tonedImage = new WritableImage(width, height);

        PixelWriter pixelWriter = tonedImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int pixel = pixelReader.getArgb(x, y);

                int alpha = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int tonedRed = Math.max((int) Math.min((double) red + tone, 255D), 0);
                int tonedBlue = Math.min((int) Math.max((double) blue - tone, 0D), 255);
                int toned = (alpha << 24) | (tonedRed << 16) | (green << 8) | tonedBlue;

                pixelWriter.setArgb(x, y, toned);
            }
        }
        this.currentImage = tonedImage;
    }

    //check if a pixel is in bounds of an image of specified height and width
    private boolean pointInBounds(int pointX, int pointY, int maxX, int maxY) {
        return pointX < maxX && pointY < maxY && pointX >= 0 && pointY >= 0;
    }

    //set image to black and white
    private void applyBV() {
        Image sourceImage = this.getImage();

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int)sourceImage.getWidth();
        int height = (int)sourceImage.getHeight();
        int[][] carry = new int[width][height];

        WritableImage BVImage = new WritableImage(width, height);

        PixelWriter pixelWriter = BVImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int pixel = pixelReader.getArgb(x, y);

                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int pixelColor = (red + green + blue) / 3 + carry[x][y];
                int error;

                if(pixelColor < 128) {
                    error = pixelColor;
                    pixelWriter.setColor(x, y, Color.BLACK);
                }
                else {
                    error = pixelColor - 255;
                    pixelWriter.setColor(x, y, Color.WHITE);
                }

                if(pointInBounds(x + 1, y, width, height)) carry[x + 1][y] += error >> 2;
                if(pointInBounds(x + 2, y, width, height)) carry[x + 2][y] += error >> 3;
                if(pointInBounds(x + 1, y + 1, width, height)) carry[x + 1][y + 1] += error >> 3;
                if(pointInBounds(x + 2, y + 1, width, height)) carry[x + 2][y + 1] += error >> 4;
                if(pointInBounds(x, y + 1, width, height)) carry[x][y + 1] += error >> 2;
                if(pointInBounds(x - 1, y + 1, width, height)) carry[x - 1][y + 1] += error >> 3;
                if(pointInBounds(x - 2, y + 1, width, height)) carry[x - 2][y + 1] += error >> 4;
            }
        }
        this.currentImage = BVImage;
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
        this.currentImage = this.defaultImage;
    }

    public void setImage(Image image) {
        this.defaultImage = image;
        this.currentImage = image;
    }

    //get current image
    public Image getImage() {
        return currentImage;
    }

    //save image to file
    public void saveImage(String path) throws RuntimeException{
        File outputFile = new File(path);
        BufferedImage bImage = SwingFXUtils.fromFXImage(this.getImage(), null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
