package Model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageChanger {

    private Image defaultImage;
    private Image currentImage;
    private Filter filter;
    private double[][] grayscaleCoefficients;

    //create ImageChanger of image from specified url
    public ImageChanger(String url) {
        this.setImage(url);
        this.filter = new Filter();
        initializeGrayscaleCoefficients();
    }

    //create empty ImageChanger
    public ImageChanger() {
        this.filter = new Filter();
        initializeGrayscaleCoefficients();
    }

    //create imageChanger of specified image
    public ImageChanger(Image image) {
        this.setImage(image);
        this.filter = new Filter();
        initializeGrayscaleCoefficients();
    }

    private void initializeGrayscaleCoefficients() {
        this.grayscaleCoefficients = new double[3][256];
        for(int i = 0; i < 256; i++)
            this.grayscaleCoefficients[0][i] = 0.2162 * i;

        for(int i = 0; i < 256; i++)
            this.grayscaleCoefficients[1][i] = 0.7152 * i;

        for(int i = 0; i < 256; i++)
            this.grayscaleCoefficients[2][i] = 0.0722 * i;
    }

    //return image to initial state
    public void toDefault() {
        this.filter = new Filter();
        this.currentImage = defaultImage;
    }

    //apply changes to image
    private void applyFilter() {
        this.currentImage = defaultImage;
        boolean isGrayscale = this.filter.isGrayscale();
        double tone = this.filter.getTone();
        double brightness = this.filter.getBrightness();
        boolean isBV = this.filter.isBV();

        Image sourceImage = this.currentImage;

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage changedImage = new WritableImage(width, height);
        PixelWriter pixelWriter = changedImage.getPixelWriter();

        int[] argb = new int[4];
        int[][] carry = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                argb[0] = ((pixel >> 24) & 0xff);
                argb[1] = ((pixel >> 16) & 0xff);
                argb[2] = ((pixel >> 8) & 0xff);
                argb[3] = (pixel & 0xff);

                if(tone != 0D) getTonedPixel(argb, tone);
                if(brightness != 1) getBrightPixel(argb, brightness);
                if(isGrayscale) getGrayScalePixel(argb);
                if(isBV) getBVPixel(argb, x, y, carry);

                int changedPixel = (argb[0] << 24) | (argb[1] << 16) | (argb[2] << 8) | argb[3];
                pixelWriter.setArgb(x, y, changedPixel);
            }
        }
        this.currentImage = changedImage;
    }

    //tone pixel with specified argb
    private void getTonedPixel(int[] argb, double tone) {
        argb[1] = Math.min((int) Math.max((double) argb[1] + tone, 0D), 255);
        argb[3] = Math.min((int) Math.max((double) argb[3] - tone, 0D), 255);
    }

    //make pixel with specified argb bright
    private void getBrightPixel(int[] argb, double brightness) {
        argb[1] = (int) Math.min((double)argb[1] * brightness, 255D);
        argb[2] = (int) Math.min((double)argb[2] * brightness, 255D);
        argb[3] = (int) Math.min((double)argb[3] * brightness, 255D);
    }

    //set pixel with specified argb to grayscale
    private void getGrayScalePixel(int[] argb) {
        int gray = (int) (grayscaleCoefficients[0][argb[1]] + grayscaleCoefficients[1][argb[2]] + grayscaleCoefficients[2][argb[3]]) / 3;
        argb[1] = gray;
        argb[2] = gray;
        argb[3] = gray;
    }

    //set pixel with specified argb to black-and-white
    private void getBVPixel(int[] argb, int x, int y, int[][] carry) {
        int pixelColor = (argb[1] + argb[2] + argb[3]) / 3 + carry[x][y];
        int error;
        int width = carry.length;
        int height = carry[0].length;

        if(pixelColor < 128) {
            error = pixelColor;
            argb[1] = 0;
            argb[2] = 0;
            argb[3] = 0;
        }
        else {
            error = pixelColor - 255;
            argb[1] = 255;
            argb[2] = 255;
            argb[3] = 255;
        }

        if(pointInBounds(x + 1, y, width, height)) carry[x + 1][y] += error >> 2;
        if(pointInBounds(x + 2, y, width, height)) carry[x + 2][y] += error >> 3;
        if(pointInBounds(x + 1, y + 1, width, height)) carry[x + 1][y + 1] += error >> 3;
        if(pointInBounds(x + 2, y + 1, width, height)) carry[x + 2][y + 1] += error >> 4;
        if(pointInBounds(x, y + 1, width, height)) carry[x][y + 1] += error >> 2;
        if(pointInBounds(x - 1, y + 1, width, height)) carry[x - 1][y + 1] += error >> 3;
        if(pointInBounds(x - 2, y + 1, width, height)) carry[x - 2][y + 1] += error >> 4;
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

    //change filter to black-and-white
    public void toBV() {
        if(!this.filter.isBV()) {
            this.filter.setBV(true);
            this.applyFilter();
        }
    }

    //change image back from black-and-white
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

    //check if a pixel is in bounds of an image of specified height and width
    private boolean pointInBounds(int pointX, int pointY, int maxX, int maxY) {
        return pointX < maxX && pointY < maxY && pointX >= 0 && pointY >= 0;
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

    //get distribution of red, green, or blue across specified number of divisions
    private int[] getHistogram(int divisions, Color color) {
        Image sourceImage = this.getImage();
        PixelReader pixelReader = sourceImage.getPixelReader();

        int divisionSize = 256 / divisions;
        int divisionRatio = 256 % divisions;

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

                int divisionNumber = colorValue / (divisionSize + 1);
                if(divisionNumber >= divisionRatio)
                    divisionNumber = (colorValue - (divisionSize + 1) * divisionRatio) / divisionSize + divisionRatio;
                histogram[divisionNumber]++;
            }
        }
        return histogram;
    }

    public void setImage() {
        InputStream inputStream = this.getClass().getResourceAsStream("white.jpg");
        this.defaultImage = new Image(inputStream);
        this.currentImage = this.defaultImage;
    }

    //set image from url
    public void setImage(String url) {
        this.defaultImage = new Image(url);
        this.currentImage = Jarvis.process(this.defaultImage);
    }

    public void setImage(File f){
        setImage(f.toURI().toString());
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

    public void saveImage(File f) throws RuntimeException, IOException {
        File outputFile = f;
        if (outputFile.createNewFile()) {
            BufferedImage bImage = SwingFXUtils.fromFXImage(this.getImage(), null);
            try {
                ImageIO.write(bImage, "png", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
