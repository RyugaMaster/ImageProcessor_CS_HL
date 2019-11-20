package Model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

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
    private int[][] colorDistribution;
    private Dithering ditheringType = Dithering.BURKES;

    //create ImageChanger of image from specified url
    public ImageChanger(String url) {
        this.setImage(url);
        this.filter = new Filter();
        initializeGrayscaleCoefficients();
    }

    //create empty ImageChanger
    public ImageChanger() {
        this.filter = new Filter();
        this.setImage();
        initializeGrayscaleCoefficients();
    }

    //create imageChanger of specified image
    public ImageChanger(Image image) {
        this.setImage(image);
        this.filter = new Filter();
        initializeGrayscaleCoefficients();
    }

    //change th algorithm of black-and-white conversion
    public void setDitheringType(Dithering type) {
        this.ditheringType = type;
        if(this.filter.isBV())
            this.applyFilter();
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

    //calculate histograms when image is uploaded
    private void initializeColorDistribution() {
        Image sourceImage = this.currentImage;

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();
        int red, green, blue;

        colorDistribution = new int[4][256];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                red = ((pixel >> 16) & 0xff);
                green = ((pixel >> 8) & 0xff);
                blue = (pixel & 0xff);

                colorDistribution[0][(red + green + blue) / 3]++;
                colorDistribution[1][red]++;
                colorDistribution[2][green]++;
                colorDistribution[3][blue]++;
            }
        }
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
        double[] errorMultipliers = getErrorMultipliers();

        colorDistribution = new int[4][256];

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
                if(isBV) getBVPixel(argb, x, y, carry, errorMultipliers);

                colorDistribution[0][(argb[1] + argb[2] + argb[3]) / 3]++;
                colorDistribution[1][argb[1]]++;
                colorDistribution[2][argb[2]]++;
                colorDistribution[3][argb[3]]++;

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
    private void getBVPixel(int[] argb, int x, int y, int[][] carry, double[] errorMultipliers) {
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

        pushError(x, y, error, carry, width, height, errorMultipliers);
    }

    private double[] getErrorMultipliers() {
        return this.ditheringType.getErrorMultipliers();
    }

    private void pushError(int x, int y, int error, int[][] carry, int width, int height, double[] errorMultipliers) {
        if(ditheringType == Dithering.BURKES)
            pushErrorBurkes(x, y, error, carry, width, height, errorMultipliers);
        else pushErrorJarvis(x, y, error, carry, width, height, errorMultipliers);
    }

    private void pushErrorBurkes(int x, int y, int error, int[][] carry, int width, int height, double[] errorMultipliers) {
        for(int i = Math.max(x - 2, 0); i < x + 3 && i < width ; i++) {
            for (int j = y; j < y + 2 && j < height; j++) {
                carry[i][j] += errorMultipliers[Math.abs(i - x) + Math.abs(j - y)] * error;
            }
        }
    }

    private void pushErrorJarvis(int x, int y, int error, int[][] carry, int width, int height, double[] errorMultipliers) {
        for(int i = Math.max(x - 2, 0); i < x + 3 && i < width ; i++) {
            for (int j = y; j < y + 3 && j < height; j++) {
                carry[i][j] += errorMultipliers[Math.abs(i - x) + Math.abs(j - y)] * error;
            }
        }
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

    //get distribution of gray across specified number of divisions
    public int[] getHistogramTotal(int divisions) {
        return getHistogram(divisions, 0);
    }

    //get distribution of red across specified number of divisions
    public int[] getHistogramRed(int divisions) {
        return getHistogram(divisions, 1);
    }

    //get distribution of green across specified number of divisions
    public int[] getHistogramGreen(int divisions) {
        return getHistogram(divisions, 2);
    }

    //get distribution of blue across specified number of divisions
    public int[] getHistogramBlue(int divisions) {
        return getHistogram(divisions, 3);
    }

    //get distribution of red, green, or blue across specified number of divisions
    private int[] getHistogram(int divisions, int colorNumber) {
        int divisionSize = 256 / divisions;
        int divisionRatio = 256 % divisions;

        int[] histogram = new int[divisions];
        int startingValue = 0;
        int currentDivisionSize;
        int finalValue;

        for(int i = 0; i < divisions; i++) {
            currentDivisionSize = i < divisionRatio ? divisionSize + 1 : divisionSize;
            finalValue = currentDivisionSize + startingValue - 1;
            for(int j = startingValue; j <= finalValue; j++)
                histogram[i] += colorDistribution[colorNumber][j];
            startingValue = finalValue + 1;
        }
        return histogram;
    }

    //set empty image
    public void setImage() {
        InputStream inputStream = this.getClass().getResourceAsStream("white.jpg");
        this.defaultImage = new Image(inputStream);
        this.currentImage = this.defaultImage;
        initializeColorDistribution();
    }

    //set image from url
    public void setImage(String url) {
        this.defaultImage = new Image(url);
        this.currentImage = this.defaultImage;
        initializeColorDistribution();
    }

    public void setImage(File f){
        setImage(f.toURI().toString());
    }

    public void setImage(Image image) {
        this.defaultImage = image;
        this.currentImage = image;
        initializeColorDistribution();
    }

    //get current image
    public Image getImage() {
        return currentImage;
    }


    //save image to file
    public void saveImage(String path) throws RuntimeException{
        File outputFile = new File(path);
        try {
            this.saveImage(outputFile);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveImage(File f) throws RuntimeException, IOException {
        if (f.createNewFile()) {
            BufferedImage bImage = SwingFXUtils.fromFXImage(this.getImage(), null);
            try {
                ImageIO.write(bImage, "png", f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
