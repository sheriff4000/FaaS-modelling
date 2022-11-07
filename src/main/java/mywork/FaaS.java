package mywork;

// The tools package contains the required simulation management code,
// distribution samplers etc. (It's lib/sim-tools.jar.)

import tools.DistributionSampler;
import tools.Event;
import tools.Samplers;
import tools.Sim;
import java.io.*;
import java.util.Scanner;

class FaaS extends Sim {
    // State variables, measurement variables etc. go here, so they are in scope
    // throughout the class
    int F = 10861;
    int M = 40;
    f[] functions  = new f[10862];

    public class f{
        int S_time;
        int id;
        int invocations;

        public f(int id, int s, int i){
            System.out.println("initialising");
            this.id = id;
            this.S_time = s;
            this.invocations = i;
        }


    }

    public class container_f{
        int id;
        float last_called;
        boolean busy;

        public container_f(int id, float last_called, boolean busy){
            this.id = id;
            this.last_called = last_called;
            this.busy = busy;
        }
    }

    void read_csv(String filename) throws Exception{
        //int f_size= F+1;
        f init = new f(0,0, 0);
        functions[0] = init;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine(); // skips header line
        //System.out.println(functions[0].S_time);

        while (line != null){
            line = br.readLine();
            String[] func = line.split(",");
            //System.out.println("function ID: " + func[0] + " service time: " + func[1] + " invocations: " + func[2]);
            int id = Integer.parseInt(func[0]);
            f tmp = new f(id, Integer.parseInt(func[1]), Integer.parseInt(func[2]));
            functions[id] = tmp;

            //functions[Integer.parseInt(func[0])].S_time = Integer.parseInt(func[1]);
            //functions[Integer.parseInt(func[0])].invocations = Integer.parseInt(func[2]);

            System.out.println("function ID: " + func[0] + " service time: " + func[1]);
            
        }


    }


    boolean[] busy = new boolean[40];
    float t;

    DistributionSampler service_time = new Samplers.Exp(1.0 / 5);
    DistributionSampler request_time = new Samplers.Exp(1.0 / 100);

    // The constructor will typically have configuration/model parameters
    public FaaS(int param) {
        // Setup array of all functions - containing service time and invocations;
        String file = "/Users/sherifagbabiaka/Documents/uni-3/Simulation and Modelling/part-i-code-CW/src/main/java/mywork/trace-final.csv";
        try {
            read_csv(file);
        } catch (Exception e){
            System.out.println("error reading file");
            e.printStackTrace();
        }
        //initialise container
        container_f[] container = new container_f[40];
        for(int i = 0; i < 40; i++){
            container_f tmp = new container_f(i, 0, false);
            container[i] = tmp;
        }

        //System.out.println(functions[10861].S_time); // 74?


        //this.something = param;
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
            //something++;
            schedule(new E(now() + 10.0));
        }
    }

    class request extends Event {
        // need to keep track of IDs
        public request(double t) {
            super(t);
        }

        public void invoke() {
            //schedule next request
            // check whether f is in container and available - determines whether request lost (busy = true)
            // schedule new request
            schedule(new E(now() + 10.0));
        }
    }

    class completion extends Event {
        public completion(double t) {
            super(t);
        }

        public void invoke() {
            //set busy to false
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
        new FaaS(param);
    }
}


