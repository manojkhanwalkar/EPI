package multiprocessor.work;

import java.util.concurrent.atomic.AtomicReference;

public class WSQNode {

    public enum Status {NOTREADY, AVAIALABLE, BUSY , DONE}

    AtomicReference<Status> status = new AtomicReference<>(Status.NOTREADY);

    WSQRA task;

    WSQNode pred;

    public WSQNode(WSQRA task)
    {
        this.task = task;
        status.set(Status.NOTREADY);

    }


    public void process()
    {
        task.compute();

        this.status.set(Status.DONE);
    }

}
