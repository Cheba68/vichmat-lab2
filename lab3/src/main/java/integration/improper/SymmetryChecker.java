package integration.improper;

import integration.function.MathFunction;

public class SymmetryChecker {

    private static final double[] TEST_DISTANCES = {
            0.1,
            0.01,
            0.001,
            0.0001
    };

    private static final double RELATIVE_TOLERANCE = 1e-8;

    public boolean isOddAroundPoint(
            MathFunction function,
            double point
    ) {

        boolean hasValidTest = false;
        for (double distance : TEST_DISTANCES) {

            double leftX = point - distance;
            double rightX = point + distance;

            double leftValue =
                    function.evaluate(leftX);

            double rightValue =
                    function.evaluate(rightX);

            if (!isFinite(leftValue)
                    || !isFinite(rightValue)) {

                continue;
            }

            hasValidTest = true;

            double scale =
                    Math.max(
                            1.0,
                            Math.max(
                                    Math.abs(leftValue),
                                    Math.abs(rightValue)
                            )
                    );

            double difference =
                    Math.abs(
                            leftValue + rightValue
                    );

            if (difference
                    > RELATIVE_TOLERANCE * scale) {

                return false;
            }
        }

        return hasValidTest;
    }

    private boolean isFinite(double value) {

        return !Double.isNaN(value)
                && !Double.isInfinite(value);
    }
}