package java891011121314.phaserframework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator implements Validator
{

    Pattern pattern = Pattern.compile("[A-Z][a-z]+");


    @Override
    public boolean validate(PII pii)
    {
        String name = pii.name;
        Matcher matcher = pattern.matcher(name);


        return matcher.matches();
    }
}
