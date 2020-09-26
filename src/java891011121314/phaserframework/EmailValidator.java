package java891011121314.phaserframework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator
{
    Pattern pattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]+@([a-z]+\\.)+(com||edu||gov)");


    @Override
    public boolean validate(PII pii)
    {
        String email = pii.email;
        Matcher matcher = pattern.matcher(email);
     //   pii.updateStatus(false,"error in email format");
        return matcher.matches();
    }
}
