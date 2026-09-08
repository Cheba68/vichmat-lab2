package integration.function;

import java.util.ArrayList;
import java.util.List;

public final class Functions {

    private Functions() {
    }

    public static List<MathFunction> getAvailableFunctions() {
        List<MathFunction> functions = new ArrayList<>();

        functions.add(new MathFunction() {

            @Override
            public double evaluate(double x) {
                return -2 * x * x * x
                        - 5 * x * x
                        + 7 * x
                        - 13;
            }

            @Override
            public String getName() {
                return "f(x) = -2x³ - 5x² + 7x - 13";
            }

            @Override
            public String getFormula() {
                return "-2x³ - 5x² + 7x - 13";
            }
        });

        functions.add(new MathFunction() {

            @Override
            public double evaluate(double x) {
                return x * x + 1;
            }

            @Override
            public String getName() {
                return "f(x) = x² + 1";
            }

            @Override
            public String getFormula() {
                return "x² + 1";
            }
        });

        functions.add(new MathFunction() {

            @Override
            public double evaluate(double x) {
                return Math.sin(x);
            }

            @Override
            public String getName() {
                return "f(x) = sin(x)";
            }

            @Override
            public String getFormula() {
                return "sin(x)";
            }
        });

        functions.add(new MathFunction() {

            @Override
            public double evaluate(double x) {
                return Math.exp(x-1);
            }

            @Override
            public String getName() {
                return "f(x) = e^(x-1)";
            }

            @Override
            public String getFormula() {
                return "e^(x-1)";
            }
        });

        functions.add(new MathFunction() {

            @Override
            public double evaluate(double x) {
                return 1.0 / x;
            }

            @Override
            public String getName() {
                return "f(x) = 1/x";
            }

            @Override
            public String getFormula() {
                return "1/x";
            }

            @Override
            public double[] getDiscontinuityPoints() {
                return new double[]{0.0};
            }
        });

        functions.add(new MathFunction() {
    @Override
    public double evaluate(double x) {
        return 1.0 / Math.sqrt(Math.abs(x));
    }

    @Override
    public String getName() {
        return "f(x) = 1/√|x|";
    }

    @Override
    public String getFormula() {
        return "1/√|x|";
    }

    @Override
    public double[] getDiscontinuityPoints() {
        return new double[]{0.0};
    }

    @Override
    public boolean hasIntegrableSingularity() {
        return true;
    }
});

functions.add(new MathFunction() {
    @Override
    public double evaluate(double x) {
        return 1 + 1.0 / Math.sqrt(Math.abs(x - 1));
    }

    @Override
    public String getName() {
        return "f(x) =1 + 1/√|x - 1|";
    }

    @Override
    public String getFormula() {
        return "1 + 1/√|x - 1|";
    }

    @Override
    public double[] getDiscontinuityPoints() {
        return new double[]{1.0};
    }
});

functions.add(new MathFunction() {
    @Override
    public double evaluate(double x) {
        return Math.log(x+5);
    }

    @Override
    public String getName() {
        return "f(x) = ln(x+5)";
    }

    @Override
    public String getFormula() {
        return "ln(x+5)";
    }

    @Override
    public double[] getDiscontinuityPoints() {
        return new double[]{0.0};
    }

    @Override
    public boolean hasIntegrableSingularity() {
        return true;
    }
});
functions.add(new MathFunction() {
    @Override
    public double evaluate(double x) {
        return Math.exp(-x*x);
    }

    @Override
    public String getName() {
        return "f(x) = e^-x2";
    }

    @Override
    public String getFormula() {
        return "e^-x2";
    }
});

        return functions;
    }
}