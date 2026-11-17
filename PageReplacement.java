import java.util.Scanner;

public class PageReplacement {

    static final int MAXFRAMES = 10;
    static final int MAXPAGES = 50;

    static boolean isPresent(int[] frames, int nf, int page) {
        for (int i = 0; i < nf; i++) {
            if (frames[i] == page) {
                return true;
            }
        }
        return false;
    }

    static void fifo(int[] ref, int n, int nf) {

        int[] frames = new int[MAXFRAMES];

        for (int i = 0; i < nf; i++) {
            frames[i] = -1;
        }

        int front = 0;
        int faults = 0;

        for (int i = 0; i < n; i++) {

            if (!isPresent(frames, nf, ref[i])) {

                frames[front] = ref[i];
                front = (front + 1) % nf;
                faults++;
            }
        }

        System.out.println("FIFO Page Faults = " + faults);
    }

    static void lru(int[] ref, int n, int nf) {

        int[] frames = new int[MAXFRAMES];
        int[] lastUsed = new int[MAXFRAMES];

        for (int i = 0; i < nf; i++) {
            frames[i] = -1;
        }

        int filled = 0;
        int faults = 0;

        for (int i = 0; i < n; i++) {

            int pos = -1;

            for (int j = 0; j < nf; j++) {
                if (frames[j] == ref[i]) {
                    pos = j;
                    break;
                }
            }

            if (pos == -1) {

                faults++;

                if (filled < nf) {
                    frames[filled] = ref[i];
                    lastUsed[filled] = i;
                    filled++;

                } else {

                    // Find least recently used frame
                    int lruIdx = 0;

                    for (int j = 1; j < nf; j++) {
                        if (lastUsed[j] < lastUsed[lruIdx]) {
                            lruIdx = j;
                        }
                    }

                    frames[lruIdx] = ref[i];
                    lastUsed[lruIdx] = i;
                }

            } else {
                lastUsed[pos] = i;
            }
        }

        System.out.println("LRU Page Faults = " + faults);
    }

    static void optimal(int[] ref, int n, int nf) {

        int[] frames = new int[MAXFRAMES];

        for (int i = 0; i < nf; i++) {
            frames[i] = -1;
        }

        int filled = 0;
        int faults = 0;

        for (int i = 0; i < n; i++) {

            if (isPresent(frames, filled, ref[i])) {
                continue;
            }

            faults++;

            if (filled < nf) {
                frames[filled++] = ref[i];
                continue;
            }

            // Find the page used farthest in the future
            int replaceIdx = -1;
            int farthest = -1;

            for (int j = 0; j < nf; j++) {

                int k;

                for (k = i + 1; k < n; k++) {
                    if (frames[j] == ref[k]) {
                        break;
                    }
                }

                // Page is never used again
                if (k == n) {
                    replaceIdx = j;
                    break;
                }

                if (k > farthest) {
                    farthest = k;
                    replaceIdx = j;
                }
            }

            frames[replaceIdx] = ref[i];
        }

        System.out.println("Optimal Page Faults = " + faults);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int[] ref = new int[MAXPAGES];

        System.out.print(
            "Enter number of pages in reference string: "
        );

        int n = sc.nextInt();

        System.out.print("Enter the reference string: ");

        for (int i = 0; i < n; i++) {
            ref[i] = sc.nextInt();
        }

        System.out.print("Enter number of frames: ");

        int nf = sc.nextInt();

        fifo(ref, n, nf);
        lru(ref, n, nf);
        optimal(ref, n, nf);

        sc.close();
    }
}
