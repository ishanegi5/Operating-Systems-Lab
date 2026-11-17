import java.util.Scanner;

public class FileAllocation {

    static final int MAXBLOCKS = 30;
    static final int MAXFILES = 10;

    static int[] disk = new int[MAXBLOCKS];

    // 0 = Free, 1 = Allocated
    static void resetDisk() {
        for (int i = 0; i < MAXBLOCKS; i++) {
            disk[i] = 0;
        }
    }

    static void contiguousAllocation(Scanner sc) {

        int nf;
        resetDisk();

        System.out.println("\n--- Contiguous Allocation ---");
        System.out.print("Enter number of files: ");
        nf = sc.nextInt();

        for (int f = 0; f < nf; f++) {

            System.out.print(
                "File " + (f + 1) + " - starting block, length: "
            );

            int start = sc.nextInt();
            int len = sc.nextInt();

            boolean ok = true;

            if (start < 0 || len <= 0 || start + len > MAXBLOCKS) {
                ok = false;
            } else {
                for (int i = start; i < start + len; i++) {
                    if (disk[i] == 1) {
                        ok = false;
                        break;
                    }
                }
            }

            if (!ok) {
                System.out.println(
                    "Allocation failed: blocks not free/out of range."
                );
                continue;
            }

            for (int i = start; i < start + len; i++) {
                disk[i] = 1;
            }

            System.out.println(
                "File " + (f + 1) + " allocated blocks "
                + start + " to " + (start + len - 1)
            );
        }
    }

    static void linkedAllocation(Scanner sc) {

        int nf;
        resetDisk();

        System.out.println("\n--- Linked Allocation ---");
        System.out.print("Enter number of files: ");
        nf = sc.nextInt();

        for (int f = 0; f < nf; f++) {

            System.out.print(
                "File " + (f + 1) + " - number of blocks: "
            );

            int len = sc.nextInt();

            if (len <= 0) {
                System.out.println("Invalid number of blocks.");
                continue;
            }

            int[] allocated = new int[len];
            int count = 0;

            for (int i = 0; i < MAXBLOCKS && count < len; i++) {

                if (disk[i] == 0) {
                    disk[i] = 1;
                    allocated[count++] = i;
                }
            }

            System.out.print(
                "File " + (f + 1) + " allocated blocks: "
            );

            for (int i = 0; i < count; i++) {
                System.out.print(allocated[i]);

                if (i < count - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println(" -> NULL");

            if (count < len) {
                System.out.println(
                    "Warning: disk full, could not allocate all blocks."
                );
            }
        }
    }

    static void indexedAllocation(Scanner sc) {

        int nf;
        resetDisk();

        System.out.println("\n--- Indexed Allocation ---");
        System.out.print("Enter number of files: ");
        nf = sc.nextInt();

        for (int f = 0; f < nf; f++) {

            System.out.print(
                "File " + (f + 1) + " - number of data blocks needed: "
            );

            int len = sc.nextInt();

            if (len <= 0) {
                System.out.println("Invalid number of data blocks.");
                continue;
            }

            int indexBlock = -1;

            // Find a free index block
            for (int i = 0; i < MAXBLOCKS; i++) {
                if (disk[i] == 0) {
                    indexBlock = i;
                    break;
                }
            }

            if (indexBlock == -1) {
                System.out.println("Allocation failed: disk is full.");
                continue;
            }

            // Collect free data blocks
            int[] dataBlocks = new int[len];
            int count = 0;

            for (int i = 0; i < MAXBLOCKS; i++) {
                if (disk[i] == 0 && i != indexBlock) {
                    dataBlocks[count++] = i;

                    if (count == len) {
                        break;
                    }
                }
            }

            if (count < len) {
                System.out.println(
                    "Allocation failed: not enough free blocks."
                );
                continue;
            }

            // Allocate index block and data blocks
            disk[indexBlock] = 1;

            System.out.print(
                "File " + (f + 1) + " - Index block: "
                + indexBlock + ", Data blocks: "
            );

            for (int i = 0; i < len; i++) {
                disk[dataBlocks[i]] = 1;
                System.out.print(dataBlocks[i] + " ");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice;

        System.out.println("Menu:");
        System.out.println("1. Contiguous Allocation");
        System.out.println("2. Linked Allocation");
        System.out.println("3. Indexed Allocation");

        System.out.print("Enter choice: ");
        choice = sc.nextInt();

        switch (choice) {

            case 1:
                contiguousAllocation(sc);
                break;

            case 2:
                linkedAllocation(sc);
                break;

            case 3:
                indexedAllocation(sc);
                break;

            default:
                System.out.println("Invalid choice");
        }

        sc.close();
    }
}
