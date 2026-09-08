package integration.method;

import integration.function.MathFunction;

public class RectangleMethod implements IntegrationMethod {

    public enum Type {
        LEFT,
        RIGHT,
        MIDDLE
    }

    private final Type type;

    public RectangleMethod(Type type) {
        this.type = type;
    }

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
        double sum = 0.0;

        for (int i = 0; i < n; i++) {

            double x;

            switch (type) {

                case LEFT:
                    x = a + i * h;
                    break;

                case RIGHT:
                    x = a + (i + 1) * h;
                    break;

                case MIDDLE:
                    x = a + (i + 0.5) * h;
                    break;

                default:
                    throw new IllegalStateException(
                            "Неизвестный тип метода прямоугольников."
                    );
            }

            sum += function.evaluate(x);
        }

        return h * sum;
    }

    @Override
    public String getName() {

        switch (type) {

            case LEFT:
                return "Метод левых прямоугольников";

            case RIGHT:
                return "Метод правых прямоугольников";

            case MIDDLE:
                return "Метод средних прямоугольников";

            default:
                return "Метод прямоугольников";
        }
    }
    @Override
public int getOrder() {

    switch (type) {

        case LEFT:
        case RIGHT:
            return 1;

        case MIDDLE:
            return 2;

        default:
            throw new IllegalStateException(
                    "Неизвестный тип метода прямоугольников."
            );
    }
}
}