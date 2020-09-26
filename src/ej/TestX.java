package ej;

public class TestX {


    public static void main(String[] args) {

        long x =4, y=6, z=9;

        long f=1 , s=1, t=1;

        for (int i=1;i<100;i++)
        {
            f = f*x;
            s = s*y;
            t = t*z;

            System.out.println(i + " " + (f+s) + " " + t);
            if ((f+s)==t)
                break;
        }


    }
}
