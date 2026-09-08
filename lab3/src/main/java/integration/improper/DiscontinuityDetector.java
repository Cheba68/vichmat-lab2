package integration.improper;

import integration.function.MathFunction;

public class DiscontinuityDetector {

    private static final double RELATIVE_EPSILON = 1e-10;

    public DiscontinuityInfo detect(
        MathFunction function,
        double a,
        double b
) {

    if (function == null) {
        throw new IllegalArgumentException(
                "Функция не выбрана."
        );
    }

    double left = Math.min(a, b);
    double right = Math.max(a, b);

    /*
     * Сначала проверяем точки разрыва,
     * которые сама функция явно сообщает.
     */
    double[] knownPoints =
            function.getDiscontinuityPoints();

    for (double point : knownPoints) {

        if (point < left || point > right) {
            continue;
        }

        if (approximatelyEqual(point, left)) {

            return new DiscontinuityInfo(
                    DiscontinuityInfo.Type.AT_LEFT,
                    point
            );
        }

        if (approximatelyEqual(point, right)) {

            return new DiscontinuityInfo(
                    DiscontinuityInfo.Type.AT_RIGHT,
                    point
            );
        }

        return new DiscontinuityInfo(
                DiscontinuityInfo.Type.INSIDE,
                point
        );
    }

    /*
     * Если известных точек нет,
     * выполняем обычную проверку границ.
     */
    if (!isFinite(function.evaluate(left))) {

        return new DiscontinuityInfo(
                DiscontinuityInfo.Type.AT_LEFT,
                left
        );
    }

    if (!isFinite(function.evaluate(right))) {

        return new DiscontinuityInfo(
                DiscontinuityInfo.Type.AT_RIGHT,
                right
        );
    }

    return new DiscontinuityInfo(
            DiscontinuityInfo.Type.NONE,
            Double.NaN
    );
}

private boolean approximatelyEqual(
        double first,
        double second
) {

    return Math.abs(first - second)
            <= RELATIVE_EPSILON
            * Math.max(
                    1.0,
                    Math.max(
                            Math.abs(first),
                            Math.abs(second)
                    )
            );
}

    private boolean isFinite(double value) {

        return !Double.isNaN(value)
                && !Double.isInfinite(value);
    }

    private boolean isPossibleDiscontinuity(
            double first,
            double second
    ) {

        if (!isFinite(first)
                || !isFinite(second)) {

            return true;
        }

        double scale =
                Math.max(
                        1.0,
                        Math.max(
                                Math.abs(first),
                                Math.abs(second)
                        )
                );

        return Math.abs(first - second)
                > 1000.0 * scale;
    }

    private double findPossiblePoint(
            MathFunction function,
            double left,
            double right
    ) {

        double l = left;
        double r = right;

        for (int i = 0; i < 60; i++) {

            double middle =
                    (l + r) / 2.0;

            double value =
                    function.evaluate(middle);

            if (!isFinite(value)) {
                r = middle;
            } else {

                double leftValue =
                        function.evaluate(l);

                if (!isFinite(leftValue)) {
                    l = middle;
                } else {
                    r = middle;
                }
            }
        }

        return (l + r) / 2.0;
    }
}