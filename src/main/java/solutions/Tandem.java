package solutions;

import tools.*;

// Simulates a tandem queue (two M/M/1 queues joined together)
// Measurement code keeps track of the busy times of the two servers
// which are used to compute utilisations.
public class Tandem extends Sim {
    double lambda, tEnd;
    double[] mu = new double[2];
    int[] n = new int[2];

    double[] busyTime = {0, 0}, wentBusy = {0, 0};

    public Tandem(double lambda, double mu1, double mu2, double tEnd) {
        this.lambda = lambda;
        mu[0] = mu1;
        mu[1] = mu2;
        this.tEnd = tEnd;
    }

    class Arrival extends Event {
        public Arrival(double t) {
            super(t);
        }

        public void invoke() {
            schedule(new Arrival(now() + Samplers.exp(lambda)));
            n[0]++;
            if (n[0] == 1) {
                wentBusy[0] = now();
                schedule(new Completion(0, now() + Samplers.exp(mu[0])));
            }
        }
    }

    // For illustration, this parameterises the completion event by the
    // index of the server (0 or 1).
    // It enables some factorisation, but it doesn't buy much when compared
    // to a model with separate completion events for each server.
    class Completion extends Event {
        int index;

        public Completion(int i, double t) {
            super(t);
            index = i;
        }

        public void invoke() {
            n[index]--;
            if (index == 0) {
                n[1]++;
                if (n[1] == 1) {
                    wentBusy[1] = now();
                    schedule(new Completion(1, now() + Samplers.exp(mu[1])));
                }
            }
            if (n[index] > 0) {
                schedule(new Completion(index, now() + Samplers.exp(mu[index])));
            } else {
                busyTime[index] += now() - wentBusy[index];
            }
        }
    }

    public boolean stop() {
        return now() > tEnd;
    }

    void runSim() {
        schedule(new Arrival(now() + Samplers.exp(lambda)));
        simulate();
        System.out.println("U1 = " + busyTime[0] / now());
        System.out.println("U2 = " + busyTime[1] / now());
    }

    public static void main(String args[]) {
        double lambda = Double.parseDouble(args[0]);
        double mu1 = Double.parseDouble(args[1]);
        double mu2 = Double.parseDouble(args[2]);
        double tEnd = Double.parseDouble(args[3]);
        new Tandem(lambda, mu1, mu2, tEnd).runSim();
    }
}

