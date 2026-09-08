package integration.method;

import integration.function.MathFunction;

public class NewtonCotesMethod implements IntegrationMethod {

    private static final int N = 6;

    private static final int[] WEIGHTS = {
            41, 216, 27, 272, 27, 216, 41
    };

    @Override
    public double integrate(
            MathFunction function,
            double a,
            double b,
            int n
    ) {

        if (n != N) {
            throw new IllegalArgumentException(
                    "Для формулы Ньютона-Котеса в этой " +
                    "вычислительной части используется n = 6."
            );
        }

        double h = (b - a) / N;

        double sum = 0.0;

        for (int i = 0; i <= N; i++) {

            double x = a + i * h;

            sum += WEIGHTS[i] * function.evaluate(x);
        }

        return h * sum / 840.0;
    }

    @Override
    public String getName() {
        return "Формула Ньютона-Котеса (n = 6)";
    }

    @Override
public int getOrder() {
    return 6;
}
}