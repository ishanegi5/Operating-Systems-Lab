import java.util.concurrent.Semaphore;

public class ReadersWriters {

    static Semaphore mutex = new Semaphore(1);
    static Semaphore wrt = new Semaphore(1);

    static int read_count = 0;
    static int shared_data = 0;

    static class Reader implements Runnable {

        int id;

        Reader(int id) {
            this.id = id;
        }

        public void run() {

            try {

                mutex.acquire();

                read_count++;

                if (read_count == 1) {
                    wrt.acquire();
                }

                mutex.release();

                System.out.println(
                        "Reader " + id
                                + ": read shared_data = " + shared_data
                );

                Thread.sleep(200);

                mutex.acquire();

                read_count--;

                if (read_count == 0) {
                    wrt.release();
                }

                mutex.release();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }

    static class Writer implements Runnable {

        int id;

        Writer(int id) {
            this.id = id;
        }

        public void run() {

            try {

                wrt.acquire();

                shared_data++;

                System.out.println(
                        "Writer " + id
                                + ": wrote shared_data = " + shared_data
                );

                Thread.sleep(300);

                wrt.release();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        Thread[] readers = new Thread[5];

        Thread[] writers = new Thread[2];

        writers[0] = new Thread(new Writer(1));

        writers[0].start();

        for (int i = 0; i < 5; i++) {

            readers[i] = new Thread(new Reader(i + 1));

            readers[i].start();
        }

        writers[1] = new Thread(new Writer(2));

        writers[1].start();

        for (int i = 0; i < 5; i++) {
            readers[i].join();
        }

        writers[0].join();

        writers[1].join();

    }
}
