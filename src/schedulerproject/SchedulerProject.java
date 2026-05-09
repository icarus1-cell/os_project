package schedulerproject;

import java.util.*;

public class SchedulerProject {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select Scenario:");
        System.out.println("1. Basic Mixed Workload");
        System.out.println("2. Urgency Case");
        System.out.println("3. Fairness Case");
        System.out.println("4. Starvation Case");
        System.out.println("5. Validation Case");

        int scenario = sc.nextInt();

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        if (n <= 0) {
            System.out.println("Invalid number of processes!");
            return;
        }

        List<Process> processesRR = new ArrayList<>();
        List<Process> processesPR = new ArrayList<>();
        
        Set<Integer> usedIds = new HashSet<>();

        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1));

            System.out.print("Arrival Time: ");
            int at = sc.nextInt();

            System.out.print("Burst Time: ");
            int bt = sc.nextInt();

            System.out.print("Priority: ");
            int pr = sc.nextInt();

   
            if (bt <= 0) {
                System.out.println("Invalid Burst Time! Burst Time must be greater than 0.");
                return;
            }
            

            if (at < 0) {
                System.out.println("Invalid Arrival Time! Arrival Time cannot be negative.");
                return;
            }
            
 
            if (pr < 0) {
                System.out.println("Invalid Priority! Priority cannot be negative.");
                return;
            }
            

            int id = i + 1;
            if (usedIds.contains(id)) {
                System.out.println("Invalid! Duplicate Process ID: P" + id);
                System.out.println("Each process must have a unique ID.");
                return;
            }
            usedIds.add(id);

            processesRR.add(new Process(id, at, bt, pr));
            processesPR.add(new Process(id, at, bt, pr));
        }

        System.out.print("Enter Time Quantum: ");
        int quantum = sc.nextInt();


        if (quantum <= 0) {
            System.out.println("Invalid Quantum! Time Quantum must be greater than 0.");
            return;
        }

        System.out.println("\nRunning Scenario " + scenario);

        Metrics rrMetrics = RoundRobin.runRR(processesRR, quantum);
        Metrics prMetrics = PriorityScheduling.runPriority(processesPR);

        Analysis.compare(rrMetrics, prMetrics);
    }
}