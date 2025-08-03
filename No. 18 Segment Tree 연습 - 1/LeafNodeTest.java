public class LeafNodeTest {
    public static void main(String[] args) {
        int[] arr = {10, 20, 30, 40, 50};
        
        System.out.println("=== AISolution 방식 ===");
        testAIStyle(arr);
        
        System.out.println("\n=== Solution 방식 ===");
        testSolutionStyle(arr);
    }
    
    static void testAIStyle(int[] arr) {
        int n = arr.length;
        int size = 1;
        while (size < n) size <<= 1;
        
        int[] tree = new int[2 * size];
        
        System.out.println("배열 크기: " + n);
        System.out.println("size (2의 거듭제곱): " + size);
        System.out.println("전체 트리 크기: " + (2 * size));
        
        // 리프 노드 위치 출력
        System.out.println("리프 노드 위치:");
        for (int i = 0; i < n; i++) {
            int leafIndex = size + i;
            tree[leafIndex] = arr[i];
            System.out.println("arr[" + i + "] = " + arr[i] + " -> tree[" + leafIndex + "]");
        }
        
        System.out.println("리프 노드들이 연속적으로 위치: " + size + "~" + (size + n - 1));
    }
    
    static void testSolutionStyle(int[] arr) {
        int n = arr.length;
        int[] tree = new int[4 * n];
        
        System.out.println("배열 크기: " + n);
        System.out.println("전체 트리 크기: " + (4 * n));
        
        // 재귀적으로 구성하면서 리프 노드 위치 추적
        System.out.println("리프 노드 위치 (재귀적 구성):");
        buildAndTrack(tree, 1, 0, n - 1, arr);
    }
    
    static void buildAndTrack(int[] tree, int node, int start, int end, int[] arr) {
        if (start == end) {
            tree[node] = arr[start];
            System.out.println("arr[" + start + "] = " + arr[start] + " -> tree[" + node + "]");
        } else {
            int mid = start + (end - start) / 2;
            buildAndTrack(tree, 2 * node, start, mid, arr);
            buildAndTrack(tree, 2 * node + 1, mid + 1, end, arr);
            tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
        }
    }
}
