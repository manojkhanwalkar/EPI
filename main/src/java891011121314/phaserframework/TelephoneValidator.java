package java891011121314.phaserframework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelephoneValidator implements Validator
{
    //Pattern pattern = Pattern.compile("([0-9]{2})?\\( [0-9]{3}\\) [0-9]{3} - [0-9]{4}");
    Pattern pattern = Pattern.compile("([0-9]{2}-)?\\([0-9]{3}\\)[0-9]{3}-[0-9]{4}");

    @Override
    public boolean validate(PII pii)
    {
        String tel = pii.tel;
        Matcher matcher = pattern.matcher(tel);
        return matcher.matches();
    }
}
