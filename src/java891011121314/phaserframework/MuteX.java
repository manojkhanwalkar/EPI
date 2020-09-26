package java891011121314.phaserframework;

public class MuteX {


    final int resources ;

    int curr;
    public MuteX(int resources)
    {
        this.resources = resources;
        this.curr= 0;
    }

    public synchronized void acquire()
    {
        if (curr < resources)
        {
            curr++;
            return;
        }

        while(!(curr<resources))
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void release()
    {
        curr--;
        notifyAll();

    }


}
