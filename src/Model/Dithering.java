package Model;

public enum Dithering {
    BURKES(new double[]{0, 0.25, 0.125, 0.0625}),
    JARVIS(new double[]{0, 7.0 / 48, 5.0 / 48, 3.0 / 48, 1.0 / 48});
    private double[] errorMultipliers;

    private Dithering(double[] errorMultipliers) {
        this.errorMultipliers = errorMultipliers;
    }

    public double[] getErrorMultipliers() {
        return this.errorMultipliers;
    }
}
