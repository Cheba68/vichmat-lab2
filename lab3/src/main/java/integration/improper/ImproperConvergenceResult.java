package integration.improper;

public class ImproperConvergenceResult {

    private final boolean convergent;
    private final double value;
    private final double error;
    private final int n;
    private final String message;

    public ImproperConvergenceResult(
            boolean convergent,
            double value,
            double error,
            int n,
            String message
    ) {
        this.convergent = convergent;
        this.value = value;
        this.error = error;
        this.n = n;
        this.message = message;
    }

    public boolean isConvergent() {
        return convergent;
    }

    public double getValue() {
        return value;
    }

    public double getError() {
        return error;
    }

    public int getN() {
        return n;
    }

    public String getMessage() {
        return message;
    }
}