package schedulerproject;

class Analysis {

    public static void compare(Metrics rr, Metrics pr) {

        System.out.println("\n===== Analysis =====");

     
        if (rr.avgWT < pr.avgWT)
            System.out.println("Round Robin has better average waiting time.");
        else
            System.out.println("Priority Scheduling has better average waiting time.");

       
        if (rr.avgRT < pr.avgRT)
            System.out.println("Round Robin has better response time.");
        else
            System.out.println("Priority Scheduling has better response time.");

   
        System.out.println("Round Robin distributes CPU time more evenly (better fairness).");

       
        System.out.println("Priority Scheduling may cause starvation for low-priority processes.");

        System.out.println("====================");
    }
}