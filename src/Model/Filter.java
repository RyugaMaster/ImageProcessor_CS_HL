package Model;

class Filter {
    private double brightness;
    private double tone;
    private boolean isGrayscale;
    private boolean isBV;

    Filter() {
        this.brightness = 1D;
        this.tone = 0D;
        this.isGrayscale = false;
        isBV = false;
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

    void setBV(boolean BV) {
        isBV = BV;
    }

    boolean isBV() {
        return isBV;
    }
}
