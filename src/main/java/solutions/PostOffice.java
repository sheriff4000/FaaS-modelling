package solutions;

import tools.*;

// Simulates the high-street post office, as set in the 2019 exam
class PostOffice extends Sim {
    private double lambda, mu, simTime;
    private int n;
    private int m;
    private int nCustomers;
    private int thinkers = 0;

    int nWaiting = 0;
    boolean counter1Busy = false;
    boolean counter2Open = false;

    public PostOffice(double lambda, double mu, int n, int m, int nCustomers, double simTime) {
        this.lambda = lambda;
        this.mu = mu;
        this.n = n;
        this.m = m;
        this.nCustomers = nCustomers;
        this.simTime = simTime;
        thinkers = nCustomers;
    }

    class ArriveAtBank extends Event {
        public ArriveAtBank(double t) {
            super(t);
        }

        public void invoke() {
            if (nWaiting < n) {
                thinkers--;
                if (counter1Busy) {
                    if (nWaiting == m - 1 & !counter2Open) {
                        counter2Open = true;
                        schedule(new LeaveBank(2, now() + Samplers.exp(mu)));
                    } else {
                        nWaiting++;
                    }
                } else {
                    counter1Busy = true;
                    schedule(new LeaveBank(1, now() + Samplers.exp(mu)));
                }
            }
            if (thinkers > 0) {
                schedule(new ArriveAtBank(now() + Samplers.exp(lambda * thinkers)));
            }
        }
    }

    class LeaveBank extends Event {
        int counterIndex;

        public LeaveBank(int i, double t) {
            super(t);
            counterIndex = i;
        }

        public void invoke() {
            if (counterIndex == 1) {
                if (nWaiting > 0) {
                    nWaiting--;
                    schedule(new LeaveBank(1, now() + Samplers.exp(mu)));
                } else {
                    counter1Busy = false;
                }
            } else {
                if (nWaiting < m) {
                    counter2Open = false;
                } else {
                    nWaiting--;
                    schedule(new LeaveBank(2, now() + Samplers.exp(mu)));
                }
            }
            thinkers++;
            if (thinkers == 1) {
                schedule(new ArriveAtBank(now() + Samplers.exp(lambda * thinkers)));
            }
        }
    }

    public boolean stop() {
        return now() > simTime;
    }

    void runSim() {
        schedule(new ArriveAtBank(now() + Samplers.exp(lambda * thinkers)));
        simulate();
    }
    public static void main(String args[]) {
        double arrivalRate = Double.parseDouble(args[0]);
        double serviceRate = Double.parseDouble(args[1]);
        int n = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        int nCustomers = Integer.parseInt(args[4]);
        double simTime = Integer.parseInt(args[5]);
        new PostOffice(arrivalRate, serviceRate, n, m, nCustomers, simTime).runSim();
    }
}



