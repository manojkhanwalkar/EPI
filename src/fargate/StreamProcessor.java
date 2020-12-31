package fargate;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class StreamProcessor implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    public StreamProcessor(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {

        System.out.println("Running Stream Processor ");
        BufferedReader br=new BufferedReader(
                new InputStreamReader(
                        inputStream));
        String line;

        try {
            while((line=br.readLine())!=null){

                consumer.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}