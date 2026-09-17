package approximation.approximation;

import java.util.List;

import approximation.math.Statistics;
import approximation.model.ApproximationResult;
import approximation.model.DataPoint;

public class LinearApproximation implements ApproximationFunction {

    private double a;
    private double b;

    public ApproximationResult calculate(
            List<DataPoint> points
    ) {

        int n = points.size();

        double sumX = 0.0;
        double sumY = 0.0;
        double sumX2 = 0.0;
        double sumXY = 0.0;

        for (DataPoint point : points) {

            double x = point.getX();
            double y = point.getY();

            sumX += x;
            sumY += y;
            sumX2 += x * x;
            sumXY += x * y;
        }

        double denominator =
                n * sumX2 - sumX * sumX;

        if (Math.abs(denominator) < 1e-12) {

            throw new IllegalArgumentException(
                    "Невозможно построить линейную аппроксимацию."
            );
        }

        a =
                (n * sumXY - sumX * sumY)
                / denominator;

        b =
                (sumY - a * sumX)
                / n;

        double[] approximatedY =
                new double[n];

        double[] errors =
                new double[n];

        for (int i = 0; i < n; i++) {

            double x = points.get(i).getX();
            double y = points.get(i).getY();

            approximatedY[i] =
                    evaluate(x);

            errors[i] =
                    approximatedY[i] - y;
        }

        double sumSquaredErrors =
                Statistics.calculateSumSquaredErrors(
                        points,
                        approximatedY
                );

        double standardDeviation =
                Statistics.calculateStandardDeviation(
                        sumSquaredErrors,
                        n
                );

        double rSquared =
                Statistics.calculateRSquared(
                        points,
                        sumSquaredErrors
                );

        return new ApproximationResult(
                "Линейная",
                new double[]{a, b},
                approximatedY,
                errors,
                sumSquaredErrors,
                standardDeviation,
                rSquared
        );
    }

    public double evaluate(double x) {
        return a * x + b;
    }

    @Override
public String getName() {
    return "Линейная";
}
}