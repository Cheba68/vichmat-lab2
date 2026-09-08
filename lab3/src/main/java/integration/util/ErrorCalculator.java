package integration.util;

public final class ErrorCalculator {

    private ErrorCalculator() {
    }

    /**
     * Абсолютная погрешность:
     *
     * |I_approx - I_exact|
     */
    public static double absoluteError(
            double approximate,
            double exact
    ) {
        return Math.abs(approximate - exact);
    }

    /**
     * Относительная погрешность:
     *
     * |I_approx - I_exact| / |I_exact|
     */
    public static double relativeError(
            double approximate,
            double exact
    ) {
        if (exact == 0.0) {
            throw new IllegalArgumentException(
                    "Невозможно вычислить относительную " +
                    "погрешность: точное значение интеграла равно нулю."
            );
        }

        return Math.abs(approximate - exact)
                / Math.abs(exact);
    }

    /**
     * Относительная погрешность в процентах.
     */
    public static double relativeErrorPercent(
            double approximate,
            double exact
    ) {
        return relativeError(approximate, exact) * 100.0;
    }
}