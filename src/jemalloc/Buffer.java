package jemalloc;

public class Buffer {


    protected long threadIdOfAllocation;

    private char [] buf;

    public Buffer(int size)
    {
        buf = new char[size];
    }

    public char get(int loc)
    {
        return buf[loc];
    }

    public void put(char c, int loc)
    {
        buf[loc] = c;
    }

    public int size()
    {
        return buf.length;
    }



}

