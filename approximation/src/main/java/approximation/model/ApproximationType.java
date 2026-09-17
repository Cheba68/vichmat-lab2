package approximation.model;

public enum ApproximationType {

    LINEAR("Линейная"),
    QUADRATIC("Квадратичная"),
    CUBIC("Кубическая"),
    EXPONENTIAL("Экспоненциальная"),
    LOGARITHMIC("Логарифмическая"),
    POWER("Степенная");

    private final String displayName;

    ApproximationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}