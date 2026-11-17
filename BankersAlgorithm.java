import java.util.Scanner;

public class BankersAlgorithm {

    static final int P = 5; // Number of processes
    static final int R = 3; // Number of resource types

    static int[] available = new int[R];
    static int[][] maximum = new int[P][R];
    static int[][] allocation = new int[P][R];
    static int[][] need = new int[P][R];

    static void calculateNeed() {
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }
    }

    static boolean isSafe(int[] safeSeq) {
        int[] work = available.clone();
        boolean[] finish = new boolean[P];

        int count = 0;

        while (count < P) {
            boolean found = false;

            for (int i = 0; i < P; i++) {
                if (!finish[i]) {
                    int j;

                    for (j = 0; j < R; j++) {
                        if (need[i][j] > work[j]) {
                            break;
                        }
                    }

                    if (j == R) {
                        for (int k = 0; k < R; k++) {
                            work[k] += allocation[i][k];
                        }

                        safeSeq[count++] = i;
                        finish[i] = true;
                        found = true;
                    }
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    static boolean requestResources(int pid, int[] request) {

        for (int j = 0; j < R; j++) {

            if (request[j] > need[pid][j]) {
                System.out.println(
                    "Error: process has exceeded its maximum claim."
                );
                return false;
            }

            if (request[j] > available[j]) {
                System.out.println(
                    "Request denied: not enough resources available."
                );
                return false;
            }
        }

        // Tentatively allocate resources
        for (int j = 0; j < R; j++) {
            available[j] -= request[j];
            allocation[pid][j] += request[j];
            need[pid][j] -= request[j];
        }

        int[] safeSeq = new int[P];

        if (isSafe(safeSeq)) {
            System.out.println("Request can be granted safely.");
            return true;
        } else {

            // Rollback allocation
            for (int j = 0; j < R; j++) {
                available[j] += request[j];
                allocation[pid][j] -= request[j];
                need[pid][j] += request[j];
            }

            System.out.println(
                "Request denied: would lead to an unsafe state."
            );

            return false;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print(
            "Enter available resources of each type (" + R + " types): "
        );

        for (int j = 0; j < R; j++) {
            available[j] = sc.nextInt();
        }

        System.out.println(
            "Enter Maximum matrix (" + P + " processes x " + R + " resources):"
        );

        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                maximum[i][j] = sc.nextInt();
            }
        }

        System.out.println("Enter Allocation matrix:");

        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                allocation[i][j] = sc.nextInt();
            }
        }

        calculateNeed();

        int[] safeSeq = new int[P];

        if (isSafe(safeSeq)) {
            System.out.println("System is in a SAFE state.");
            System.out.print("Safe sequence: ");

            for (int i = 0; i < P; i++) {
                System.out.print("P" + safeSeq[i] + " ");
            }

            System.out.println();

        } else {
            System.out.println("System is in an UNSAFE state.");
        }

        System.out.print(
            "\nEnter process number requesting resources: "
        );

        int pid = sc.nextInt();

        if (pid < 0 || pid >= P) {
            System.out.println("Invalid process number.");
        } else {
            int[] request = new int[R];

            System.out.print(
                "Enter request vector (" + R + " values): "
            );

            for (int j = 0; j < R; j++) {
                request[j] = sc.nextInt();
            }

            requestResources(pid, request);
        }

        sc.close();
    }
}
