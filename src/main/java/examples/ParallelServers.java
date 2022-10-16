package examples; 

import network.*;
import tools.*;

// Simulates a closed system with two parallel servers using the queueing network library
// The finite source injects a specified number of jobs into the target node during
// initialisation
public class ParallelServers extends Sim {
  double tEnd;

  // This uses the queueing network library (network)
  public ParallelServers(int n, double mu1, double mu2, double tEnd) {
    this.tEnd = tEnd;
    Network.initialise();
    Delay s1 = new Delay(new Samplers.Exp(mu1));
    Delay s2 = new Delay(new Samplers.Exp(mu2));

    // Define the nodes
    FiniteSource source = new FiniteSource("Source", n);
    QueueingNode qn1 = new QueueingNode("QN1", s1, 1);
    QueueingNode qn2 = new QueueingNode("QN2", s2, 1);
    ThroughputSensor s = new ThroughputSensor("Sensor");

    // The 50:50 branch...
    ProbabilisticBranch branch
            = new ProbabilisticBranch(new double[]{0.5, 0.5},
            new Node[]{qn1, qn2});

    // Join the nodes together
    source.setLink(new Link(s));
    qn1.setLink(new Link(s));
    qn2.setLink(new Link(s));
    s.setLink(branch);
  }

  public boolean stop() {
    return Sim.now() > tEnd;
  }

  void runSim() {
    simulate();
    Network.logResults();
  }

//
// args[0] = population, N
// args[1] = server 1 rate, \mu_1
// args[2] = server 2 rate, \mu_2
//
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    double mu1 = Double.parseDouble(args[1]);
    double mu2 = Double.parseDouble(args[2]);
    double tEnd = Double.parseDouble(args[3]);
    new ParallelServers(n, mu1, mu2, tEnd).runSim();
    Network.displayResults();
  }

}




