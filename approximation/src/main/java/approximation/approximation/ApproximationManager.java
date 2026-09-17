package approximation.approximation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import approximation.model.ApproximationError;
import approximation.model.ApproximationResult;
import approximation.model.DataPoint;

public class ApproximationManager {

    private final List<ApproximationFunction> functions;
    private final List<ApproximationError> errors;

    public ApproximationManager() {

        functions = List.of(
                new LinearApproximation(),
                new QuadraticApproximation(),
                new CubicApproximation(),
                new ExponentialApproximation(),
                new LogarithmicApproximation(),
                new PowerApproximation()
        );
        errors = new ArrayList<>();
    }

    public List<ApproximationResult> calculateAll(
        List<DataPoint> points
) {

    List<ApproximationResult> results =
            new ArrayList<>();

    errors.clear();

    for (ApproximationFunction function : functions) {

        try {

            ApproximationResult result =
                    function.calculate(points);

            results.add(result);

        } catch (IllegalArgumentException e) {

            errors.add(
                    new ApproximationError(
                            function.getName(),
                            e.getMessage()
                    )
            );
        }
    }

    

    results.sort(
            Comparator.comparingDouble(
                    ApproximationResult::getStandardDeviation
            )
    );

    return results;
}

public List<ApproximationError> getErrors() {
    return errors;
}
    public ApproximationResult findBest(
            List<ApproximationResult> results
    ) {

        if (results.isEmpty()) {

            throw new IllegalArgumentException(
                    "Не удалось построить ни одну аппроксимацию."
            );
        }

        return results.get(0);
    }

    public ApproximationFunction findFunction(
        ApproximationResult result
) {

    for (ApproximationFunction function : functions) {

        if (function.getName().equals(result.getName())) {
            return function;
        }
    }

    throw new IllegalArgumentException(
            "Не удалось найти функцию для результата."
    );
}
}