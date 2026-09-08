package integration.improper;

public class ImproperIntegralResult {

    public enum Status {
        ORDINARY,
        CONVERGENT,
        DIVERGENT,
        SYMMETRIC_CANCELLATION
    }

    private final Status status;
    private final double value;
    private final double error;
    private final int n;
    private final String message;

    public ImproperIntegralResult(
            Status status,
            double value,
            double error,
            int n,
            String message
    ) {
        this.status = status;
        this.value = value;
        this.error = error;
        this.n = n;
        this.message = message;
    }

    public Status getStatus() {
        return status;
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

    @Override 
    public String toString(){
        return status.toString() + " " + value + " " + error + " " + n + " " + message.toString();
    }
}