import java.util.concurrent.Semaphore;

public class Scheduler extends java.lang.Thread{
    Semaphore sem;
    workThread t1;
    workThread t2;
    workThread t3;
    workThread t4;

    public int time;
    private int overrun1 = 0;
    private int overrun2 = 0;
    private int overrun3 = 0;
    private int overrun4 = 0;

    public Scheduler(){
        this.t1 = new workThread(1, 2);
        this.t2 = new workThread(2, 3);
        this.t3 = new workThread(4, 4);
        this.t4 = new workThread(16, 5);
        time = 0;

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        schedule();
        killThreads();

    }

    public void schedule(){
        t1.sem.release();
        t2.sem.release();
        t3.sem.release();
        t4.sem.release();
        for(int i = 0; i < 160; ++i)
        {
            try {
                Thread.sleep(10);
            }
            catch(Exception e){
                e.printStackTrace();
            }

            time++;
            //check if previous iteration failed to complete

            //t1
            if (!t1.isDone()) {
                overrun1++;
            }
            if(time > 0) {
                t1.sem.release();
            }
            //t2
            if (time % 2 == 0) {
                if (!t2.isDone()) {
                    overrun2++;
                }
                t2.sem.release();
            }
            //t3
            if (time % 4 == 0) {
                if (!t3.isDone()) {
                    overrun3++;
                }
                t3.sem.release();
            }
            //t4
            if (time % 16 == 0) {
                if (!t4.isDone()) {
                    overrun4++;
                }
                t4.sem.release();
            }
        }
    }

    public void joinAll(){
        try{
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void killThreads(){
        t1.doForceQuit();
        t2.doForceQuit();
        t3.doForceQuit();
        t4.doForceQuit();
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
        t4.interrupt();
    }
    public void printResults(){
        System.out.println("RESULTS");
        System.out.println("Thread 1 completions:" + t1.completions);
        System.out.println("Thread 1 overruns:" + overrun1);
        System.out.println();
        System.out.println("Thread 2 completions:" + t2.completions);
        System.out.println("Thread 2 overruns:" + overrun2);
        System.out.println();
        System.out.println("Thread 3 completions:" + t3.completions);
        System.out.println("Thread 3 overruns:" + overrun3);
        System.out.println();
        System.out.println("Thread 4 completions:" + t4.completions);
        System.out.println("Thread 4 overruns:" + overrun4);
    }
}
