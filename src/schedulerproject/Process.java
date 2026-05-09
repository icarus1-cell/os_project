package schedulerproject;
class Process {
    int id;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int priority;

    int completionTime;
    int waitingTime;
    int turnaroundTime;
    int responseTime;

    boolean started = false;
    public Process(int id, int at, int bt, int pr) {
        this.id = id;
        this.arrivalTime = at;
        this.burstTime = bt;
        this.remainingTime = bt;
        this.priority = pr;
    }
}