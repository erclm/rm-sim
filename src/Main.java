public class Main {

    public static void main(String[] args) {

        Scheduler s = new Scheduler();
        s.joinAll();

        try{
            s.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        s.printResults();
    }
}
