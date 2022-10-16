package examples;

import tools.*;

//
// Simulates an M/M/1 queue with an infinite-capacity queue
//
class MM1Sim extends Sim {

    double lambda, mu, runTime;

    // State variable (population)
    int n = 0;

    // This is useful for testing memorylessness
    Event arrivalEvent;

    // Measurement variables for estimating mean population
    double integral = 0.0, lastTransitionTime = 0.0;

    public MM1Sim(double lambda, double mu, double runTime) {
        this.lambda = lambda;
        this.mu = mu;
        this.runTime = runTime;
    }

    class Arrival extends Event {
        public Arrival(double t) {
            super(t);
        }

        public void invoke() {
            integral += (now() - lastTransitionTime) * n;
            lastTransitionTime = now();
            arrivalEvent = new Arrival(now() + Samplers.exp(lambda));
            schedule(arrivalEvent);
            n++;
            if (n == 1) {
                schedule(new Completion(now() + Samplers.exp(mu)));
            }
        }
    }

    class Completion extends Event {
        public Completion(double t) {
            super(t);
        }

        public void invoke() {
            integral += (now() - lastTransitionTime) * n;
            lastTransitionTime = now();
            n--;
            if (n > 0) {
                schedule(new Completion(now() + Samplers.exp(mu)));
            }
            deschedule(arrivalEvent);
            arrivalEvent = new Arrival(now() + Samplers.exp(lambda));
            schedule(arrivalEvent);
        }
    }

    public boolean stop() {
        return now() > runTime;
    }

    void runSim() {
        arrivalEvent = new Arrival(now() + Samplers.exp(lambda));
        schedule(arrivalEvent);
        simulate();
        System.out.println("Mean population = " + integral / now());
    }

    public static void main(String[] args) {
        double lambda = Double.parseDouble(args[0]);
        double mu = Double.parseDouble(args[1]);
        double runTime = Double.parseDouble(args[2]);
        new MM1Sim(lambda, mu, runTime).runSim();
    }
}



