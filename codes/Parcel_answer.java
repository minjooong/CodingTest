import java.io.*;
import java.util.*;

public class Parcel_answer {
    static class Box {
        int k, h, w, r, c;
        boolean removed;
        Box(int k, int h, int w, int r, int c) {
            this.k = k; this.h = h; this.w = w; this.r = r; this.c = c;
            this.removed = false;
        }
    }

    static int N, M;
    static int[][] A;                 // occupancy grid
    static List<Box> boxes = new ArrayList<>();
    static int[] dx = {1, 0, 0};      // down, left, right
    static int[] dy = {0, -1, 1};

    static boolean inRange(int r, int c) {
        return 0 <= r && r < N && 0 <= c && c < N;
    }

    // leading-edge collision check for one-step move
    static boolean canPut(int h, int w, int r, int c, int d) {
        int r1 = r, r2 = r + h - 1, c1 = c, c2 = c + w - 1;
        if (d == 0) r1 = r + h - 1;       // down
        else if (d == 1) c2 = c;          // left
        else c1 = c + w - 1;              // right

        for (int i = r1; i <= r2; i++) {
            for (int j = c1; j <= c2; j++) {
                if (!inRange(i, j) || A[i][j] != 0) return false;
            }
        }
        return true;
    }

    // push to the end in direction d; return final (r, c)
    static int[] moveBox(int h, int w, int r, int c, int d) {
        int rr = r, cc = c;
        while (true) {
            int nr = rr + dx[d], nc = cc + dy[d];
            if (canPut(h, w, nr, nc, d)) {
                rr = nr; cc = nc;
            } else break;
        }
        return new int[]{rr, cc};
    }

    static void removeBox(Box b) {
        b.removed = true;
        for (int i = b.r; i < b.r + b.h; i++)
            for (int j = b.c; j < b.c + b.w; j++)
                A[i][j] = 0;
    }

    static void putBox(Box b) {
        b.removed = false;
        for (int i = b.r; i < b.r + b.h; i++)
            for (int j = b.c; j < b.c + b.w; j++)
                A[i][j] = b.k;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        A = new int[N][N];

        // initial drop
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int k = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r0 = 0, c0 = c1 - 1;
            int[] res = moveBox(h, w, r0, c0, 0); // down
            Box b = new Box(k, h, w, res[0], res[1]);
            boxes.add(b);
            putBox(b);
        }

        // process: alternate left/right, exactly M removals
        boxes.sort(Comparator.comparingInt(b -> b.k));
        StringBuilder out = new StringBuilder();

        for (int turn = 0; turn < M; turn++) {
            boolean isLeft = (turn % 2 == 0);

            // (1) find removable this turn
            for (Box b : boxes) {
                if (b.removed) continue;

                removeBox(b);
                int[] res = moveBox(b.h, b.w, b.r, b.c, isLeft ? 1 : 2); // left/right
                int rr = res[0], cc = res[1];
                boolean canExit = isLeft ? (cc == 0) : (cc + b.w == N);
                if (canExit) {
                    out.append(b.k).append('\n');   // permanently removed
                    break;
                } else {
                    putBox(b);                      // restore
                }
            }

            // (2) gravity: bottom-first
            boxes.sort((a, b) -> Integer.compare((b.r + b.h), (a.r + a.h)));
            for (Box b : boxes) {
                if (b.removed) continue;
                removeBox(b);
                int[] fall = moveBox(b.h, b.w, b.r, b.c, 0); // down
                b.r = fall[0]; b.c = fall[1];
                putBox(b);
            }

            // back to k-ascending for next pick
            boxes.sort(Comparator.comparingInt(b -> b.k));
        }

        System.out.print(out.toString());
    }
}
