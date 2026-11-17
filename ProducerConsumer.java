import java.util.concurrent.Semaphore;

public class ProducerConsumer {

    static final int BUFFER_SIZE = 5;
    static final int ITEMS = 10;

    static int[] buffer = new int[BUFFER_SIZE];

    static int in = 0, out = 0;

    static Semaphore empty =
            new Semaphore(BUFFER_SIZE);

    static Semaphore full =
            new Semaphore(0);

    static Semaphore mutex =
            new Semaphore(1);

    static class Producer implements Runnable {

        public void run() {

            try {

                for (int i = 1; i <= ITEMS; i++) {

                    empty.acquire();
                    mutex.acquire();

                    buffer[in] = i;

                    System.out.println(
                            "Producer produced item "
                                    + i + " at slot " + in);

                    in = (in + 1) % BUFFER_SIZE;

                    mutex.release();
                    full.release();

                    Thread.sleep(100);

                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {

        public void run() {

            try {

                for (int i = 1; i <= ITEMS; i++) {

                    full.acquire();
                    mutex.acquire();

                    int item = buffer[out];

                    System.out.println(
                            "\t\tConsumer consumed item "
                                    + item + " from slot " + out);

                    out = (out + 1) % BUFFER_SIZE;

                    mutex.release();
                    empty.release();

                    Thread.sleep(150);

                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        Thread prod = new Thread(new Producer());
        Thread cons = new Thread(new Consumer());

        prod.start();
        cons.start();

        prod.join();
        cons.join();

        System.out.println(
                "All items produced and consumed.");
    }
}
