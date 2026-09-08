package integration.improper;

import integration.function.MathFunction;
import integration.method.AdaptiveIntegrator;
import integration.method.IntegrationMethod;
import integration.util.IntegrationResult;

public class ImproperIntegralHandler {

    private final DiscontinuityDetector detector;
    private final DiscontinuityAnalyzer analyzer;
    private final AdaptiveIntegrator integrator;
    private final SymmetryChecker symmetryChecker;
    private final ConvergenceChecker convergenceChecker;
    private final ImproperConvergenceChecker convergence;

    public ImproperIntegralHandler() {

    detector = new DiscontinuityDetector();

    analyzer = new DiscontinuityAnalyzer();

    integrator = new AdaptiveIntegrator();

    symmetryChecker = new SymmetryChecker();

    convergenceChecker =
        new ConvergenceChecker();

    convergence =
        new ImproperConvergenceChecker();
}

    public ImproperIntegralResult integrate(
            MathFunction function,
            IntegrationMethod method,
            double a,
            double b,
            double epsilon
    ) {

        DiscontinuityInfo discontinuity =
                detector.detect(
                        function,
                        a,
                        b
                );

        /*
         * Разрыва нет.
         */
        if (!discontinuity.exists()) {

            IntegrationResult result =
                    integrator.integrate(
                            method,
                            function,
                            a,
                            b,
                            epsilon
                    );

            return new ImproperIntegralResult(
                    ImproperIntegralResult.Status.ORDINARY,
                    result.getValue(),
                    result.getError(),
                    result.getN(),
                    "Разрывов на интервале не обнаружено."
            );
        }

        double point =
                discontinuity.getPoint();

        DiscontinuityAnalysis analysis =
                analyzer.analyze(
                        function,
                        a,
                        b,
                        point
                );

        /*
         * Разрыв внутри интервала.
         */
        if (analysis.getLocation()
                == DiscontinuityInfo.Type.INSIDE) {

            return handleInteriorDiscontinuity(
                    function,
                    method,
                    a,
                    b,
                    epsilon,
                    analysis
            );
        }

        /*
         * Пока отдельно возвращаем сообщение
         * для разрыва на границе.
         *
         * Полноценную проверку сходимости
         * реализуем следующим этапом.
         */
        return handleBoundaryDiscontinuity(
        function,
        method,
        a,
        b,
        epsilon,
        analysis
);
    }

    private ImproperIntegralResult
handleIntegrableSingularity(
        MathFunction function,
        IntegrationMethod method,
        double a,
        double b,
        double epsilon,
        double point
) {

    /*
     * Левая часть:
     *
     * x = point - t^2
     *
     * dx = -2t dt
     *
     * При x -> point:
     * t -> 0
     *
     * Поэтому точку разрыва
     * мы вообще не передаём
     * численному методу.
     */

    double leftLength =
            point - a;

    double rightLength =
            b - point;

    double leftValue = 0.0;
    double rightValue = 0.0;

    double leftError = 0.0;
    double rightError = 0.0;

    int maxN = 0;

    /*
     * Левая часть.
     */
    if (leftLength > 0.0) {

        MathFunction leftFunction =
                new MathFunction() {

                    @Override
public double evaluate(double t) {
    if (t == 0.0) {
        t = 1e-8;
    }

    double x =
            point - t * t;

    return function.evaluate(x)
            * 2.0 * t;
}

                    @Override
                    public String getName() {
                        return "Преобразованная левая часть";
                    }

                    @Override
public String getFormula() {
    return "Преобразованная левая часть";
}
                };

        IntegrationResult result =
                integrator.integrate(
                        method,
                        leftFunction,
                        0.0,
                        Math.sqrt(leftLength),
                        epsilon
                );

        leftValue =
                result.getValue();

        leftError =
                result.getError();

        maxN =
                Math.max(
                        maxN,
                        result.getN()
                );
    }

    /*
     * Правая часть:
     *
     * x = point + t^2
     * dx = 2t dt
     */
    if (rightLength > 0.0) {

        MathFunction rightFunction =
                new MathFunction() {

                    @Override
public double evaluate(double t) {
    if (t == 0.0) {
        return 0.0;
    }

    double x =
            point + t * t;

    return function.evaluate(x)
            * 2.0 * t;
}

                    @Override
                    public String getName() {
                        return "Преобразованная правая часть";
                    }

                    @Override
public String getFormula() {
    return "Преобразованная правая часть";
}
                };

        IntegrationResult result =
                integrator.integrate(
                        method,
                        rightFunction,
                        0.0,
                        Math.sqrt(rightLength),
                        epsilon
                );

        rightValue =
                result.getValue();

        rightError =
                result.getError();

        maxN =
                Math.max(
                        maxN,
                        result.getN()
                );
    }

    return new ImproperIntegralResult(
            ImproperIntegralResult.Status.CONVERGENT,
            leftValue + rightValue,
            leftError + rightError,
            maxN,
            "Несобственный интеграл "
                    + "с интегрируемой особенностью "
                    + "вычислен с помощью "
                    + "замены переменной."
    );
}

private ImproperIntegralResult
handleInteriorDiscontinuity(
        MathFunction function,
        IntegrationMethod method,
        double a,
        double b,
        double epsilon,
        DiscontinuityAnalysis analysis
) {

    double point =
            analysis.getPoint();

    if (function.hasIntegrableSingularity()) {

    return handleIntegrableSingularity(
            function,
            method,
            a,
            b,
            epsilon,
            point
    );
}

    /*
     * Сначала проверяем специальный случай
     * противоположных бесконечных пределов.
     */
    boolean oppositeInfiniteLimits =
            isOppositeInfiniteLimits(
                    analysis
            );

    if (oppositeInfiniteLimits) {

        return handleSymmetricCase(
                function,
                method,
                a,
                b,
                epsilon,
                point
        );
    }

    /*
     * Обычный случай внутреннего разрыва:
     *
     * [a, point) + (point, b]
     *
     * проверяем отдельно.
     */
    ImproperConvergenceResult leftResult =
            convergence.fromRight(
                    function,
                    method,
                    a,
                    point,
                    epsilon
            );

    /*
     * Если левая часть расходится,
     * весь интеграл расходится.
     */
    if (!leftResult.isConvergent()) {

        return new ImproperIntegralResult(
                ImproperIntegralResult.Status.DIVERGENT,
                Double.NaN,
                Double.NaN,
                0,
                "Интеграл не существует."
        );
    }

    ImproperConvergenceResult rightResult =
            convergence.fromLeft(
                    function,
                    method,
                    point,
                    b,
                    epsilon
            );

    /*
     * Если правая часть расходится,
     * весь интеграл расходится.
     */
    if (!rightResult.isConvergent()) {

        return new ImproperIntegralResult(
                ImproperIntegralResult.Status.DIVERGENT,
                Double.NaN,
                Double.NaN,
                0,
                "Интеграл не существует."
        );
    }

    /*
     * Обе части сходятся.
     */
    double value =
            leftResult.getValue()
                    + rightResult.getValue();

    double error =
            leftResult.getError()
                    + rightResult.getError();

    int n =
            Math.max(
                    leftResult.getN(),
                    rightResult.getN()
            );

    return new ImproperIntegralResult(
            ImproperIntegralResult.Status.CONVERGENT,
            value,
            error,
            n,
            "Обе части несобственного "
                    + "интеграла сходятся."
    );
}
    private boolean isOppositeInfiniteLimits(
            DiscontinuityAnalysis analysis
    ) {

        return
                analysis.getLeftBehavior()
                        == DiscontinuityAnalysis.Behavior
                        .NEGATIVE_INFINITY
                &&
                analysis.getRightBehavior()
                        == DiscontinuityAnalysis.Behavior
                        .POSITIVE_INFINITY

                ||

                analysis.getLeftBehavior()
                        == DiscontinuityAnalysis.Behavior
                        .POSITIVE_INFINITY
                &&
                analysis.getRightBehavior()
                        == DiscontinuityAnalysis.Behavior
                        .NEGATIVE_INFINITY;
    }

    private ImproperIntegralResult
handleSymmetricCase(
        MathFunction function,
        IntegrationMethod method,
        double a,
        double b,
        double epsilon,
        double point
) {

    double leftDistance =
            Math.abs(point - a);

    double rightDistance =
            Math.abs(b - point);

    double symmetricDistance =
            Math.min(
                    leftDistance,
                    rightDistance
            );

    double symmetricLeft =
            point - symmetricDistance;

    double symmetricRight =
            point + symmetricDistance;

    if (symmetricLeft >= symmetricRight) {
    return new ImproperIntegralResult(
            ImproperIntegralResult.Status.DIVERGENT,
            Double.NaN,
            Double.NaN,
            0,
            "Не удалось определить симметричный участок."
    );
}

    boolean symmetricInterval =
            Math.abs(
                    (point - symmetricLeft)
                            -
                    (symmetricRight - point)
            ) < 1e-12;

    boolean oddSymmetry =
            symmetryChecker.isOddAroundPoint(
                    function,
                    point
            );

    if (!symmetricInterval || !oddSymmetry) {

        return new ImproperIntegralResult(
                ImproperIntegralResult.Status.DIVERGENT,
                Double.NaN,
                Double.NaN,
                0,
                "Интеграл не существует."
        );
    }

    double result = 0.0;

    double error = 0.0;

    int maxN = 0;

    /*
     * Левая часть за пределами
     * симметричного участка.
     */
    if (a < symmetricLeft) {

        IntegrationResult leftResult =
                integrator.integrate(
                        method,
                        function,
                        a,
                        symmetricLeft,
                        epsilon
                );

        result += leftResult.getValue();

        error += leftResult.getError();

        maxN = Math.max(
                maxN,
                leftResult.getN()
        );
    }

    /*
     * Правая часть за пределами
     * симметричного участка.
     */
    if (symmetricRight < b) {

        IntegrationResult rightResult =
                integrator.integrate(
                        method,
                        function,
                        symmetricRight,
                        b,
                        epsilon
                );

        result += rightResult.getValue();

        error += rightResult.getError();

        maxN = Math.max(
                maxN,
                rightResult.getN()
        );
    }

    return new ImproperIntegralResult(
            ImproperIntegralResult.Status
                    .SYMMETRIC_CANCELLATION,
            result,
            error,
            maxN,
            "Симметричный участок относительно "
                    + "точки разрыва сокращён. "
                    + "Вычислены оставшиеся части интервала."
    );
}

private ImproperIntegralResult
handleIntegrableBoundarySingularity(
        MathFunction function,
        IntegrationMethod method,
        double a,
        double b,
        double epsilon,
        double point
) {

    /*
     * Разрыв в левой границе:
     *
     * x = point + t^2
     * dx = 2t dt
     */
    if (Math.abs(point - a) < 1e-12) {

        double length =
                b - point;

        if (length <= 0.0) {

            return new ImproperIntegralResult(
                    ImproperIntegralResult.Status.DIVERGENT,
                    Double.NaN,
                    Double.NaN,
                    0,
                    "Интеграл не существует."
            );
        }

        MathFunction transformed =
                new MathFunction() {

                    @Override
public double evaluate(double t) {
    if (t == 0.0) {
        t = 1e-6;
    }

    double x =
            point + t * t;

    return function.evaluate(x)
            * 2.0 * t;
}

                    @Override
                    public String getName() {
                        return "Преобразованная "
                                + "подынтегральная функция";
                    }

                    @Override
public String getFormula() {
    return "проеобразовання подынтегральная функция";
}
                };

        IntegrationResult result =
                integrator.integrate(
                        method,
                        transformed,
                        0.0,
                        Math.sqrt(length),
                        epsilon
                );

        return new ImproperIntegralResult(
                ImproperIntegralResult.Status.CONVERGENT,
                result.getValue(),
                result.getError(),
                result.getN(),
                "Несобственный интеграл "
                        + "с интегрируемой особенностью "
                        + "вычислен с помощью "
                        + "замены переменной."
        );
    }

    /*
     * Разрыв в правой границе:
     *
     * x = point - t^2
     * dx = -2t dt
     */
    if (Math.abs(point - b) < 1e-12) {

        double length =
                point - a;

        if (length <= 0.0) {

            return new ImproperIntegralResult(
                    ImproperIntegralResult.Status.DIVERGENT,
                    Double.NaN,
                    Double.NaN,
                    0,
                    "Интеграл не существует."
            );
        }

        MathFunction transformed =
                new MathFunction() {

                   @Override
public double evaluate(double t) {
    double x =
            point - t * t;

    return function.evaluate(x)
            * 2.0 * t;
}

                    @Override
                    public String getName() {
                        return "Преобразованная "
                                + "подынтегральная функция";
                    }

                    @Override
public String getFormula() {
    return "преобразованная подынтегральная функция";
}
                };

        IntegrationResult result =
                integrator.integrate(
                        method,
                        transformed,
                        0.0,
                        Math.sqrt(length),
                        epsilon
                );

        return new ImproperIntegralResult(
                ImproperIntegralResult.Status.CONVERGENT,
                result.getValue(),
                result.getError(),
                result.getN(),
                "Несобственный интеграл "
                        + "с интегрируемой особенностью "
                        + "вычислен с помощью "
                        + "замены переменной."
        );
    }

    return new ImproperIntegralResult(
            ImproperIntegralResult.Status.DIVERGENT,
            Double.NaN,
            Double.NaN,
            0,
            "Интеграл не существует."
    );
}

private ImproperIntegralResult
handleBoundaryDiscontinuity(
        MathFunction function,
        IntegrationMethod method,
        double a,
        double b,
        double epsilon,
        DiscontinuityAnalysis analysis
) {
        if (function.hasIntegrableSingularity()) {

        return handleIntegrableBoundarySingularity(
                function,
                method,
                a,
                b,
                epsilon,
                analysis.getPoint()
        );
    }

    ImproperConvergenceResult convergenceResult;

    if (analysis.getLocation()
            == DiscontinuityInfo.Type.AT_LEFT) {

        convergenceResult =
                convergence.fromLeft(
                        function,
                        method,
                        a,
                        b,
                        epsilon
                );

    } else {

        convergenceResult =
                convergence.fromRight(
                        function,
                        method,
                        a,
                        b,
                        epsilon
                );
    }

    if (!convergenceResult.isConvergent()) {

        return new ImproperIntegralResult(
                ImproperIntegralResult.Status.DIVERGENT,
                Double.NaN,
                Double.NaN,
                0,
                "Интеграл не существует."
        );
    }

    return new ImproperIntegralResult(
            ImproperIntegralResult.Status.CONVERGENT,
            convergenceResult.getValue(),
            convergenceResult.getError(),
            convergenceResult.getN(),
            convergenceResult.getMessage()
    );
}
}