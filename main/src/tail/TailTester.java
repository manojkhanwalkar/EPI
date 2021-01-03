package tail;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

import java.io.File;

public class TailTester {


        private static final int SLEEP = 10000;

        public static void main(String[] args) throws Exception {

            MyListener listener = new MyListener();
            Tailer tailer = Tailer.create(new File("/tmp/log.txt"), listener, SLEEP);
            while (true) {
                Thread.sleep(SLEEP);
            }
        }

        static class MyListener extends TailerListenerAdapter {

            @Override
            public void handle(String line) {
                System.out.println(line);
            }

        }

}
