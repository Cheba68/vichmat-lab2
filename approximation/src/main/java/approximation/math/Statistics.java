package approximation.math;

import java.util.List;

import approximation.model.DataPoint;

public class Statistics {

    public static double calculateSumSquaredErrors(
            List<DataPoint> points,
            double[] approximatedY
    ) {

        double sum = 0.0;

        for (int i = 0; i < points.size(); i++) {

            double error =
                    approximatedY[i] - points.get(i).getY();

            sum += error * error;
        }

        return sum;
    }

    public static double calculateStandardDeviation(
            double sumSquaredErrors,
            int numberOfPoints
    ) {

        return Math.sqrt(
                sumSquaredErrors / numberOfPoints
        );
    }

    public static double calculateRSquared(
            List<DataPoint> points,
            double sumSquaredErrors
    ) {

        double meanY = 0.0;

        for (DataPoint point : points) {
            meanY += point.getY();
        }

        meanY /= points.size();

        double totalSumOfSquares = 0.0;

        for (DataPoint point : points) {

            double difference =
                    point.getY() - meanY;

            totalSumOfSquares += difference * difference;
        }

        if (Math.abs(totalSumOfSquares) < 1e-12) {
            return 1.0;
        }

        return 1.0 -
                sumSquaredErrors / totalSumOfSquares;
    }

    public static String getRSquaredMessage(double rSquared) {

    if (rSquared >= 0.9) {
        return "Очень хорошее соответствие экспериментальным данным.";
    }

    if (rSquared >= 0.7) {
        return "Хорошее соответствие экспериментальным данным.";
    }

    if (rSquared >= 0.5) {
        return "Удовлетворительное соответствие экспериментальным данным.";
    }

    return "Слабое соответствие экспериментальным данным.";
}

    public static double calculatePearsonCorrelation(
            List<DataPoint> points
    ) {

        int n = points.size();

        double sumX = 0.0;
        double sumY = 0.0;
        double sumX2 = 0.0;
        double sumY2 = 0.0;
        double sumXY = 0.0;

        for (DataPoint point : points) {

            double x = point.getX();
            double y = point.getY();

            sumX += x;
            sumY += y;
            sumX2 += x * x;
            sumY2 += y * y;
            sumXY += x * y;
        }

        double numerator =
                n * sumXY - sumX * sumY;

        double denominator =
                Math.sqrt(
                        (n * sumX2 - sumX * sumX) *
                        (n * sumY2 - sumY * sumY)
                );

        if (Math.abs(denominator) < 1e-12) {
            return 0.0;
        }

        return numerator / denominator;
    }
}