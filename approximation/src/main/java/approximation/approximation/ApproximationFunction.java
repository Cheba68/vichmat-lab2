package approximation.approximation;

import java.util.List;

import approximation.model.ApproximationResult;
import approximation.model.DataPoint;

public interface ApproximationFunction {

    ApproximationResult calculate(
            List<DataPoint> points
    );

    double evaluate(double x);

    String getName();
}