package solutions;

import tools.Samplers;

public class TryMe {

    // Repeatedly advance time by a random distribution sample; stop at 100...
    // Uncomment your favourite sampler & define the respective parameters
    public static void main(String[] args) {
        final double a = 0, b = 10;
        double t = 0.0;
        while (t < 100) {
            System.out.println(t);
            t += Samplers.uniform(a, b);            // mean (b-a)/2
            // t += Samplers.exp(r);                // mean 1/r
            // t += Samplers.weibull(lam, k);       // mean lam*(1/k)! when 1/k is an integer
                                                    // gamma(1/k) otherwise
            // t += Samplers.erlang(k, theta);      // mean k/theta (k integer)
            // t += Samplers.normal(mu, sigma);     // mean mu
            // t += Samplers.pareto(xm, a);         // mean (a*xm)/(a-1), xm>0, a>1
        }
    }
}
