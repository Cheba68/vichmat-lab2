package integration.method;

import integration.function.MathFunction;

public class TrapezoidMethod implements IntegrationMethod {

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

        double h = (b - a) / n;

        double sum = (function.evaluate(a)
                + function.evaluate(b)) / 2.0;

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += function.evaluate(x);
        }

        return h * sum;
    }

    @Override
    public String getName() {
        return "Метод трапеций";
    }

    @Override                                                                                       
public int getOrder() {
    return 2;
}
}