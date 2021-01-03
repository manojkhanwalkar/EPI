package tmp;

import java.util.Random;

public class AnotherTest {

    public static void main(String[] args) {

        AnotherTest atest = new AnotherTest();

        System.out.println(atest.compute());

    }


    Random random = new Random();


    public String compute()
    {
        return String.valueOf(random.nextInt(100000));
    }

}
