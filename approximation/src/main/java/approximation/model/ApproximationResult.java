package approximation.model;

public class ApproximationResult {

    private final String name;
    private final double[] coefficients;
    private final double[] approximatedY;
    private final double[] errors;
    private final double sumSquaredErrors;
    private final double standardDeviation;
    private final double rSquared;

    public ApproximationResult(
            String name,
            double[] coefficients,
            double[] approximatedY,
            double[] errors,
            double sumSquaredErrors,
            double standardDeviation,
            double rSquared
    ) {
        this.name = name;
        this.coefficients = coefficients;
        this.approximatedY = approximatedY;
        this.errors = errors;
        this.sumSquaredErrors = sumSquaredErrors;
        this.standardDeviation = standardDeviation;
        this.rSquared = rSquared;
    }

    public String getName() {
        return name;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public double[] getApproximatedY() {
        return approximatedY;
    }

    public double[] getErrors() {
        return errors;
    }

    public double getSumSquaredErrors() {
        return sumSquaredErrors;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public double getRSquared() {
        return rSquared;
    }
}