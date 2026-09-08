package integration.improper;

public class DiscontinuityAnalysis {

    public enum Behavior {
        FINITE,
        POSITIVE_INFINITY,
        NEGATIVE_INFINITY,
        UNDEFINED
    }

    private final double point;

    private final DiscontinuityInfo.Type location;

    private final Behavior leftBehavior;

    private final Behavior rightBehavior;

    public DiscontinuityAnalysis(
            double point,
            DiscontinuityInfo.Type location,
            Behavior leftBehavior,
            Behavior rightBehavior
    ) {
        this.point = point;
        this.location = location;
        this.leftBehavior = leftBehavior;
        this.rightBehavior = rightBehavior;
    }

    public double getPoint() {
        return point;
    }

    public DiscontinuityInfo.Type getLocation() {
        return location;
    }

    public Behavior getLeftBehavior() {
        return leftBehavior;
    }

    public Behavior getRightBehavior() {
        return rightBehavior;
    }
}