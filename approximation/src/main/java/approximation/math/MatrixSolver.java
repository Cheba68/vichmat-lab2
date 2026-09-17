package approximation.math;

public class MatrixSolver {

    public static double[] solve(
            double[][] matrix,
            double[] rightSide
    ) {

        int n = rightSide.length;

        double[][] a = new double[n][n + 1];

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {
                a[i][j] = matrix[i][j];
            }

            a[i][n] = rightSide[i];
        }

        for (int i = 0; i < n; i++) {

            int maxRow = i;

            for (int k = i + 1; k < n; k++) {

                if (Math.abs(a[k][i])
                        > Math.abs(a[maxRow][i])) {

                    maxRow = k;
                }
            }

            if (Math.abs(a[maxRow][i]) < 1e-12) {
                throw new IllegalArgumentException(
                        "Система не имеет единственного решения."
                );
            }

            double[] temp = a[i];
            a[i] = a[maxRow];
            a[maxRow] = temp;

            for (int k = i + 1; k < n; k++) {

                double factor =
                        a[k][i] / a[i][i];

                for (int j = i; j <= n; j++) {

                    a[k][j] -=
                            factor * a[i][j];
                }
            }
        }

        double[] solution =
                new double[n];

        for (int i = n - 1; i >= 0; i--) {

            double sum = a[i][n];

            for (int j = i + 1; j < n; j++) {

                sum -=
                        a[i][j] * solution[j];
            }

            solution[i] =
                    sum / a[i][i];
        }

        return solution;
    }
}