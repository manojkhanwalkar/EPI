package timers;

public class TimerTest {

    public static void main(String[] args) throws Exception {


        ScheduledTimer timer = new ScheduledTimer();

        TaskHandle handle = timer.add(1000,2000, ()-> {

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Running first task " + Thread.currentThread().getName() ); });

        timer.add(2000,2000, ()-> { System.out.println("Running SECOND task " + Thread.currentThread().getName()); });


        timer.add(5000,2000, ()-> { System.out.println("Running LAST task " + Thread.currentThread().getName()); });

        Thread.sleep(100000);

        handle.cancel();

    }

}
