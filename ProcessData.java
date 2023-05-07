public class ProcessData implements Cloneable {

    private final String processName;
    private final int arrivalTime;
    private int executionTime;
    private int reactionPoint;
    private double priority;
    private int waitingTime;
    private int responseTime;
    private int turnaroundTime;

    public ProcessData(String[] list) {
        this.processName = list[0];
        this.arrivalTime = Integer.parseInt(list[1]);
        this.executionTime = Integer.parseInt(list[2]);
        this.priority = Integer.parseInt(list[3]);
        this.reactionPoint = Integer.parseInt(list[4]);
        this.waitingTime = 0;
        this.responseTime = 0;
        this.turnaroundTime = 0;
    }

    public ProcessData(ProcessData data) {
        this.processName = data.processName;
        this.arrivalTime = data.arrivalTime;
        this.executionTime = data.executionTime;
        this.priority = data.priority;
        this.reactionPoint = data.reactionPoint;
        this.waitingTime = data.waitingTime;
        this.responseTime = data.responseTime;
        this.turnaroundTime = data.turnaroundTime;
    }

    public String getProcessName() {
        return this.processName;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public void setExecutionTime(int value) {
        this.executionTime = value;
    }

    public int getExecutionTime() {
        return this.executionTime;
    }

    public void setReactionPoint(int value) {
        this.reactionPoint = value;
    }

    public int getReactionPoint() {
        return this.reactionPoint;
    }

    public void setPriority(double value) {
        this.priority = value;
    }

    public double getPriority() {
        return this.priority;
    }

    public void setWaitingTime(int value) {
        this.waitingTime = value;
    }

    public int getWaitingTime() {
        return this.waitingTime;
    }

    public void setResponseTime(int value) {
        this.responseTime = value;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    public void setTurnaroundTime(int value) {
        this.turnaroundTime = value;
    }

    public int getTurnaroundTime() {
        return this.turnaroundTime;
    }

    @Override
    public ProcessData clone() throws CloneNotSupportedException {
        return (ProcessData) super.clone();
    }
}
