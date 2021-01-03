package scheduler.O1Scheduler;

import java.util.*;

public class TasksArray {

    static final int PriorityLevels = 140;

    LinkedList<Task>[] tasksPriority = new LinkedList[140];

    BitSet tasksbits = new BitSet(PriorityLevels);

    public TasksArray() {
        for (int i = 0; i < PriorityLevels; i++) {
            tasksPriority[i] = new LinkedList<>();
        }

        tasksbits.clear();
    }

    public void add(Task task) {
        int priority = task.priority;
        tasksPriority[priority].add(task);
        tasksbits.set(priority, true);
    }

    // get the priority from the max task
    public Optional<Task> get() {
        for (int i = 0; i < PriorityLevels; i++) {
            if (tasksbits.get(i)) {
                Task task = tasksPriority[i].removeFirst();
                if (tasksPriority[i].isEmpty()) {
                    tasksbits.set(i, false);
                }

                return Optional.of(task);
            }
        }

        return Optional.empty();
    }

}




