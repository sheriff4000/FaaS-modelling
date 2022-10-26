package mywork;

// The tools package contains the required simulation management code,
// distribution samplers etc. (It's lib/sim-tools.jar.)
import tools.*;

class SimTemplate extends Sim {
    // State variables, measurement variables etc. go here, so they are in scope
    // throughout the class
    int something;

    // The constructor will typically have configuration/model parameters
    public SimTemplate(int param) {
        // Assign the state variables & initialise here
        this.something = param;
        // Schedule the initial event(s), e.g. to invoke E at time 10.0:
        schedule(new E(10.0));
        // To run the simulation do:
        simulate();
    }

    // General template for an event
    // Note: the invoke code can 'see' the simulation state variables
    class E extends Event {
        // Boilerplate to set the event invocation time...
        // (You can add other event parameters if necessary)
        public E(double t) {
            super(t);
        }

        public void invoke() {
            // Insert event code here, which typically updates the state and
            // measurement variables and schedules zero or more future events, e.g.
            something++;
            schedule(new E(now() + 10.0));
        }
    }

    // The termination condition is defined by stop(). Replace true with any test,
    // e.g. based on the time, given by now(), number of occurrences of a
    // particular event, etc.
    public boolean stop() {
        return true;
    }

    // The main method typically reads parameters, builds and then runs the simulation.
    // An alternative approach is to separate building and running by adding
    // a runSim() method (above) to schedule the initial events & run the simulation.
    // You then need to replace the code below with SimTemplate(param).runSim() -- see
    // the PostOffice simulation in the solutions for an example.
    public static void main(String[] args) {
        int param = Integer.parseInt(args[0]);
        new SimTemplate(param);
    }
}


