package integration.util;

public final class RungeRule {

    private RungeRule() {
    }

    /**
     * Оценка погрешности по правилу Рунге.
     *
     * @param resultN  результат при n разбиениях
     * @param result2N результат при 2n разбиениях
     * @param order порядок метода
     * @return оценка погрешности
     */
    public static double estimate(
            double resultN,
            double result2N,
            int order
    ) {

        if (order <= 0) {
            throw new IllegalArgumentException(
                    "Порядок метода должен быть больше нуля."
            );
        }

        double denominator =
                Math.pow(2, order) - 1;

        return Math.abs(result2N - resultN)
                / denominator;
    }
}