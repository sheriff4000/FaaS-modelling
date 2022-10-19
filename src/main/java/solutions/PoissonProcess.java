package solutions;

import tools.*;

import java.util.Arrays;

public class PoissonProcess {

    // Simulates a Poisson process using an exponential distribution sampler
    // for the inter-arrival times.
    // Plots the inter-arrival time and counting distributions, as a sanity
    // check.
    // There are some magic numbers for configuring the histograms, e.g.
    // 5 is approximately -ln(0.01)...!
    static void ppExp(double lambda, double endT) {
        double window = 10 * lambda;
        double iatLimit = 5 / lambda;
        Hist hiat = new Hist(0, iatLimit, 15);
        int u = (int) (3 * lambda * window);
        Hist hcount = new Hist(0, u, 15);
        int n = 0;
        double time = 0;
        double winStart = 0;
        // The first two lines solve the problem. The rest
        // are for sanity checking.
        while (time < endT) {
            double iat = Samplers.exp(lambda);
            time += iat;
            hiat.add(iat);
            if (time - winStart > window) {
                hcount.add(n);
                n = 0;
                winStart = time;
            } else {
                n++;
            }
        }
        hiat.display();
        hcount.display();
    }

    // Simulates n Poisson processes by splitting a single process whose
    // rate parameter is the sum of the given rates (lambdas).
    // The code plots the inter-arrival time distribution for stream 0
    // (rate lambda[0]) as a sanity check.
    static void ppSplit(double[] lambdas, double endT) {
        double iatLimit = 5 / lambdas[0];
        Hist hiat = new Hist(0, iatLimit, 15);
        int n = lambdas.length;
        double lambda = 0;
        for (int i = 0; i < n; i++) {
            lambda += lambdas[i];
        }
        double[] ps = new double[n];
        for (int i = 0; i < n; i++) {
            ps[i] = lambdas[i] / lambda;
        }
        // This implements the Bernoulli splitting process...
        Samplers.DiscreteEmpiricalSampler splitter = new Samplers.DiscreteEmpiricalSampler(ps);
        // arrTime0 corresponds to the arrival instants on stream 0
        double time = 0, arrTime0 = 0;
        while (time < endT) {
            double iat = Samplers.exp(lambda);
            time += iat;
            int stream = (int) splitter.next();
            if (stream == 0) {
                hiat.add(time - arrTime0);
                arrTime0 = time;
            }
        }
        hiat.display();
    }

    // Simulates a Poisson process exploiting conditional arrival times, as per
    // the in-class exercises.
    static void ppU(double lambda, double endT) {
        // Fix the window, so that there are 10 arrivals on average each time
        // we sample the Poisson distribution
        double window = 10 * lambda;
        double iatLimit = 5 / lambda;
        Hist hiat = new Hist(0, iatLimit, 15);
        double time = 0;
        double offset = 0;
        // The time between the last arrival in one window and the first
        // arrival in the next *non-empty* window is also an inter-arrival
        // time.
        while (time < endT) {
            int n = (int) Samplers.poisson(lambda * window);
            double[] ts = new double[n];
            for (int i = 0; i < n; i++) {
                ts[i] = offset + Samplers.uniform(0, window);
            }
            Arrays.sort(ts);
            for (int i = 0; i < n; i++) {
                double iat = ts[i] - time;
                hiat.add(iat);
                time += iat;
            }
            offset += window;
        }
        hiat.display();
    }

    public static void main(String[] args) {
        // lambda -- arrival rate
        // endT   -- termination time
        double lambda = Double.parseDouble(args[0]);
        double endT   = Double.parseDouble(args[1]);
        // ppExp builds the process using exponentially distributed inter-arrival
        // times. There can either be a single rate or a vector of rates. The
        // latter works by splitting a single process whose rate is the sum of
        // those given.
        //
        // ppSplit generates n streams by splitting a single stream whose rate is
        // the sum of the individual streams.
        //
        // ppU uses the conditional arrival times, where the inter-arrival times are
        // uniformly distributed within a given window.
        //
        //ppExp(lambda, endT);
        //ppSplit(new double[]{lambda, 2 * lambda, 3 * lambda}, endT);
        ppU(lambda, endT);
    }
}
