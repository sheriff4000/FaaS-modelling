package mywork;

import examples.CTMCSolver;

// A template for using the CTMC solver provided
public class CTMCSolveTemplate {
    public static void main(String[] args) {
        double[][] q = new double[][] {
            // Insert generator matrix here
        } ;
        double[] sol = CTMCSolver.solve(q);
        CTMCSolver.printSolution(sol);
    }
}
