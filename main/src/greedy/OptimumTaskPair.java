package greedy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OptimumTaskPair {

    static class TaskPair {

        Task task1;
        Task task2;

        public TaskPair(Task task1, Task task2) {
            this.task1 = task1;
            this.task2 = task2;
        }


        @Override
        public String toString() {
            return "TaskPair{" +
                    "task1=" + task1 +
                    ", task2=" + task2 +
                    '}';
        }
    }


    static class Task implements Comparable<Task>
    {
        int duration;
        String name;


        public Task(String name, int duration) {
            this.duration = duration;
            this.name = name;
        }


        @Override
        public String toString() {
            return "CFSTask{" +
                    "duration=" + duration +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Task task) {
            return Integer.compare(duration,task.duration);
        }
    }


    public static void main(String[] args) {

        Random random = new Random();

        List<Task> tasks = new ArrayList<>();
        List<TaskPair> optimumTasks = new ArrayList<>();

        for (int i=0;i<10;i++)
        {
            tasks.add(new Task("CFSTask"+i, random.nextInt(100)));
        }


        Collections.sort(tasks);

        // take first and last and pair them up .

        for (int i=0;i<tasks.size()/2;i++)
        {
            int next = tasks.size()-i-1;
            TaskPair taskPair = new TaskPair(tasks.get(i), tasks.get(next));
            optimumTasks.add(taskPair);
        }

        System.out.println(optimumTasks);


    }

}
