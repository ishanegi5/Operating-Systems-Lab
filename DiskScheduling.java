import java.util.Scanner;
import java.util.Arrays;

public class DiskScheduling {

    static final int MAX = 50;

    static int absDiff(int a, int b) {
        return Math.abs(a - b);
    }

    static void fcfs(int[] req, int n, int head) {

        int total = 0;
        int cur = head;

        for (int i = 0; i < n; i++) {
            total += absDiff(cur, req[i]);
            cur = req[i];
        }

        System.out.println(
            "FCFS total head movement = " + total
        );
    }

    static void sstf(int[] req, int n, int head) {

        boolean[] visited = new boolean[n];

        int total = 0;
        int cur = head;

        for (int c = 0; c < n; c++) {

            int idx = -1;
            int best = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {

                if (!visited[i] && absDiff(cur, req[i]) < best) {
                    best = absDiff(cur, req[i]);
                    idx = i;
                }
            }

            visited[idx] = true;

            total += best;
            cur = req[idx];
        }

        System.out.println(
            "SSTF total head movement = " + total
        );
    }

    static void scan(int[] req, int n, int head, int diskSize) {

        int[] arr = Arrays.copyOf(req, n + 1);
        arr[n] = head;

        Arrays.sort(arr);

        int idx = 0;

        for (int i = 0; i <= n; i++) {
            if (arr[i] == head) {
                idx = i;
                break;
            }
        }

        int total = 0;
        int cur = head;

        // Move towards the higher end first
        for (int i = idx + 1; i <= n; i++) {
            total += absDiff(cur, arr[i]);
            cur = arr[i];
        }

        total += absDiff(cur, diskSize - 1);
        cur = diskSize - 1;

        // Reverse direction
        for (int i = idx - 1; i >= 0; i--) {
            total += absDiff(cur, arr[i]);
            cur = arr[i];
        }

        System.out.println(
            "SCAN total head movement = " + total
        );
    }

    static void cscan(int[] req, int n, int head, int diskSize) {

        int[] arr = Arrays.copyOf(req, n + 1);
        arr[n] = head;

        Arrays.sort(arr);

        int idx = 0;

        for (int i = 0; i <= n; i++) {
            if (arr[i] == head) {
                idx = i;
                break;
            }
        }

        int total = 0;
        int cur = head;

        // Move towards the higher end
        for (int i = idx + 1; i <= n; i++) {
            total += absDiff(cur, arr[i]);
            cur = arr[i];
        }

        // Go to the end of the disk
        total += absDiff(cur, diskSize - 1);

        // Jump back to cylinder 0
        total += (diskSize - 1);
        cur = 0;

        // Service remaining requests
        for (int i = 0; i < idx; i++) {
            total += absDiff(cur, arr[i]);
            cur = arr[i];
        }

        System.out.println(
            "C-SCAN total head movement = " + total
        );
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n, head, diskSize;

        int[] req = new int[MAX];

        System.out.print("Enter number of requests: ");
        n = sc.nextInt();

        System.out.print("Enter the requests: ");

        for (int i = 0; i < n; i++) {
            req[i] = sc.nextInt();
        }

        System.out.print("Enter initial head position: ");
        head = sc.nextInt();

        System.out.print("Enter disk size (max cylinder + 1): ");
        diskSize = sc.nextInt();

        fcfs(req, n, head);
        sstf(req, n, head);
        scan(req, n, head, diskSize);
        cscan(req, n, head, diskSize);

        sc.close();
    }
}
