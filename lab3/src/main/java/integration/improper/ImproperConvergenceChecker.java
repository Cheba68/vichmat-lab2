package integration.improper;

import integration.function.MathFunction;
import integration.method.AdaptiveIntegrator;
import integration.method.IntegrationMethod;
import integration.util.IntegrationResult;

public class ImproperConvergenceChecker {

    private static final int MAX_STEPS = 15;

    private static final double MIN_DELTA = 1e-14;

    private final AdaptiveIntegrator integrator;

    public ImproperConvergenceChecker() {
        integrator = new AdaptiveIntegrator();
    }

    public ImproperConvergenceResult fromLeft(
            MathFunction function,
            IntegrationMethod method,
            double a,
            double b,
            double epsilon
    ) {

        double previous = Double.NaN;

        double lastValue = Double.NaN;
        double lastError = Double.NaN;
        int lastN = 0;

        for (int i = 1; i <= MAX_STEPS; i++) {

            double delta =
                    Math.pow(10.0, -i);

            if (delta < MIN_DELTA) {
                break;
            }

            double start = a + delta;

            if (start >= b) {
                continue;
            }

            IntegrationResult result;

            try {

                result =
                        integrator.integrate(
                                method,
                                function,
                                start,
                                b,
                                epsilon
                        );

            } catch (RuntimeException e) {

                return new ImproperConvergenceResult(
                        false,
                        Double.NaN,
                        Double.NaN,
                        0,
                        "Интеграл не существует."
                );
            }

            double value =
                    result.getValue();

            if (!Double.isFinite(value)) {

                return new ImproperConvergenceResult(
                        false,
                        Double.NaN,
                        Double.NaN,
                        0,
                        "Интеграл не существует."
                );
            }

            lastValue = value;
            lastError = result.getError();
            lastN = result.getN();

            if (Double.isFinite(previous)) {

                double difference =
                        Math.abs(
                                value - previous
                        );

                /*
                 * Если два последовательных
                 * приближения отличаются меньше
                 * заданной точности, считаем
                 * последовательность устойчивой.
                 */
                if (difference <= epsilon) {

                    return new ImproperConvergenceResult(
                            true,
                            value,
                            lastError + difference,
                            lastN,
                            "Несобственный интеграл "
                                    + "сходится."
                    );
                }
            }

            previous = value;
        }

        /*
         * Последовательность не стабилизировалась.
         */
        return new ImproperConvergenceResult(
                false,
                Double.NaN,
                Double.NaN,
                0,
                "Интеграл не существует."
        );
    }

    public ImproperConvergenceResult fromRight(
            MathFunction function,
            IntegrationMethod method,
            double a,
            double b,
            double epsilon
    ) {

        double previous = Double.NaN;

        double lastValue = Double.NaN;
        double lastError = Double.NaN;
        int lastN = 0;

        for (int i = 1; i <= MAX_STEPS; i++) {

            double delta =
                    Math.pow(10.0, -i);

            if (delta < MIN_DELTA) {
                break;
            }

            double end = b - delta;

            if (end <= a) {
                continue;
            }

            IntegrationResult result;

            try {

                result =
                        integrator.integrate(
                                method,
                                function,
                                a,
                                end,
                                epsilon
                        );

            } catch (RuntimeException e) {

                return new ImproperConvergenceResult(
                        false,
                        Double.NaN,
                        Double.NaN,
                        0,
                        "Интеграл не существует."
                );
            }

            double value =
                    result.getValue();

            if (!Double.isFinite(value)) {

                return new ImproperConvergenceResult(
                        false,
                        Double.NaN,
                        Double.NaN,
                        0,
                        "Интеграл не существует."
                );
            }

            lastValue = value;
            lastError = result.getError();
            lastN = result.getN();

            if (Double.isFinite(previous)) {

                double difference =
                        Math.abs(
                                value - previous
                        );

                if (difference <= epsilon) {

                    return new ImproperConvergenceResult(
                            true,
                            value,
                            lastError + difference,
                            lastN,
                            "Несобственный интеграл "
                                    + "сходится."
                    );
                }
            }

            previous = value;
        }

        return new ImproperConvergenceResult(
                false,
                Double.NaN,
                Double.NaN,
                0,
                "Интеграл не существует."
        );
    }
}