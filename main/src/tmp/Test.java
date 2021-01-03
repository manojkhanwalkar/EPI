package tmp;

import java.time.LocalDateTime;
import java.util.Date;

public class Test {

    public String compute()
    {
        String curr = LocalDateTime.now().toString();


        return curr;
    }


    public static void main(String[] args) {

        Test test = new Test();

        System.out.println(test.compute());

    }


}
