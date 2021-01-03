package mesif;

public class CacheLine {

    char[] buf = null;

    int address=-1;

    CacheLineStates status = CacheLineStates.Invalid;

    public void put(char[] buf)
    {
        this.buf = buf;
    }


    public char[] get()
    {
        return buf;
    }

    public char get(int index)
    {
        return buf[index];
    }

    public void put(int index, char c)
    {
        buf[index] = c;
    }
}

