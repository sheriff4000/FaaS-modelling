package solutions;

import examples.CTMCSolver;

// Solve the CTMC for the simplified Monopoly problem
public class MonopolyCTMC {
    public static void main(String[] args) {
        double[][] q = new double[][] {
                {-1,      1.0/6.0,  1.0/3.0, 1.0/6.0, 1.0/6.0, 1.0/6.0, 0},
                {0,            -1,  1.0/3.0, 1.0/6.0, 1.0/6.0, 1.0/6.0, 1.0/6.0},
                {1.0/6.0,       0, -5.0/6.0, 1.0/6.0, 1.0/6.0, 1.0/6.0, 1.0/6.0},
                {1.0/6.0, 1.0/6.0,  1.0/6.0,      -1, 1.0/6.0, 1.0/6.0, 1.0/6.0},
                {1.0/6.0, 1.0/6.0,  1.0/3.0,       0,      -1, 1.0/6.0, 1.0/6.0},
                {1.0/6.0, 1.0/6.0,  1.0/3.0, 1.0/6.0,       0,      -1, 1.0/6.0},
                {1.0/6.0, 1.0/6.0,  1.0/6.0, 1.0/6.0, 1.0/6.0, 1.0/6.0, -1}
        } ;
        double[] sol = CTMCSolver.solve(q);
        CTMCSolver.printSolution(sol);
    }
}
