package java891011121314.phaserframework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSNValidator implements Validator
{
    Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}");

    @Override
    public boolean validate(PII pii)
    {
        String ssn = pii.ssn;
        Matcher matcher = pattern.matcher(ssn);

        return matcher.matches();
    }
}
