package zk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DistributedLock implements Lock {

    Collection<Server> servers;
    ThreadLocalRandom random = ThreadLocalRandom.current();

    public DistributedLock(Collection<Server> servers)
    {
        this.servers = servers;
    }

    public void lock()
    {
        while(true) {
            List<Server> successfulLock = new ArrayList<>();
            boolean success = true;
            for (Server server : servers) {
                if (server.treeLock.tryLock()) {
                    successfulLock.add(server);
                }
                else
                {
                    success = false;
                    successfulLock.forEach(s->server.treeLock.unlock());

                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            if (success)
                return;


        }


    }


    public void unlock()
    {
        servers.stream().forEach(s-> s.treeLock.unlock());
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
