package java891011121314.phaserframework;

public class Sema4 {


    MuteX mutex = new MuteX(1);
    public Sema4()
    {

    }


    public void acquire()
    {
        mutex.acquire();
    }


    public void release()
    {
        mutex.release();
    }



}
