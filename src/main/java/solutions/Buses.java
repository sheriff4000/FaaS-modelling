package solutions;

import tools.*;

class Buses {
  private static double simBuses(double t, int n) {
    double sum = 0;
    double at;
    for (int i = 0; i < n; i++) {
      at = 0;
      // Pick your favourite inter-arrival time distribution
      while (at < t) {
        at = //Samplers.exp(0.1);
             //Samplers.uniform(0,20);
             Samplers.pareto(3,10.0 / 7.0);
      }
      sum += at - t;
    }
    return sum / n;
  }

  public static void main(String[] args) {
    double w = simBuses(Double.parseDouble(args[0]), Integer.parseInt(args[1]));
    System.out.println("E(W) = " + w);
  }
}
