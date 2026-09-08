package integration.util;

import integration.function.MathFunction;
import integration.method.IntegrationMethod;
import java.math.BigDecimal;

public final class InputValidator {

    private InputValidator() {
    }

    public static void validateAll(
            MathFunction function,
            IntegrationMethod method,
            BigDecimal a,
            BigDecimal b,
            BigDecimal epsilon
    ) {

        validateFunction(function);
        validateMethod(method);
        validateInterval(a, b);
        validateEpsilon(epsilon);
    }

    public static void validateFunction(
            MathFunction function
    ) {

        if (function == null) {
            throw new IllegalArgumentException(
                    "Функция не выбрана."
            );
        }
    }

    public static void validateMethod(
            IntegrationMethod method
    ) {

        if (method == null) {
            throw new IllegalArgumentException(
                    "Метод интегрирования не выбран."
            );
        }
    }

    public static void validateInterval(
        BigDecimal a,
        BigDecimal b
) {

    if (a == null) {
        throw new IllegalArgumentException(
                "Левая граница интегрирования не задана."
        );
    }

    if (b == null) {
        throw new IllegalArgumentException(
                "Правая граница интегрирования не задана."
        );
    }

    if (a.compareTo(b) == 0) {
        throw new IllegalArgumentException(
                "Левая и правая границы "
                        + "интегрирования не должны совпадать."
        );
    }

    if (a.compareTo(b) > 0) {
        throw new IllegalArgumentException(
                "Левая граница интегрирования "
                        + "должна быть меньше правой."
        );
    }
}

    public static void validateEpsilon(
            BigDecimal epsilon
    ) {

        if (epsilon == null) {
            throw new IllegalArgumentException(
                    "Точность вычисления не задана."
            );
        }

        if (epsilon.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Точность должна быть больше нуля."
            );
        }
    }

    public static void validatePartitions(
            int n
    ) {

        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Количество разбиений должно быть "
                            + "больше нуля."
            );
        }
    }

    public static void validateSimpsonPartitions(
            int n
    ) {

        validatePartitions(n);

        if (n % 2 != 0) {
            throw new IllegalArgumentException(
                    "Для метода Симпсона количество "
                            + "разбиений должно быть чётным."
            );
        }
    }
}