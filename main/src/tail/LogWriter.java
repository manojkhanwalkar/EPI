package tail;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class LogWriter {

    public static void main(String[] args) throws Exception {
        final Logger logger
                = LoggerFactory.getLogger("tail.LogWriter");

        for (int i=0;i<10;i++) {

            logger.warn("Test logging " + Instant.now());

            Thread.sleep(2000);

        }



    }
}
