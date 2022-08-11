package examples;

import network.*;
import tools.*;

public class CentralServer extends Sim {
    double lambda;

// 
// Calculates the mean pop on arrival and mean residual s.t.
// For U(0, 0.01) this is 10^-4/(3*2*0.005) = 0.003333, i.e.
// E(S^2)/(2E(S))

    // Simulates the central server system (CPU and two disks) using
    // the (fixed) parameterisation given in the notes.
    public CentralServer(double lambda) {
        this.lambda = lambda;
    }

    void runSim() {
        Network.initialise();
        Delay cpuTime = new Delay(new Samplers.Exp(1 / 0.005));
        Delay disk1Time = new Delay(new Samplers.Exp(1 / 0.03));
        Delay disk2Time = new Delay(new Samplers.Exp(1 / 0.027));

        Source source = new Source("Source", new Samplers.Exp(lambda));

        QueueingNode cpu = new QueueingNode("CPU", cpuTime, 1);
        QueueingNode disk1 = new QueueingNode("Disk 1", disk1Time, 1);
        QueueingNode disk2 = new QueueingNode("Disk 2", disk2Time, 1);

        Sink sink = new Sink("Sink");

        double[] routingProbs = {1.0 / 121.0, 70.0 / 121.0, 50.0 / 121.0};
        ProbabilisticBranch cpuOutputLink
                = new ProbabilisticBranch(routingProbs, new Node[]{sink, disk1, disk2});

        source.setLink(new Link(cpu));
        cpu.setLink(cpuOutputLink);
        disk1.setLink(new Link(cpu));
        disk2.setLink(new Link(cpu));

        simulate();

        Network.logResults();
    }

    public boolean stop() {
        return now() > 15000;
    }

    public static void main(String args[]) {
        new CentralServer(Double.parseDouble(args[0])).runSim();
        Network.displayResults();
    }

}



