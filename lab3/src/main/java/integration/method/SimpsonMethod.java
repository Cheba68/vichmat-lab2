package integration.method;

import integration.function.MathFunction;

public class SimpsonMethod implements IntegrationMethod {

    @Override
    public double integrate(
            MathFunction function,
            double a,
            double b,
            int n
    ) {

        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Количество разбиений должно быть больше нуля."
            );
        }

        if (n % 2 != 0) {
            throw new IllegalArgumentException(
                    "Для метода Симпсона количество разбиений " +
                    "должно быть чётным."
            );
        }

        double h = (b - a) / n;

        double sum = function.evaluate(a)
                + function.evaluate(b);

        for (int i = 1; i < n; i++) {

            double x = a + i * h;

            if (i % 2 == 0) {
                sum += 2 * function.evaluate(x);
            } else {
                sum += 4 * function.evaluate(x);
            }
        }

        return (h / 3.0) * sum;
    }

    @Override
    public String getName() {
        return "Метод Симпсона";
    }

    @Override
public int getOrder() {
    return 4;
}
}