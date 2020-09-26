package scheduler.O1Scheduler;

import java.util.Random;

import static scheduler.O1Scheduler.TasksArray.PriorityLevels;

public class ScheduleUser {


    public static void main(String[] args)  throws Exception {


        Scheduler scheduler = new Scheduler();

        Random random = new Random();

      //  while(true) {

            for (int i = 0; i < 100; i++) {
              //  CFSTask task = new CFSTask(random.nextInt(Integer.MAX_VALUE), 100, random.nextInt(PriorityLevels));
                Task task = new Task(i, 100, 139-i);
                scheduler.add(task);

                Thread.sleep(100);

            }


        //    Thread.sleep(5000);




//       }



    }

}

