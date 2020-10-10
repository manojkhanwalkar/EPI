package memmanager;

public class ProcMemory {

    String procName;

    final int totalMemory;

    MainMemory mainMemory;




    public ProcMemory(String procName, int totalMemory, MainMemory mainMemory) {
        this.procName = procName;
        this.totalMemory = totalMemory;

        this.mainMemory = mainMemory;
    }

    public void write (int location , char c)
    {
        if (location>=totalMemory)
            throw new RuntimeException("Invalid memory location access" );

        int page = location/Page.size;
        int offset = location%Page.size;

        mainMemory.write(procName,page,offset,c);

    }


    public char read(int location)
    {
        if (location>=totalMemory)
            throw new RuntimeException("Invalid memory location access" );

        int page = location/Page.size;
        int offset = location%Page.size;

        return mainMemory.read(procName,page,offset);

    }



}
