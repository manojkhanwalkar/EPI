package java891011121314.fjframework;

public abstract class RT<T> {

    public T join()
    {
        if (!forked)
            throw new RuntimeException("CFSTask has not been forked , cannot join");

            synchronized (this) {
                while(!completed) {

                    try {
                        wait();
                       // Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
        }

        return result;

    }


    T result;

    volatile boolean forked = false;

    volatile boolean completed=false;

 //  static  volatile FJ fjhandle;

      public static ThreadLocal<FJ>  fjhandle = new ThreadLocal<>();


    protected abstract T compute();

    public RT<T> fork()
    {


        fjhandle.get().fork(this);


        return this;
    }

}
