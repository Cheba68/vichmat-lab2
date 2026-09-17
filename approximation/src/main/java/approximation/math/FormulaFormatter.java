package approximation.math;

import approximation.model.ApproximationResult;

public class FormulaFormatter {

    public static String format(ApproximationResult result) {
        double[] c = result.getCoefficients();

        return switch (result.getName()) {

            case "Линейная" ->
                    "y = "
                            + NumberFormatter.format(c[0])
                            + "x "
                            + formatSigned(c[1]);

            case "Квадратичная" ->
                    "y = "
                            + NumberFormatter.format(c[0])
                            + "x² "
                            + formatSigned(c[1])
                            + "x "
                            + formatSigned(c[2]);

            case "Кубическая" ->
                    "y = "
                            + NumberFormatter.format(c[0])
                            + "x³ "
                            + formatSigned(c[1])
                            + "x² "
                            + formatSigned(c[2])
                            + "x "
                            + formatSigned(c[3]);

            case "Экспоненциальная" ->
                    "y = "
                            + NumberFormatter.format(c[0])
                            + " · e^("
                            + NumberFormatter.format(c[1])
                            + "x)";

            case "Логарифмическая" ->
                    "y = "
                            + NumberFormatter.format(c[0])
                            + " · ln(x) "
                            + formatSigned(c[1]);

            case "Степенная" ->
                    "y = "
                            + NumberFormatter.format(c[0])
                            + " · x^("
                            + NumberFormatter.format(c[1])
                            + ")";

            default -> "Неизвестная функция";
        };
    }

    private static String formatSigned(double value) {
        if (value >= 0) {
            return "+ " + NumberFormatter.format(value);
        }

        return "- " + NumberFormatter.format(Math.abs(value));
    }
}