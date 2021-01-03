package memmanager;

//TODO - make this runnable to allow multiple processes to run

public class Process  {

    ProcMemory procMemory ;
    char c ;

    public Process (String name, char c)
    {
        procMemory = new ProcMemory(name,100000,MainMemory.getInstance());

        procMemory.mainMemory.register(name);

        this.c = c;
    }


    private char read(int location)
    {
        return procMemory.read(location);
    }


    private void write(int location, char c)
    {
        procMemory.write(location,c);
    }


    public void run()
    {
        for (int i=0;i<100000;i++)
        {
            write(i,c);
        }

        for (int i=0;i<100000;i=i+12500)
        {
            System.out.println(read(i));
        }
    }

}
