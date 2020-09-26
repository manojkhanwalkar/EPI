package java891011121314.patterns;

import java891011121314.phaserframework.CBarrier;
import java891011121314.phaserframework.CDLatch;
import java891011121314.phaserframework.Phazer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strategy {


    @FunctionalInterface
    interface Validator
    {

         boolean validate(String str);
    }

    static class InputValidator implements Validator
    {

        @Override
        public boolean validate(String str)
        {

            if (str!=null)
                return true;
            else
                return false;
        }
    }

    static class NameValidator implements Validator
    {

        Pattern pattern = Pattern.compile("[A-Z][a-z]+");


        @Override
        public boolean validate(String str)
        {
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
    }


    static class EmailValidator implements Validator
    {
        Pattern pattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]+@([a-z]+\\.)+(com||edu||gov)");


        @Override
        public boolean validate(String str)
        {
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
    }

    static class SSNValidator implements Validator
    {
        Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}");

        @Override
        public boolean validate(String str)
        {
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
    }




    Validator inputValidator = new InputValidator();
    Validator nameValidator = new NameValidator();
    Validator emailValidator = new EmailValidator();
    Validator ssnValidator = new SSNValidator();
    Validator telValidator = new TelephoneValidator();




    static class TelephoneValidator implements Validator
    {
        //Pattern pattern = Pattern.compile("([0-9]{2})?\\( [0-9]{3}\\) [0-9]{3} - [0-9]{4}");
        Pattern pattern = Pattern.compile("([0-9]{2}-)?\\([0-9]{3}\\)[0-9]{3}-[0-9]{4}");

        @Override
        public boolean validate(String str)
        {
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
    }


    public boolean isValid(String str, Validator validator)
    {
        return validator.validate(str);
    }

    public static void main(String[] args) {

        Strategy controller = new Strategy();


        String email="manoj@test.com";
        String name="Manoj";
        String tel="01(732)888-0000";
        String ssn = "909-99-8888";

       // System.out.println(controller.isValid2(pii));

       // System.out.println(controller.emailValidator.validate(email));

        Validator lowercase = (s)-> { return s.matches("[a-z]*"); };

        Validator isChars = (s) -> { return s.matches("[a-z,A-Z]*"); } ;

        System.out.println(controller.isValid(email,lowercase));
        System.out.println(controller.isValid(name,isChars));

   /*     System.out.println(controller.isValid(pii));


        System.out.println(controller.isValid1(pii));*/


    }



}
