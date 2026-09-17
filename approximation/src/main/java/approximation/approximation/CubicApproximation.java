package approximation.approximation;

import java.util.List;

import approximation.math.MatrixSolver;
import approximation.math.Statistics;
import approximation.model.ApproximationResult;
import approximation.model.DataPoint;

public class CubicApproximation implements ApproximationFunction {

    private double a;
    private double b;
    private double c;
    private double d;

    public ApproximationResult calculate(
            List<DataPoint> points
    ) {

        int n = points.size();

        double sumX = 0.0;
        double sumX2 = 0.0;
        double sumX3 = 0.0;
        double sumX4 = 0.0;
        double sumX5 = 0.0;
        double sumX6 = 0.0;

        double sumY = 0.0;
        double sumXY = 0.0;
        double sumX2Y = 0.0;
        double sumX3Y = 0.0;

        for (DataPoint point : points) {

            double x = point.getX();
            double y = point.getY();

            double x2 = x * x;
            double x3 = x2 * x;
            double x4 = x3 * x;
            double x5 = x4 * x;
            double x6 = x5 * x;

            sumX += x;
            sumX2 += x2;
            sumX3 += x3;
            sumX4 += x4;
            sumX5 += x5;
            sumX6 += x6;

            sumY += y;
            sumXY += x * y;
            sumX2Y += x2 * y;
            sumX3Y += x3 * y;
        }

        double[][] matrix = {
                {sumX6, sumX5, sumX4, sumX3},
                {sumX5, sumX4, sumX3, sumX2},
                {sumX4, sumX3, sumX2, sumX},
                {sumX3, sumX2, sumX, n}
        };

        double[] rightSide = {
                sumX3Y,
                sumX2Y,
                sumXY,
                sumY
        };

        double[] coefficients =
                MatrixSolver.solve(
                        matrix,
                        rightSide
                );

        a = coefficients[0];
        b = coefficients[1];
        c = coefficients[2];
        d = coefficients[3];

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
                "Кубическая",
                new double[]{a, b, c, d},
                approximatedY,
                errors,
                sumSquaredErrors,
                standardDeviation,
                rSquared
        );
    }

    public double evaluate(double x) {
        return a * x * x * x
                + b * x * x
                + c * x
                + d;
    }

    @Override
public String getName() {
    return "Кубическая";
}
}