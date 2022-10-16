package examples;

import tools.*;

//
// Simulates an M/M/1 queue (exponentially distributed inter-arrival and service times)
// with a finite-capacity queue
//
class MM1CSim extends Sim {
    int capacity;

    double lambda, mu, runTime;

    // State variable (population)
    int n = 0;

    // Measurement variables
    double b = 0.0, tb = 0.0;

    public MM1CSim(double lambda, double mu, int capacity, double runTime) {
        this.lambda = lambda;
        this.mu = mu;
        this.capacity = capacity;
        this.runTime = runTime;
    }

    class Arrival extends Event {
        public Arrival(double t) {
            super(t);
        }

        public void invoke() {
            n++;
            if (n < capacity) {
                schedule(new Arrival(now() + Samplers.exp(lambda)));
            }
            if (n == 1) {
                schedule(new Completion(now() + Samplers.exp(mu)));
                tb = now();
            }
        }
    }

    class Completion extends Event {
        public Completion(double t) {
            super(t);
        }

        public void invoke() {
            n--;
            if (n == capacity - 1) {
                schedule(new Arrival(now() + Samplers.exp(lambda)));
            }
            if (n > 0) {
                schedule(new Completion(now() + Samplers.exp(mu)));
            } else {
                b += now() - tb;
            }
        }
    }

    public boolean stop() {
        return now() > runTime;
    }

    void runSim() {
        schedule(new Arrival(now() + Samplers.exp(lambda)));
        simulate();
        System.out.println("Utilisation = " + b / runTime);
    }

    public static void main(String[] args) {
        double lambda = Double.parseDouble(args[0]);
        double mu = Double.parseDouble(args[1]);
        int capacity = Integer.parseInt(args[2]);
        double runTime = Double.parseDouble(args[3]);
        new MM1CSim(lambda, mu, capacity, runTime).runSim();
    }
}


