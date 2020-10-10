package memmanager;

public class MemPage {

    char[] buf = new char[Page.size];

    final int pageNum;

    public MemPage(int pageNum) {
        this.pageNum = pageNum;
    }


    public void write(int location , char c)
    {
        buf[location] = c;
    }

    public char read(int location)
    {
        return buf[location];
    }
}
