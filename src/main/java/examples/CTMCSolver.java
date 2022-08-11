package examples;

import Jama.*;

public class CTMCSolver {
    public static double[] solve(double[][] q) {
        int n = q.length;
        double[][] qc = new double[n][n];
        double[] onec = new double[n];
        for (int i = 0; i < n; i++) {
            onec[i] = 0;
            for (int j = 0; j < n; j++) {
                if (j == n - 1) {
                    qc[j][i] = 1;
                } else {
                    qc[j][i] = q[i][j];
                }
            }
        }
        onec[n - 1] = 1;
        Matrix lhs = new Matrix(qc);
        Matrix rhs = new Matrix(onec, n);
        double[][] sol = lhs.solve(rhs).getArray();
        double[] res = new double[n];
        for (int i = 0; i < n; i++) {
            res[i] = sol[i][0];
        }
        return res;
    }

    public static void printSolution(double[] v) {
        int n = v.length;
        System.out.print("(");
        for (int i = 0; i < n - 1; i++) {
            System.out.print(v[i] + ", ");
        }
        System.out.println(v[n - 1] + ")");
    }
}
