package fuzzy;

import java.util.Objects;

public class NameRecord {

    String fname ;
    String lname;

    public NameRecord(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameRecord that = (NameRecord) o;
        return fname.equals(that.fname) &&
                lname.equals(that.lname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fname, lname);
    }

    @Override
    public String toString() {
        return "NameRecord{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                '}';
    }
}
