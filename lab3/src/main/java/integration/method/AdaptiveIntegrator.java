package integration.method;

import integration.function.MathFunction;
import integration.util.IntegrationResult;
import integration.util.RungeRule;

public class AdaptiveIntegrator {

    private static final int INITIAL_N = 4;

    private static final int MAX_ITERATIONS = 100;

    public IntegrationResult integrate(
            IntegrationMethod method,
            MathFunction function,
            double a,
            double b,
            double epsilon
    ) {

        if (epsilon <= 0.0) {
            throw new IllegalArgumentException(
                    "Точность должна быть больше нуля."
            );
        }

        int n = INITIAL_N;

        for (int iteration = 0;
             iteration < MAX_ITERATIONS;
             iteration++) {

            double resultN =
        method.integrate(
                function,
                a,
                b,
                n
        );

if (n > Integer.MAX_VALUE / 2) {
    throw new IllegalStateException(
            "Не удалось достичь заданной точности."
    );
}

int nextN = n * 2;

double result2N =
        method.integrate(
                function,
                a,
                b,
                nextN
        );

            double error =
                    RungeRule.estimate(
                            resultN,
                            result2N,
                            method.getOrder()
                    );

            if (error <= epsilon) {

                return new IntegrationResult(
                        result2N,
                        nextN,
                        error
                );
            }

            n = nextN;
        }

        throw new IllegalStateException(
                "Не удалось достичь заданной точности "
                        + "за допустимое количество итераций."
        );
    }
}