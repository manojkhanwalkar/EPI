package scheduler.cfs;

import java.util.Random;

import static scheduler.cfs.CFSTasksArray.PriorityLevels;

public class CFSScheduleUser {


    public static void main(String[] args)  throws Exception {


        CFSScheduler CFSScheduler = new CFSScheduler();

        Random random = new Random();

      //  while(true) {

            for (int i = 0; i < 100; i++) {
                CFSTask CFSTask = new CFSTask(random.nextInt(Integer.MAX_VALUE), 100, random.nextInt(PriorityLevels));
                //CFSTask CFSTask = new CFSTask(i, 100, 139-i);
                CFSScheduler.add(CFSTask);

                Thread.sleep(100);

            }


        //    Thread.sleep(5000);




//       }



    }

}

