package password;

import org.bouncycastle.util.Arrays;

public class PasswordValidator {

    RulesConfig rulesConfig ;

    public PasswordValidator(RulesConfig rulesConfig) {
        this.rulesConfig = rulesConfig;
    }

    public boolean validate(String password)
    {
        int numSpecial=0;
        int numNumeric=0;
        int numSmall=0;
        int numCap=0;


        if (!isValidLength(password,rulesConfig.length))
        {
            System.out.println("Invalid length");
            return false;
        }

        for (int i=0;i<password.length();i++) {
            char c = password.charAt(i);
            if (isCap(c))
            {
                numCap++;
                continue;
            }
            if (isNumeric(c))
            {
                numNumeric++;
                continue;
            }
            if (isSmall(c))
            {
                numSmall++;
                continue;
            }
            if (isSpecial(c))
            {
                numSpecial++;
                continue;
            }

            System.out.println("Invalid character");
            return false;
        }

  /*      int numSpecial=0;
        int numNumeric=0;
        int numSmall=0;
        int numCap=0;
      validate */

        return (numCap>=rulesConfig.numLarge && numNumeric>=rulesConfig.numNumber && numSmall>=rulesConfig.numSmall && numSpecial>=rulesConfig.numSpecial);

    }


    char[] special = { '!','~','@' , '#' , '$' , '%' , '%' , '&' ,'^' ,'*' };

    public boolean isSpecial(char c )
    {

        return Arrays.contains(special,c);
    }


    public boolean isNumeric(char c)
    {
        int start = (int)'0';
        int end = (int)'9';
        int x = (int) c;

        return isBetween(start,end,x);

    }

    private boolean isBetween(int start , int end , int index)
    {
        return (index>=start && index<=end);

    }

    public boolean isCap(char c)
    {
        int start = (int)'A';
        int end = (int)'Z';
        int x = (int) c;

        return isBetween(start,end,x);
    }

    public boolean isSmall(char c)
    {
        int start = (int)'a';
        int end = (int)'z';
        int x = (int) c;

        return isBetween(start,end,x);
    }



    public boolean isValidLength(String password, int minLength )
    {
        return password.length() >= minLength;
    }


    public static void main(String[] args) {

        RulesConfig rulesConfig = new RulesConfig();
        PasswordValidator validator = new PasswordValidator(rulesConfig);

        System.out.println(validator.validate("testA$8uuio"));

    }


}
