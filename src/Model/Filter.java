package Model;

public class Filter {
    private double brightness;
    private double tone;
    private boolean isGrayscale;

    public Filter() {
        this.brightness = 1D;
        this.tone = 1D;
        this.isGrayscale = false;
    }

    void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    void setTone(double tone) {
        this.tone = tone;
    }

    void setGrayscale(boolean grayscale) {
        isGrayscale = grayscale;
    }

    double getBrightness() {
        return brightness;
    }

    double getTone() {
        return tone;
    }

    boolean isGrayscale() {
        return isGrayscale;
    }
}
