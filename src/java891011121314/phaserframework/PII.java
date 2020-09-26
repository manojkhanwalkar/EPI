package java891011121314.phaserframework;

public class PII
{
    public String name ;
    public String tel;
    public String ssn;
    public String email;

    boolean status=true;
    String errors;

    public synchronized void updateStatus(boolean status, String error)
    {

        if (this.status && !status) // if already marked false , do not update the status
        {
           this.status = status;
        }

        if (!error.equals("") && !status)
        {
            errors = errors + " " + error;
        }
    }
}
