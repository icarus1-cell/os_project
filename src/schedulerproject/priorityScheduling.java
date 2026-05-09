package schedulerproject;

import java.util.List;

class PriorityScheduling {

    public static Metrics runPriority(List<Process> processes) {
        int time = 0;
        int completed = 0;
        int n = processes.size();

        boolean[] done = new boolean[n];

        System.out.println("\nGantt Chart (Priority):");

        while (completed < n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);

                if (!done[i] && p.arrivalTime <= time) {
                    if (p.priority < highestPriority) {
                        highestPriority = p.priority;
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                time++;
                continue;
            }

            Process p = processes.get(idx);

            if (!p.started) {
                p.responseTime = time - p.arrivalTime;
                p.started = true;
            }

            System.out.print("P" + p.id + " ");

            time += p.burstTime;

            p.completionTime = time;
            p.turnaroundTime = time - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;

            done[idx] = true;
            completed++;
        }

        return printResults.printResults(processes, "Priority Scheduling");
    }
}