package tail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleAlerter implements Alerter {

    static final String DefaultPattern = "^[Error|Exception]";

    String regex;

    Pattern pattern;

    public ConsoleAlerter(String regex)
    {
        this.regex = regex;
        pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
    }


    public static ConsoleAlerter getDefaultConsoleAlerter()
    {
        return new ConsoleAlerter(DefaultPattern);
    }

    @Override
    public void raise(String input) {

        Matcher matcher = pattern.matcher(input);

        if (matcher.find())
        {
            System.out.println(input);
        }

    }

   public static void main(String[] args) {

        ConsoleAlerter consoleAlerter = getDefaultConsoleAlerter();

        consoleAlerter.raise("Error in this line");
    }

   /* public static void main(String args[])
    {
        // Create a pattern to be searched
        Pattern pattern =
                Pattern.compile("ge*", Pattern.CASE_INSENSITIVE);

        // Search above pattern in "geeksforgeeks.org"
        Matcher m = pattern.matcher("GeeksforGeeks.org");

        // Print starting and ending indexes of the pattern
        // in text
        while (m.find())
            System.out.println("Pattern found from " + m.start() +
                    " to " + (m.end()-1));
    }*/
}
