package scheduler.cfs;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.TreeSet;

public class CFSTasksArray {

    public static final int PriorityLevels = 140;

    TreeSet<CFSTask> tasks = new TreeSet<>();



    public void add(CFSTask task) {

        tasks.add(task);
    }

    public Optional<CFSTask> get() {

        if (tasks.isEmpty())
            return Optional.empty();

        else {

            CFSTask task = tasks.first();
            tasks.remove(task);
            return Optional.of(task);
        }
    }

}




