package solutions;

import tools.*;

class BasicRepairProblem extends Sim {
    // All times are assumed to be exponentially distributed
    // The time between failures is the time between a machine being
    // repaired and it next failing

    // mtbf - mean time between failures
    // mrt - mean repair time
    int term;
    double mtbf, mrt;
    int m = 0, n = 0, failures = 0;

    double lastChange = 0;
    double acc = 0.0;

    public BasicRepairProblem(int nMachines, double mtbf, double mrt, int term) {
        m = nMachines;
        this.mtbf = mtbf;
        this.mrt = mrt;
        this.term = term;
        for (int i = 0; i < m; i++) {
            schedule(new Failure(Samplers.exp(1.0 / mtbf)));
        }
        simulate();
        System.out.println("Av. working machines = " + acc / now());
    }

    class Failure extends Event {

        public Failure(double t) {
            super(t);
        }

        public void invoke() {
            failures++;
            acc += m * (now() - lastChange);
            lastChange = now();
            m--;
            n++;
            if (n == 1) {
                schedule(new Repair(now() + Samplers.exp(1.0 / mrt)));
            }
        }
    }

    class Repair extends Event {
        public Repair(double t) {
            super(t);
        }

        public void invoke() {
            acc += m * (now() - lastChange);
            lastChange = now();
            n--;
            m++;
            schedule(new Failure(now() + Samplers.exp(1.0 / mtbf)));
            if (n > 0) {
                schedule(new Repair(now() + Samplers.exp(1.0 / mrt)));
            }
        }
    }

    public boolean stop() {
        return failures >= term;
    }

    public static void main(String[] args) {
        int nMachines = Integer.parseInt(args[0]);
        double ft = Double.parseDouble(args[1]);
        double rt = Double.parseDouble(args[2]);
        int term = Integer.parseInt(args[3]);
        new BasicRepairProblem(nMachines, ft, rt, term);
    }
}


