package scheduler.mflq;

import scheduler.cfs.CFSScheduler;
import scheduler.cfs.CFSTask;

import java.util.Random;

import static scheduler.cfs.CFSTasksArray.PriorityLevels;
import static scheduler.mflq.MFLQScheduler.PriorityLevel1;

public class MFLQScheduleUser {


    public static void main(String[] args)  throws Exception {


        MFLQScheduler scheduler = new MFLQScheduler();

        Random random = new Random();

      //  while(true) {

            for (int i = 0; i < 10; i++) {
                MFLQTask task = new MFLQTask(Integer.MAX_VALUE,random.nextInt(PriorityLevel1));
                //CFSTask CFSTask = new CFSTask(i, 100, 139-i);
                scheduler.add(task);

                Thread.sleep(100);

            }


        //    Thread.sleep(5000);




//       }



    }

}

