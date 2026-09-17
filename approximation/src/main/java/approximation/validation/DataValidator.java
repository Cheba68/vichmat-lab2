package approximation.validation;

import java.util.List;

import approximation.model.DataPoint;

public class DataValidator {

    public static final int MIN_POINTS = 8;
    public static final int MAX_POINTS = 12;

    public static void validateForCalculation(
            List<DataPoint> points
    ) throws ValidationException {

        if (points.size() < MIN_POINTS) {
            throw new ValidationException(
                    "Для расчёта необходимо минимум 8 точек."
            );
        }

        if (points.size() > MAX_POINTS) {
            throw new ValidationException(
                    "Количество точек не может превышать 12."
            );
        }

        for (int i = 0; i < points.size(); i++) {

            DataPoint point = points.get(i);

            if (!Double.isFinite(point.getX())) {
                throw new ValidationException(
                        "X[" + (i + 1) + "] имеет некорректное значение."
                );
            }

            if (!Double.isFinite(point.getY())) {
                throw new ValidationException(
                        "Y[" + (i + 1) + "] имеет некорректное значение."
                );
            }

            for (int j = i + 1; j < points.size(); j++) {

                if (Double.compare(
                        point.getX(),
                        points.get(j).getX()
                ) == 0) {

                    throw new ValidationException(
                            "Обнаружены одинаковые значения X: "
                                    + point.getX()
                    );
                }
            }
        }
    }
}