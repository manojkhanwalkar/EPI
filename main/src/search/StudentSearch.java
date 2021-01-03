package search;

import java.util.*;

public class StudentSearch {


    static class Student implements Comparable<Student>
    {
        double gpa;

        String name ;


        public int compareTo(Student other)
        {
            int res = Double.compare(gpa,other.gpa);
            if (res==0) {
                return name.compareTo(other.name);
            }
            else
            {
             return res;
            }
        }

        public Student(double gpa, String name) {
            this.gpa = gpa;
            this.name = name;
        }


        @Override
        public String toString() {
            return "Student{" +
                    "gpa=" + gpa +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


    static class StudentComparator implements Comparator<Student>
    {
        @Override
        public int compare(Student s1, Student t1) {

            return Double.compare(t1.gpa,s1.gpa);
        }
    }

    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();

        Random random = new Random();

        for (int i=0;i<10;i++)
        {
            students.add(new Student(random.nextInt(40)/10, "S"+random.nextInt(5)));
        }

        Collections.sort(students);

        System.out.println(students);

        Collections.sort(students,new StudentComparator());

        System.out.println(students);

        Collections.sort(students,(s1,s2)->{ return s1.name.compareTo(s2.name);});

        System.out.println(students);

    }


}
