import java.util.concurrent.Semaphore;

public class workThread extends Thread {
    public int runCount;
    public boolean done = false;
    public int[][] doWorkMatrix = new int[10][10];
    public Semaphore sem = new Semaphore(0);
    public int completions;
    public int priority;
    private int[] doWorkOrder = {1,3,5,7,9,0,2,4,6,8};
    private boolean forceQuit = false;

    public workThread(int runCountTemp, int priorityTemp) {
        this.completions = 0;
        this.runCount = runCountTemp;
        this.priority = Thread.MAX_PRIORITY - priorityTemp;
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                doWorkMatrix[i][j] = 1;
            }
        }
        this.forceQuit = false;
    }

    public boolean isDone() {
        return this.done;
    }

    //doWork, does the matrix conversion by the doWorkOrder
    public void doWork() {
        int temp = 0;
        for(int rep = 0; rep < 1000000; ++rep) {
            for(int k = 0; k < 10; ++k) {
                temp = doWorkOrder[k];
                for(int j = 0; j < 10; ++j) {
                    doWorkMatrix[j][temp] = doWorkMatrix[k][j];
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                while(!forceQuit) {
                    try {
                        sem.acquire();
                        done = false;
                        for(int i = 0; i < runCount; i++) {
                            doWork();
                            completions++;
                        }

                        done = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }if(forceQuit) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("HARD INTERRUPTED");
        }
    }

    public void doForceQuit() {
        this.forceQuit = true;
    }
}
