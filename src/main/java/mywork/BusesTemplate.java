package mywork;

import tools.*;

public class BusesTemplate {
  private static double simBuses(double t, int n) {
    double iat;
    // Write your simulation loop here & pick your favourite 
    // bus inter-arrival time distribution
    iat = Samplers.exp(0.1);
          //Samplers.uniform(0,20);
          //Samplers.pareto(3,10.0 / 7.0);
    return 0;
  }

  public static void main(String[] args) {
    double w = simBuses(Double.parseDouble(args[0]), 
                        Integer.parseInt(args[1]));
    System.out.println("E(W) = " + w);
  }
}
