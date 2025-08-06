import java.util.*;

class AISolution {
    int[] minTree, maxTree, lazy;  // 최솟값 트리, 최댓값 트리, lazy 배열
    int C, size;
    long totalSum;
    
    void init(int C) {
        this.C = C;
        size = 1;
        totalSum = 0;
        
        while (size < C) size <<= 1;
        minTree = new int[2 * size];
        maxTree = new int[2 * size];
        lazy = new int[2 * size];
        
        // 초기화: 모든 높이는 0
        Arrays.fill(minTree, 0);
        Arrays.fill(maxTree, 0);
        Arrays.fill(lazy, 0);
    }
    
    // lazy 값을 자식에게 전파
    void push(int node, int start, int end) {
        if (lazy[node] != 0) {
            minTree[node] += lazy[node];
            maxTree[node] += lazy[node];
            if (start != end) {  // 리프 노드가 아닌 경우
                lazy[node * 2] += lazy[node];
                lazy[node * 2 + 1] += lazy[node];
            }
            lazy[node] = 0;
        }
    }
    
    // 구간 [left, right]에 val만큼 더하기
    void updateRange(int node, int start, int end, int left, int right, int val) {
        push(node, start, end);
        
        if (left > end || right < start) return;
        
        if (left <= start && end <= right) {
            lazy[node] += val;
            push(node, start, end);
            return;
        }
        
        int mid = (start + end) / 2;
        updateRange(node * 2, start, mid, left, right, val);
        updateRange(node * 2 + 1, mid + 1, end, left, right, val);
        
        push(node * 2, start, mid);
        push(node * 2 + 1, mid + 1, end);
        minTree[node] = Math.min(minTree[node * 2], minTree[node * 2 + 1]);
        maxTree[node] = Math.max(maxTree[node * 2], maxTree[node * 2 + 1]);
    }
    
    // 구간 [left, right]의 최솟값 쿼리
    int queryMin(int node, int start, int end, int left, int right) {
        if (left > end || right < start) return Integer.MAX_VALUE;
        
        push(node, start, end);
        
        if (left <= start && end <= right) {
            return minTree[node];
        }
        
        int mid = (start + end) / 2;
        int leftMin = queryMin(node * 2, start, mid, left, right);
        int rightMin = queryMin(node * 2 + 1, mid + 1, end, left, right);
        
        return Math.min(leftMin, rightMin);
    }
    
    // 구간 [left, right]의 최댓값 쿼리
    int queryMax(int node, int start, int end, int left, int right) {
        if (left > end || right < start) return Integer.MIN_VALUE;
        
        push(node, start, end);
        
        if (left <= start && end <= right) {
            return maxTree[node];
        }
        
        int mid = (start + end) / 2;
        int leftMax = queryMax(node * 2, start, mid, left, right);
        int rightMax = queryMax(node * 2 + 1, mid + 1, end, left, right);
        
        return Math.max(leftMax, rightMax);
    }

    Solution.Result dropBlocks(int mCol, int mHeight, int mLength) {
        Solution.Result ret = new Solution.Result();
        
        // 구간 [mCol, mCol + mLength - 1]에 mHeight만큼 더하기
        updateRange(1, 0, size - 1, mCol, mCol + mLength - 1, mHeight);
        totalSum += (long) mHeight * mLength;
        
        // 전체 구간의 최솟값과 최댓값 구하기
        int minHeight = queryMin(1, 0, size - 1, 0, C - 1);
        int maxHeight = queryMax(1, 0, size - 1, 0, C - 1);
        
        // 결과 계산
        ret.top = maxHeight - minHeight;
        
        // 전체 합에서 (최솟값 * C)를 빼서 상대적 높이의 합 구하기
        long adjustedSum = totalSum - (long) minHeight * C;
        ret.count = (int) (adjustedSum % 1_000_000L);
        
        return ret;
    }
}