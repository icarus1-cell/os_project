package schedulerproject;

import java.util.List;

class printResults {

    public static Metrics printResults(List<Process> processes, String title) {
        double totalWT = 0, totalTAT = 0, totalRT = 0;

        System.out.println("\n\n" + title + " Results:");
        System.out.println("ID\tWT\tTAT\tRT");

        for (Process p : processes) {
            System.out.println("P" + p.id + "\t" + p.waitingTime + "\t" +
                               p.turnaroundTime + "\t" + p.responseTime);

            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
            totalRT += p.responseTime;
        }

        int n = processes.size();

        double avgWT = totalWT / n;
        double avgTAT = totalTAT / n;
        double avgRT = totalRT / n;

        System.out.println("Average WT: " + avgWT);
        System.out.println("Average TAT: " + avgTAT);
        System.out.println("Average RT: " + avgRT);

        return new Metrics(avgWT, avgTAT, avgRT);
    }
}