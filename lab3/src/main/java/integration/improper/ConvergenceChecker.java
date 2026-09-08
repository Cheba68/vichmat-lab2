package integration.improper;

import integration.function.MathFunction;

public class ConvergenceChecker {

    private static final int TEST_STEPS = 12;

    private static final double MAX_ALLOWED_VALUE = 1e12;

    public boolean convergesFromLeft(
            MathFunction function,
            double point,
            double outerPoint
    ) {

        double previousMagnitude = -1.0;

        for (int i = 1; i <= TEST_STEPS; i++) {

            double distance =
                    Math.abs(outerPoint - point)
                            * Math.pow(0.1, i);

            double x;

            if (outerPoint > point) {
                x = point + distance;
            } else {
                x = point - distance;
            }

            double value =
                    function.evaluate(x);

            if (!isFinite(value)) {
                return false;
            }

            double magnitude =
                    Math.abs(value);

            if (magnitude > MAX_ALLOWED_VALUE) {
                return false;
            }

            /*
             * Для разрыва второго рода значение
             * должно расти при приближении к точке.
             */
            if (previousMagnitude >= 0
                    && magnitude < previousMagnitude) {

                return false;
            }

            previousMagnitude = magnitude;
        }

        return true;
    }

    private boolean isFinite(double value) {

        return !Double.isNaN(value)
                && !Double.isInfinite(value);
    }
}