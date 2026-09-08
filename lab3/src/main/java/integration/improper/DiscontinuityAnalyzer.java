package integration.improper;

import integration.function.MathFunction;

public class DiscontinuityAnalyzer {

    private static final int TEST_STEPS = 12;

    public DiscontinuityAnalysis analyze(
            MathFunction function,
            double a,
            double b,
            double point
    ) {

        DiscontinuityInfo.Type location;

        if (approximatelyEqual(point, a)) {

            location =
                    DiscontinuityInfo.Type.AT_LEFT;

        } else if (approximatelyEqual(point, b)) {

            location =
                    DiscontinuityInfo.Type.AT_RIGHT;

        } else {

            location =
                    DiscontinuityInfo.Type.INSIDE;
        }

        DiscontinuityAnalysis.Behavior left =
                analyzeLeft(
                        function,
                        point
                );

        DiscontinuityAnalysis.Behavior right =
                analyzeRight(
                        function,
                        point
                );

        return new DiscontinuityAnalysis(
                point,
                location,
                left,
                right
        );
    }

    private DiscontinuityAnalysis.Behavior analyzeLeft(
            MathFunction function,
            double point
    ) {

        double lastFiniteValue = 0.0;
        boolean hasFiniteValue = false;

        for (int i = 1; i <= TEST_STEPS; i++) {

            double delta =
                    Math.pow(10, -i);

            double x = point - delta;

            double value =
                    function.evaluate(x);

            if (!isFinite(value)) {

                continue;
            }

            lastFiniteValue = value;
            hasFiniteValue = true;
        }

        if (!hasFiniteValue) {
            return DiscontinuityAnalysis.Behavior.UNDEFINED;
        }

        if (Math.abs(lastFiniteValue) > 1e10) {

            if (lastFiniteValue > 0) {
                return DiscontinuityAnalysis.Behavior
                        .POSITIVE_INFINITY;
            }

            return DiscontinuityAnalysis.Behavior
                    .NEGATIVE_INFINITY;
        }

        return DiscontinuityAnalysis.Behavior.FINITE;
    }

    private DiscontinuityAnalysis.Behavior analyzeRight(
            MathFunction function,
            double point
    ) {

        double lastFiniteValue = 0.0;
        boolean hasFiniteValue = false;

        for (int i = 1; i <= TEST_STEPS; i++) {

            double delta =
                    Math.pow(10, -i);

            double x = point + delta;

            double value =
                    function.evaluate(x);

            if (!isFinite(value)) {

                continue;
            }

            lastFiniteValue = value;
            hasFiniteValue = true;
        }

        if (!hasFiniteValue) {
            return DiscontinuityAnalysis.Behavior.UNDEFINED;
        }

        if (Math.abs(lastFiniteValue) > 1e10) {

            if (lastFiniteValue > 0) {
                return DiscontinuityAnalysis.Behavior
                        .POSITIVE_INFINITY;
            }

            return DiscontinuityAnalysis.Behavior
                    .NEGATIVE_INFINITY;
        }

        return DiscontinuityAnalysis.Behavior.FINITE;
    }

    private boolean isFinite(double value) {

        return !Double.isNaN(value)
                && !Double.isInfinite(value);
    }

    private boolean approximatelyEqual(
            double first,
            double second
    ) {

        return Math.abs(first - second)
                <= 1e-12
                * Math.max(
                        1.0,
                        Math.max(
                                Math.abs(first),
                                Math.abs(second)
                        )
                );
    }
}