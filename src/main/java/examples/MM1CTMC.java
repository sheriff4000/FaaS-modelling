package examples;

import tools.*;

// Solves the CTMC for the M/M/1/C queue (finite capacity C) and uses the
// steady-state probabilities to compute the mean queue length
class MM1CTMC {
    double[][] Q;
    int[] xs;
    Samplers.DiscreteEmpiricalSampler[] samplers;
    int simSteps;
    double lambda, mu;
    double time = 0.0;
    double holdTime;
    double[] ps;

    int nextState(int s) {
        return (int) samplers[s].next();
    }

    // This is the CTMC simulation code...
    void simulate() {
        int state = 0;
        for (int n = 0; n < simSteps; n++) {
            holdTime = Samplers.exp(-Q[state][state]);
            ps[state] += holdTime;
            time += holdTime;
            state = nextState(state);
        }
    }

    void printRow(int i) {
        for (int j = 0; j < Q[i].length; j++) {
            System.out.print((int) Q[i][j] + " ");
        }
        System.out.println();
    }

    // Sets up the Q matrix for the M/M/1/C queue
    public MM1CTMC(double lambda, double mu, int c, int simSteps) {
        this.lambda = lambda;
        this.mu = mu;
        this.simSteps = simSteps;
        xs = new int[c + 1];
        ps = new double[c + 1];
        samplers = new Samplers.DiscreteEmpiricalSampler[c + 1];
        for (int i = 0; i <= c; i++) {
            xs[i] = i;
            ps[i] = 0.0;
        }
        Q = new double[c + 1][c + 1];
        Q[0][1] = lambda;
        Q[c][c - 1] = mu;
        samplers[0] = new Samplers.DiscreteEmpiricalSampler(xs, Q[0]);
        samplers[c] = new Samplers.DiscreteEmpiricalSampler(xs, Q[c]);
        Q[0][0] = -lambda;
        Q[c][c] = -mu;
        printRow(0);
        for (int i = 1; i < c; i++) {
            Q[i][i - 1] = mu;
            Q[i][i + 1] = lambda;
            samplers[i] = new Samplers.DiscreteEmpiricalSampler(xs, Q[i]);
            Q[i][i] = -(lambda + mu);
            printRow(i);
        }
        printRow(c);
    }

    void runSim() {
        simulate();
        double acc = 0.0;
        for (int i = 0; i < ps.length; i++) {
            acc += i * ps[i] / time;
        }
        System.out.println("Mean queue length = " + acc);
    }

    //
    // Solves the CTMC for a finite-capacity M/M/1 queue
    //
    public static void main(String args[]) {
        MM1CTMC sim = new MM1CTMC(Double.parseDouble(args[0]),
                            Double.parseDouble(args[1]),
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]));
        sim.runSim();
    }
}





