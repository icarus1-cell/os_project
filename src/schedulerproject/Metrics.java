package schedulerproject;

class Metrics {
    double avgWT;
    double avgTAT;
    double avgRT;

    public Metrics(double wt, double tat, double rt) {
        this.avgWT = wt;
        this.avgTAT = tat;
        this.avgRT = rt;
    }
}