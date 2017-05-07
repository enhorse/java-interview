package xyz.enhorse.example.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:go2amd@gmail.com">enhorse</a>
 *         17.01.2017
 */
public class VolatileBehaviour {

    private static final Logger LOGGER = LoggerFactory.getLogger(VolatileBehaviour.class);

    //private static long MY_INT = 0;
    private static volatile long MY_INT = 0;


    public static void main(String[] args) {
        new ChangeListener().start();
        new ChangeMaker().start();
    }


    static class ChangeListener extends Thread {

        @Override
        public void run() {
            long local_value = MY_INT;
            while (local_value < 5) {
                if (local_value != MY_INT) {
                    LOGGER.info("Got Change for MY_INT : " + MY_INT);
                    local_value = MY_INT;
                }
            }
        }
    }

    static class ChangeMaker extends Thread {

        @Override
        public void run() {

            long local_value = MY_INT;
            while (MY_INT < 5) {
                LOGGER.info("Incrementing MY_INT to " + (local_value + 1));
                MY_INT = ++local_value;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
