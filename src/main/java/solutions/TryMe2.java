package solutions;

import tools.*;

// Uses event scheduling to advance time repeatedly by a uniformly-distributed random sample
// Stops at time 100
// Note the use of inner classes to give events scoped access to any parameters or
// state/measurement variables
public class TryMe2 extends Sim {
    double a = 0, b = 10;

    // Events must implement invoke(); once scheduled the event will be called at virtual
    // time t
    public class Ping extends Event {
        public Ping(double t) {
            super(t);
        }

        public void invoke() {
            System.out.println(now());
            schedule(new Ping(now() + Samplers.uniform(a, b)));
        }
    }

    // Every Sim must implement stop()
    public boolean stop() {
        return now() > 100;
    }

    public void runSim() {
        schedule(new Ping(now()));
        simulate();
    }

    public static void main(String[] args) {
        new TryMe2().runSim();
    }
}
