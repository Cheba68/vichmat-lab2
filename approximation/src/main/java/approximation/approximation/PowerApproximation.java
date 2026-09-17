package approximation.approximation;

import java.util.List;

import approximation.math.Statistics;
import approximation.model.ApproximationResult;
import approximation.model.DataPoint;

public class PowerApproximation implements ApproximationFunction{

    private double a;
    private double b;

    public ApproximationResult calculate(
            List<DataPoint> points
    ) {

        int n = points.size();

        double sumLnX = 0.0;
        double sumLnY = 0.0;
        double sumLnX2 = 0.0;
        double sumLnXLnY = 0.0;

        for (DataPoint point : points) {

            double x = point.getX();
            double y = point.getY();

            if (x <= 0 || y <= 0) {
                throw new IllegalArgumentException(
                        "Степенная аппроксимация требует X > 0 и Y > 0."
                );
            }

            double lnX = Math.log(x);
            double lnY = Math.log(y);

            sumLnX += lnX;
            sumLnY += lnY;
            sumLnX2 += lnX * lnX;
            sumLnXLnY += lnX * lnY;
        }

        double denominator =
                n * sumLnX2 - sumLnX * sumLnX;

        if (Math.abs(denominator) < 1e-12) {
            throw new IllegalArgumentException(
                    "Невозможно построить степенную аппроксимацию."
            );
        }

        b =
                (n * sumLnXLnY - sumLnX * sumLnY)
                / denominator;

        double lnA =
                (sumLnY - b * sumLnX)
                / n;

        a = Math.exp(lnA);

        double[] approximatedY =
                new double[n];

        double[] errors =
                new double[n];

        for (int i = 0; i < n; i++) {

            double x =
                    points.get(i).getX();

            double y =
                    points.get(i).getY();

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
                "Степенная",
                new double[]{a, b},
                approximatedY,
                errors,
                sumSquaredErrors,
                standardDeviation,
                rSquared
        );
    }

    public double evaluate(double x) {
        return a * Math.pow(x, b);
    }

    @Override
public String getName() {
    return "Степенная";
}
}