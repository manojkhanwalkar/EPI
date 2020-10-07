package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AIOClient {
    AsynchronousSocketChannel client;
    Future<Void> connectFuture;

    public AIOClient(SocketAddress server) throws IOException {
        // open a new socket channel and connect to the server
        System.out.println("Open client channel");
        client = AsynchronousSocketChannel.open();
        System.out.println("Connect to server");
        connectFuture = client.connect(server);
    }

    public void run() {

        try {
            connectFuture.get();

            for (int i=0;i<10;i++) {
                // send a message to the server
                ByteBuffer message = ByteBuffer.wrap("ping".getBytes());
                // wait for the response
                System.out.println("Sending message to the server...");
                int numberBytes = client.write(message).get();

                ByteBuffer readBuffer = ByteBuffer.allocate(100);

                int num = client.read(readBuffer).get();

                System.out.println("Message received from server: " + BufferUtil.getString(readBuffer, num));

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        client.close();
    }

    public static void main(String[] args) throws Exception  {

        CompletableFuture[] cf = new CompletableFuture[5];
        for (int i=0;i<5;i++) {

            cf[i] = CompletableFuture.runAsync(()->{
                try {
                    AIOClient client = new AIOClient(new InetSocketAddress("127.0.0.1", 4555));
                    client.run();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

        }

        CompletableFuture.allOf(cf).join();




    }

}