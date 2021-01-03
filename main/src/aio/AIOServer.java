package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class AIOServer {

public static void main(String[]args) throws Exception {

        // open a server channel and bind to a free address, then accept a connection
        System.out.println("Open server channel");

        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress("127.0.0.1", 4555));

        while(true) {
                System.out.println("Initiate accept");
                Future<AsynchronousSocketChannel> future = server.accept();
                // wait for the accept to finish
                AsynchronousSocketChannel worker = future.get();
                System.out.println("Accept completed");

                ClientHandler handler = new ClientHandler(worker);

                new Thread(handler).start();

        }


       // server.close();
  }


}

