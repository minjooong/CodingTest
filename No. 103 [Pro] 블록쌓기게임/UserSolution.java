import java.util.*;

class UserSolution {
    int[] minBoard;
    int C, size, max;
    long count;

    void update(int start, int end, int val) {
        count += (end - start) * val;
        for (int i = size + start; i < size + end; i++) {
            minBoard[i] += val;
            if (minBoard[i] > max) max = minBoard[i];
        }

        int i = size + start;
        int j = size + end - 1;

        i >>= 1;
        j >>= 1;

        while (i > 0) {
            for (int k=i; k <= j; k++) {
                minBoard[k] = Math.min(minBoard[k << 1], minBoard[k << 1 | 1]);
            }
            i >>= 1;
            j >>= 1;
        }
    }
    
    void init(int C) {
        this.C = C;
        size = 1;
        max = 0;
        count = 0;

        while (size < C) size <<= 1;
        minBoard = new int[2 * size];

        Arrays.fill(minBoard, 1, size * 2, Integer.MAX_VALUE);
        for (int i = size; i < size + C; i++) {
            minBoard[i] = 0;
            if (minBoard[i] > max) max = minBoard[i];
        }

        int i = size;
        int j = size + C - 1;

        i >>= 1;
        j >>= 1;

        while (i > 0) {
            for (int k=i; k <= j; k++) {
                minBoard[k] = Math.min(minBoard[k << 1], minBoard[k << 1 | 1]);
            }
            i >>= 1;
            j >>= 1;
        }
    }

    Solution.Result dropBlocks(int mCol, int mHeight, int mLength) {
        Solution.Result ret = new Solution.Result();
        update(mCol, mCol + mLength, mHeight);
        ret.top = max - minBoard[1];
        ret.count = (int)((count - (long)minBoard[1] * C) % 1_000_000L);
        // System.out.print(minBoard[1] + " ");
        // System.out.print(ret.top + " ");
        // System.out.println(ret.count);
        return ret;
    }

}