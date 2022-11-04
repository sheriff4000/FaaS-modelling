package solutions;

import examples.CTMCSolver;

// A template for using the CTMC solver provided
public class Digger2CTMC {
    public static void main(String[] args) {
        // The second digger problem
        double[][] q = new double[][] {
            { -0.21,    0.2,       0,    0.01},
            {  0.05,  -0.06,    0.01,       0},
            {     0, 0.0333, -0.0833,    0.05},
            {0.0333,      0,       0, -0.0333}
        } ;
        double[] sol = CTMCSolver.solve(q);
        CTMCSolver.printSolution(sol);
    }
}
