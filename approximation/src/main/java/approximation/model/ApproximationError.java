package approximation.model;

public class ApproximationError {

    private final String methodName;
    private final String message;

    public ApproximationError(
            String methodName,
            String message
    ) {
        this.methodName = methodName;
        this.message = message;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMessage() {
        return message;
    }
}