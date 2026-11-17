public class ThreadSum {
static final int NUM_THREADS = 4;
static class PartialSum implements Runnable {
int start, end;
long sum;
PartialSum(int start, int end) {
this.start = start;
this.end = end;
}
public void run() {
sum = 0;
for (int i = start; i < end; i++) {
sum += i;
}
System.out.println(
Thread.currentThread().getName()
+ ": sum[" + start + ".." + end
+ ") = " + sum
);
}
}
public static void main(String[] args)
throws InterruptedException {
CEC301/CSC301 — Operating Systems Lab Manual
Page 6 of 42
Thread[] threads = new Thread[NUM_THREADS];
PartialSum[] data = new PartialSum[NUM_THREADS];
int n = 1000000;
int chunk = n / NUM_THREADS;
long total = 0;
for (int i = 0; i < NUM_THREADS; i++) {
int start = i * chunk;
int end = (i == NUM_THREADS - 1)
? n : (i + 1) * chunk;
data[i] = new PartialSum(start, end);
threads[i] = new Thread(data[i],
"Thread-" + (i + 1));
threads[i].start();
}
for (int i = 0; i < NUM_THREADS; i++) {
threads[i].join();
total += data[i].sum;
}
System.out.println(
"Total sum (0 to " + (n - 1)
+ ") computed using " + NUM_THREADS
+ " threads = " + total
);
}
}
