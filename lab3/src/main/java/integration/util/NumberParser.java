package integration.util;

import java.math.BigDecimal;

public final class NumberParser {

    private NumberParser() {
    }

    public static BigDecimal parse(String text) {

        if (text == null) {
            throw new IllegalArgumentException(
                    "Числовое значение не может быть пустым."
            );
        }

        String value = text.trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException(
                    "Числовое поле не заполнено."
            );
        }

        value = value.replace(',', '.');

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Некорректное числовое значение: \""
                            + text
                            + "\".\n"
                            + "Введите число, например: 2,5 или 2.5."
            );
        }
    }

    public static double parseDouble(String text) {

        BigDecimal value = parse(text);

        double result = value.doubleValue();

        if (Double.isInfinite(result)) {
            throw new IllegalArgumentException(
                    "Число слишком велико для вычислений."
            );
        }

        return result;
    }

    public static double parsePositiveDouble(String text) {

        BigDecimal value = parse(text);

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Значение должно быть больше нуля."
            );
        }

        double result = value.doubleValue();

        if (Double.isInfinite(result)) {
            throw new IllegalArgumentException(
                    "Число слишком велико для вычислений."
            );
        }

        if (result == 0.0) {
            throw new IllegalArgumentException(
                    "Число слишком мало для вычислений типа double."
            );
        }

        return result;
    }
}