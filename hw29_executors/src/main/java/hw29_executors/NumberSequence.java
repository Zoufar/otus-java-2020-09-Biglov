package hw29_executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class NumberSequence {
    private static final Logger logger = LoggerFactory.getLogger(NumberSequence.class);
    private int counter = 1;
    private int direction = 1;
    private int counterUniDir = 0;
    private List<Thread> threads = new ArrayList<>();
    private static final int LIMIT = 100_000_000;
    private CyclicBarrier barrier;


    private synchronized void action(int rest) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                //spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                //поэтому не if
                while (counter % 2 == rest) {
                    this.wait();
                }
                logger.info("counter = {}", counter);
                counter = counter + direction;
                direction = (counter == 1 || counter == 10) ? direction * (-1) : direction;
                notifyAll();
                sleep();
 //             logger.info("after notify, i:{}",i);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new NotInterestingException(ex);
            }
        }
    }

    public static void main(String[] args) {
        NumberSequence numberSequence = new NumberSequence();

 //       numberSequence.winkle();

        int numberOfThreads = 3;
        numberSequence.winkle2(numberOfThreads);

    }

    private void winkle(){
       new Thread(() -> this.action(0)).start();
       new Thread(() -> this.action(1)).start();

    }


    private void winkle2(int numberOfThreads) {
        Runnable barrierAction = () -> {
 //           logger.info("barrier at counter = {}", counter)
        };
        barrier = new CyclicBarrier(numberOfThreads, barrierAction);

        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(new Action2(i, numberOfThreads));
            threads.add(thread);
            thread.start();
        }
    }

     class Action2 implements Runnable {
        int threadNum;
        int numberOfThreads;
        Action2(int threadNum, int numberOfThreads ) {
            this.threadNum = threadNum;
            this.numberOfThreads = numberOfThreads;
        }

        public void run() {
            for (int i = 0; i < LIMIT; i++) {
                synchronized (NumberSequence.this) {
                    if (counterUniDir  % numberOfThreads == threadNum) {
                        logger.info("counter = {}", counter);
                        counterUniDir++;
                        counter = counter + direction;
                        direction = (counter == 1 || counter == 10) ? direction * (-1) : direction;
                        sleep();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    logger.error(ex.getMessage(), ex);
                } catch (BrokenBarrierException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
     }


    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }


    private static class NotInterestingException extends RuntimeException {
        NotInterestingException(InterruptedException ex) {
            super(ex);
        }
    }

}
