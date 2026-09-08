package integration.function;

public interface MathFunction {

    double evaluate(double x);

    String getName();

    String getFormula();

    default double[] getDiscontinuityPoints() {
        return new double[0];
    }

    default boolean hasIntegrableSingularity() {
        return false;
    }
}