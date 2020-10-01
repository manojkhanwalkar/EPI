package fuzzy;

import java.util.HashSet;
import java.util.Set;

public class NameRecordDB {

    Set<NameRecord> names = new HashSet<>();

    public void add(NameRecord nameRecord)
    {

        names.add(nameRecord);
    }

    public boolean contains(NameRecord nameRecord)
    {
        return names.contains(nameRecord);
    }

}
