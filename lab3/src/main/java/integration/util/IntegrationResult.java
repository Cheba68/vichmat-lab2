package integration.util;

public class IntegrationResult {

    private final double value;
    private final int n;
    private final double error;

    public IntegrationResult(
            double value,
            int n,
            double error
    ) {
        this.value = value;
        this.n = n;
        this.error = error;
    }

    public double getValue() {
        return value;
    }

    public int getN() {
        return n;
    }

    public double getError() {
        return error;
    }
}