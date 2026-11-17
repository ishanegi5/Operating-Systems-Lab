import java.util.concurrent.Semaphore;

public class DiningPhilosophers {

    static final int N = 5;

    static Semaphore[] forks = new Semaphore[N];

    static Semaphore room = new Semaphore(N - 1);

    static {
        for (int i = 0; i < N; i++) {
            forks[i] = new Semaphore(1);
        }
    }

    static class Philosopher implements Runnable {

        int id;

        Philosopher(int id) {
            this.id = id;
        }

        public void run() {

            int left = id;
            int right = (id + 1) % N;

            for (int cycle = 0; cycle < 2; cycle++) {

                boolean roomEntered = false;
                boolean leftAcquired = false;
                boolean rightAcquired = false;

                try {

                    System.out.println(
                        "Philosopher " + id + " is thinking"
                    );

                    Thread.sleep(100);

                    room.acquire();
                    roomEntered = true;

                    forks[left].acquire();
                    leftAcquired = true;

                    forks[right].acquire();
                    rightAcquired = true;

                    System.out.println(
                        "Philosopher " + id
                        + " is eating (fork "
                        + left + " and fork " + right + ")"
                    );

                    Thread.sleep(150);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    break;

                } finally {

                    if (rightAcquired) {
                        forks[right].release();
                    }

                    if (leftAcquired) {
                        forks[left].release();
                    }

                    if (roomEntered) {
                        room.release();
                    }
                }

                System.out.println(
                    "Philosopher " + id
                    + " finished eating and put down forks"
                );
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        Thread[] philo = new Thread[N];

        for (int i = 0; i < N; i++) {

            philo[i] = new Thread(new Philosopher(i));

            philo[i].start();
        }

        for (int i = 0; i < N; i++) {

            philo[i].join();
        }
    }
}
