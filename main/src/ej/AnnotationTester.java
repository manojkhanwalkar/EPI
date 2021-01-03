package ej;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationTester {

    @MyTest
    public void test1()
    {

        System.out.println("Called test1");

    }


    @MyTest
    public void test2()
    {

        System.out.println("Called test2");

    }


    public void test3()
    {

        System.out.println("Called test3");

    }


    public static void main(String[] args) throws Exception {

        AnnotationTester tester = new AnnotationTester();

        Method[] methods = AnnotationTester.class.getDeclaredMethods();

        for (Method method : methods)
        {

            Annotation annotation = method.getAnnotation(MyTest.class);
            if (annotation!=null)
            {
                method.invoke(tester);
            }

        }

    }



}
