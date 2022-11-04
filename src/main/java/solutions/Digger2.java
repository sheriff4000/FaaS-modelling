package solutions;

import tools.*;
import network.Debug;

// Simulates the digger system (unloading stops if the belt jams)
class Digger2 extends Sim {
    double stopped, w = 0.0, residualTime;

    boolean jammed = false, unloading = false;

    Event refillEvent;

    DistributionSampler unloadTime = new Samplers.Exp(1.0 / 5);
    DistributionSampler timeToNextJam = new Samplers.Exp(1.0 / 100);
    DistributionSampler repairTime = new Samplers.Exp(1.0 / 30);
    DistributionSampler refillTime = new Samplers.Exp(1.0 / 20);

    // Start in state where the belt is working and the digger
    // is about to refill its bucket...
    public Digger2() {
        schedule(new Jam(now() + timeToNextJam.next()));
        schedule(new Unload(now() + refillTime.next()));
        Debug.setDebugOn();
    }

    class Unload extends Event {
        public Unload(double t) {
            super(t);
        }

        public void invoke() {
            unloading = true;
            residualTime = unloadTime.next();
            if (jammed) {
                stopped = now();
            } else {
                refillEvent = new Refill(now() + residualTime);
                schedule(refillEvent);
            }
        }
    }

    class Refill extends Event {
        public Refill(double t) {
            super(t);
        }

        public void invoke() {
            unloading = false;
            schedule(new Unload(now() + refillTime.next()));
        }
    }

    class Jam extends Event {
        public Jam(double t) {
            super(t);
        }

        public void invoke() {
            jammed = true;
            schedule(new Repaired(now() + repairTime.next()));
            if (unloading) {
                stopped = now();
                deschedule(refillEvent);
                residualTime = refillEvent.invokeTime() - now();
            }
        }
    }

    class Repaired extends Event {
        public Repaired(double t) {
            super(t);
        }

        public void invoke() {
            jammed = false;
            schedule(new Jam(now() + timeToNextJam.next()));
            if (unloading) {
                w += now() - stopped;
                refillEvent = new Refill(now() + residualTime);
                schedule(refillEvent);
            }
        }
    }

    public boolean stop() {
        return now() > 100000;
    }

    void runSim() {
        simulate();
        Logger.logResult("Prop. time spent waiting", w / now());
    }

    public static void main(String args[]) {
        new Digger2().runSim();
        new Digger2().runSim();
        new Digger2().runSim();
        Logger.displayResults(0.01);
    }
}


