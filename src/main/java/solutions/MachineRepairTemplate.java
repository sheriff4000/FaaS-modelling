package solutions;

import tools.*;

class MachineRepairTemplate extends Sim {
    // m    - No. of machines
    // mtbf - Mean time between failures
    // mrt  - Mean repair time
    int m;
    double mtbf, mrt;
    int term;

    public MachineRepairTemplate(int nMachines, double mtbf, double mrt, int term) {
        m = nMachines;
        this.mtbf = mtbf;
        this.mrt = mrt;
        this.term = term;
        simulate();
    }

    // General template for an event
    // Note: the invoke code can 'see' the simulation state variables
    class E extends Event {
        // Boilerplate to set the event invocation time...
        public E(double t) {
            super(t);
        }
        public void invoke() {
            // Insert event code here
        }
    }

    public boolean stop() {
        return true; // failures >= term;
    }

    public static void main(String[] args) {
        int nMachines = Integer.parseInt(args[0]);
        double ft = Double.parseDouble(args[1]);
        double rt = Double.parseDouble(args[2]);
        int term = Integer.parseInt(args[3]);
        new MachineRepairTemplate(nMachines, ft, rt, term);
    }
}


