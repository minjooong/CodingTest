import java.util.*;

class UserSolution {
    int[] minTree, maxTree, lazy;
    int C, size;
    long totalSum;

    void push(int node, int start, int end) {
        if (lazy[node] == 0) return;

        minTree[node] += lazy[node];
        maxTree[node] += lazy[node];

        if (start != end) {
            lazy[2 * node] += lazy[node];
            lazy[2 * node + 1] += lazy[node];
        }

        lazy[node] = 0;
    }

    void update(int node, int start, int end, int left, int right, int val) {
        push(node, start, end);

        if (right < start || end < left) return;
        if (left <= start && end <= right) {
            lazy[node] += val;
            push(node, start, end);
            return;
        }

        int mid = start + (end - start)/2;
        update(2 * node, start, mid, left, right, val);
        update(2 * node + 1, mid + 1, end, left, right, val);
        minTree[node] = Math.min(minTree[2 * node], minTree[2 * node + 1]);
        maxTree[node] = Math.max(maxTree[2 * node], maxTree[2 * node + 1]);
    }

    int queryMin(int node, int start, int end, int left, int right) {
        push(node, start, end);

        if (right < start || end < left) return Integer.MAX_VALUE;
        if (left <= start && end <= right) {
            return minTree[node];
        }

        int mid = start + (end - start)/2;
        int leftMin = queryMin(2 * node, start, mid, left, right);
        int rightMin = queryMin(2 * node + 1, mid + 1, end, left, right);
        return Math.min(leftMin, rightMin);
    }

    int queryMax(int node, int start, int end, int left, int right) {
        push(node, start, end);

        if (right < start || end < left) return Integer.MIN_VALUE;
        if (left <= start && end <= right) {
            return maxTree[node];
        }

        int mid = start + (end - start)/2;
        int leftMax = queryMax(2 * node, start, mid, left, right);
        int rightMax = queryMax(2 * node + 1, mid + 1, end, left, right);
        return Math.max(leftMax, rightMax);
    }
    
    void init(int C) {
        this.C = C;
        size = 1;
        totalSum = 0;

        while (size < C) size <<= 1;
        minTree = new int[2 * size];
        maxTree = new int[2 * size];
        lazy = new int[2 * size];
    }

    Solution.Result dropBlocks(int mCol, int mHeight, int mLength) {
        Solution.Result ret = new Solution.Result();
        update(1, 0, size-1, mCol, mCol + mLength-1, mHeight);
        
        totalSum += (long) mHeight * mLength;
        
        int minHeight = queryMin(1, 0, size - 1, 0, C - 1);
        int maxHeight = queryMax(1, 0, size - 1, 0, C - 1);
        
        ret.top = maxHeight - minHeight;
        ret.count = (int) ((totalSum - (long) minHeight * C) % 1_000_000L);
        return ret;
    }

}