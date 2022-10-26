package solutions;

import tools.*;

// Simulation model of the factory repair problem set in the 2021 exam
// There is no measurement code, so it will run but won't produce any output.
// As an exercise you can add the same measurement code used in
// the BasicRepairProblem.
class FactoryRepairProblem extends Sim {
    // All times are assumed exponential
    // miat - mean inter-arrival time
    // mtbf - mean time between failures
    // mrt - mean repair time
    // mpt - mean component processing time
    int m, term;
    boolean engineerIdle = true;
    double miat, mtbf, mrt, mpt;
    int completions = 0;
    int components = 0;
    Event[] completion;
    boolean[] idle;
    boolean[] broken;

    public FactoryRepairProblem(int m, double miat, double mtbf, double mrt, double mpt, int term) {
        this.m = m;
        this.miat = miat;
        this.mtbf = mtbf;
        this.mrt = mrt;
        this.mpt = mpt;
        this.term = term;
        completion = new Event[m];
        idle = new boolean[m];
        broken = new boolean[m];
    }

    class Arrival extends Event {
        public Arrival(double t) {
            super(t);
        }

        public void invoke() {
            schedule(new Arrival(now() + Samplers.exp(1.0 / miat)));
            int i;
            for (i = 0; i < m; i++) {
                if (idle[i]) {
                    idle[i] = false;
                    completion[i] = new Completion(i, now() + Samplers.exp(1.0 / mpt));
                    schedule(completion[i]);
                    break;
                }
            }
            if (i == m) {
                components++;
            }
        }
    }

    class Completion extends Event {
        int machine;

        public Completion(int machine, double t) {
            super(t);
            this.machine = machine;
        }

        public void invoke() {
            completions++;
            if (components > 1) {
                components--;
                completion[machine] = new Completion(machine, now() + Samplers.exp(1.0 / mpt));
                schedule(completion[machine]);
            } else {
                idle[machine] = true;
            }
        }
    }

    class Failure extends Event {
        int machine;

        public Failure(int machine, double t) {
            super(t);
            this.machine = machine;
        }

        public void invoke() {
            broken[machine] = true;
            if (engineerIdle) {
                schedule(new Repaired(machine, now() + Samplers.exp(1.0 / mrt)));
                engineerIdle = false;
            }
            if (!(idle[machine])) {
                deschedule(completion[machine]);
                idle[machine] = true;
            }
        }
    }

    class Repaired extends Event {
        int machine;

        public Repaired(int machine, double t) {
            super(t);
            this.machine = machine;
        }

        public void invoke() {
            broken[machine] = false;
            schedule(new Failure(machine, now() + Samplers.exp(1.0 / mtbf)));
            if (components > 0) {
                components--;
                completion[machine] = new Completion(machine, now() + Samplers.exp(1.0 / mpt));
                schedule(completion[machine]);
            }
            int i;
            for (i = 0; i < m; i++) {
                if (broken[i]) {
                    schedule(new Repaired(i, now() + Samplers.exp(1.0 / mrt)));
                    break;
                }
            }
            if (i == m) {
                engineerIdle = true;
            }
        }
    }

    public boolean stop() {
        return completions >= term;
    }

    void runSim() {
        schedule(new Arrival(now() + Samplers.exp(1.0 / miat)));
        for (int i = 0; i < m; i++) {
            idle[i] = true;
            broken[i] = false;
            schedule(new Failure(i, now() + Samplers.exp(1.0 / mtbf)));
        }
        simulate();
    }

    public static void main(String args[]) {
        // Inline the parameters given...
        new FactoryRepairProblem(3, 1, 20, 10, 1, 10000).runSim();
    }
}


