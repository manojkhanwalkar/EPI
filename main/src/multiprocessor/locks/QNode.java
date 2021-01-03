package multiprocessor.locks;

import java.util.concurrent.atomic.AtomicReference;

public class QNode {

    public enum State {FREE, LOCKED, RELEASED, ABORTED}


    AtomicReference<State> state;


    volatile QNode prev;


    public QNode()
    {
        state = new AtomicReference<>(State.FREE);
    }

}
