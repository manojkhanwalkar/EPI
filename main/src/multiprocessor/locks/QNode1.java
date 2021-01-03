package multiprocessor.locks;

public class QNode1 {

    volatile boolean locked = false;
    QNode1 prev;

    volatile boolean abandoned = false;
}
