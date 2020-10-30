package jemalloc1;

public class Buffer {


    protected long threadIdOfAllocation;

    private char [] buf;

    int start; int end;

    public Buffer(int start, int end, char[] buf)
    {
        this.start = start;
        this.end = end;

        this.buf = buf;
    }

    private boolean isValidLocation(int loc)
    {
        if (start<=loc && loc<=end)
                return true;
        else
            return false;

    }

    public char get(int loc)
    {
        if (isValidLocation(loc))
             return buf[loc];
        else
            throw new RuntimeException("Invalid position ");
    }

    public void put(char c, int loc)
    {
        if (isValidLocation(loc))
            buf[loc] = c;
        else
            throw new RuntimeException("Invalid position ");
    }

    public int size()
    {

        return end-start;
    }



}

