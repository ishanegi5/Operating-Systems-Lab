import java.util.Scanner;

public class MemoryAllocation {

    static final int MAX = 20;

    static void firstFit(int[] bsize, int bn, int[] psize, int pn) {

        int[] allocation = new int[MAX];
        int[] block = bsize.clone();

        for (int i = 0; i < pn; i++) {
            allocation[i] = -1;
        }

        for (int i = 0; i < pn; i++) {
            for (int j = 0; j < bn; j++) {

                if (block[j] >= psize[i]) {
                    allocation[i] = j;
                    block[j] -= psize[i];
                    break;
                }
            }
        }

        System.out.println("\n--- First Fit ---");
        System.out.println("Process No.\tProcess Size\tBlock No.");

        for (int i = 0; i < pn; i++) {
            System.out.print("P" + (i + 1) + "\t\t" + psize[i] + "\t\t");

            if (allocation[i] != -1) {
                System.out.println("B" + (allocation[i] + 1));
            } else {
                System.out.println("Not Allocated");
            }
        }
    }

    static void bestFit(int[] bsize, int bn, int[] psize, int pn) {

        int[] allocation = new int[MAX];
        int[] block = bsize.clone();

        for (int i = 0; i < pn; i++) {
            allocation[i] = -1;
        }

        for (int i = 0; i < pn; i++) {

            int bestIdx = -1;

            for (int j = 0; j < bn; j++) {

                if (block[j] >= psize[i]) {

                    if (bestIdx == -1 || block[j] < block[bestIdx]) {
                        bestIdx = j;
                    }
                }
            }

            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                block[bestIdx] -= psize[i];
            }
        }

        System.out.println("\n--- Best Fit ---");
        System.out.println("Process No.\tProcess Size\tBlock No.");

        for (int i = 0; i < pn; i++) {
            System.out.print("P" + (i + 1) + "\t\t" + psize[i] + "\t\t");

            if (allocation[i] != -1) {
                System.out.println("B" + (allocation[i] + 1));
            } else {
                System.out.println("Not Allocated");
            }
        }
    }

    static void worstFit(int[] bsize, int bn, int[] psize, int pn) {

        int[] allocation = new int[MAX];
        int[] block = bsize.clone();

        for (int i = 0; i < pn; i++) {
            allocation[i] = -1;
        }

        for (int i = 0; i < pn; i++) {

            int worstIdx = -1;

            for (int j = 0; j < bn; j++) {

                if (block[j] >= psize[i]) {

                    if (worstIdx == -1 || block[j] > block[worstIdx]) {
                        worstIdx = j;
                    }
                }
            }

            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                block[worstIdx] -= psize[i];
            }
        }

        System.out.println("\n--- Worst Fit ---");
        System.out.println("Process No.\tProcess Size\tBlock No.");

        for (int i = 0; i < pn; i++) {
            System.out.print("P" + (i + 1) + "\t\t" + psize[i] + "\t\t");

            if (allocation[i] != -1) {
                System.out.println("B" + (allocation[i] + 1));
            } else {
                System.out.println("Not Allocated");
            }
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int bn, pn;

        int[] bsize = new int[MAX];
        int[] psize = new int[MAX];

        System.out.print("Enter number of memory blocks: ");
        bn = sc.nextInt();

        System.out.print("Enter sizes of blocks: ");

        for (int i = 0; i < bn; i++) {
            bsize[i] = sc.nextInt();
        }

        System.out.print("Enter number of processes: ");
        pn = sc.nextInt();

        System.out.print("Enter sizes of processes: ");

        for (int i = 0; i < pn; i++) {
            psize[i] = sc.nextInt();
        }

        firstFit(bsize, bn, psize, pn);
        bestFit(bsize, bn, psize, pn);
        worstFit(bsize, bn, psize, pn);

        sc.close();
    }
}
