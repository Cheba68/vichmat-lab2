package integration.improper;

public class DiscontinuityInfo {

    public enum Type {
        NONE,
        AT_LEFT,
        AT_RIGHT,
        INSIDE
    }

    private final Type type;
    private final double point;

    public DiscontinuityInfo(
            Type type,
            double point
    ) {
        this.type = type;
        this.point = point;
    }

    public Type getType() {
        return type;
    }

    public double getPoint() {
        return point;
    }

    public boolean exists() {
        return type != Type.NONE;
    }
}