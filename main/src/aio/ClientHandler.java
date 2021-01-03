package aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientHandler implements Runnable {

    AsynchronousSocketChannel worker ;

    public ClientHandler(AsynchronousSocketChannel worker) {
        this.worker = worker;
    }

    public void run()
    {
        while(true) {
            ByteBuffer readBuffer = ByteBuffer.allocate(100);
            ByteBuffer writeBuffer = ByteBuffer.allocate(100);
            writeBuffer.put("Response to client ".getBytes());
            writeBuffer.flip();
            try {
                if (worker.isOpen()) {
                    // read a message from the client, timeout after 10 seconds
                    int bytes = worker.read(readBuffer).get();
                    if (bytes==-1)
                        break;
                    else {
                        String s = BufferUtil.getString(readBuffer, bytes);

                        System.out.println("Message received from client: " + s);
                    }

                    worker.write(writeBuffer);
                }
                else{
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }



    }
}
