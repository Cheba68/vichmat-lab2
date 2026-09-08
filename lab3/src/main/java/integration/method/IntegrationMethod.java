package integration.method;

import integration.function.MathFunction;

public interface IntegrationMethod {

    double integrate(
            MathFunction function,
            double a,
            double b,
            int n
    );

    String getName();

    int getOrder();
}