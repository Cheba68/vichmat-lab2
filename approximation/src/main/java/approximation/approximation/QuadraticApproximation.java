package approximation.approximation;

import java.util.List;

import approximation.math.MatrixSolver;
import approximation.math.Statistics;
import approximation.model.ApproximationResult;
import approximation.model.DataPoint;

public class QuadraticApproximation implements ApproximationFunction {

    private double a;
    private double b;
    private double c;

    public ApproximationResult calculate(
            List<DataPoint> points
    ) {

        int n = points.size();

        double sumX = 0.0;
        double sumX2 = 0.0;
        double sumX3 = 0.0;
        double sumX4 = 0.0;

        double sumY = 0.0;
        double sumXY = 0.0;
        double sumX2Y = 0.0;

        for (DataPoint point : points) {

            double x = point.getX();
            double y = point.getY();

            double x2 = x * x;
            double x3 = x2 * x;
            double x4 = x3 * x;

            sumX += x;
            sumX2 += x2;
            sumX3 += x3;
            sumX4 += x4;

            sumY += y;
            sumXY += x * y;
            sumX2Y += x2 * y;
        }

        double[][] matrix = {
                {sumX4, sumX3, sumX2},
                {sumX3, sumX2, sumX},
                {sumX2, sumX, n}
        };

        double[] rightSide = {
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
                "Квадратичная",
                new double[]{a, b, c},
                approximatedY,
                errors,
                sumSquaredErrors,
                standardDeviation,
                rSquared
        );
    }

    public double evaluate(double x) {
        return a * x * x + b * x + c;
    }

    @Override
public String getName() {
    return "Квадратичная";
}
}