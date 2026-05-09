package schedulerproject;

import java.util.*;

class RoundRobin {

    public static Metrics runRR(List<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>();
        int time = 0;
        int completed = 0;
        int n = processes.size();

     
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));


        int i = 0;
        while (i < n && processes.get(i).arrivalTime <= time) {
            queue.add(processes.get(i));
            i++;
        }


        System.out.println("\nGantt Chart (RR):");
        System.out.flush(); 

        while (completed < n) {

      
            if (queue.isEmpty()) {
                if (i < n) {
                    time = processes.get(i).arrivalTime;
                    while (i < n && processes.get(i).arrivalTime <= time) {
                        queue.add(processes.get(i));
                        i++;
                    }
                } else {
                    time++;
                    continue;
                }
            }

            Process p = queue.poll();

      
            if (!p.started) {
                p.responseTime = time - p.arrivalTime;
                p.started = true;
            }

          
            int execTime = Math.min(quantum, p.remainingTime);
            
      
            System.out.print("P"+ p.id + " ");
            System.out.flush(); 
            time += execTime;
            p.remainingTime -= execTime;

            while (i < n && processes.get(i).arrivalTime <= time) {
                queue.add(processes.get(i));
                i++;
            }

          
            if (p.remainingTime > 0) {
                queue.add(p);
            } else {

                completed++;
                p.completionTime = time;
                p.turnaroundTime = time - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
            }
        }

        System.out.println("\n"); 
        return printResults.printResults(processes, "Round Robin");
    }
}