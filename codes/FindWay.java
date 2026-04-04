import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

class Solution {
    public int[][] solution(int[][] nodeinfo) {
                
        PriorityQueue<Node> pq = new PriorityQueue<>(
            (n1, n2) -> {
                if (n1.y - n2.y != 0) {
                    return n2.y - n1.y;
                }
                return n1.x - n2.x;
            });
        
        int size = nodeinfo.length;
        Node root;
        
        for (int i = 0; i < size; i++) {
            Node now = new Node(i + 1, nodeinfo[i][0], nodeinfo[i][1]);
            pq.offer(now);
        }
        
        root = pq.poll();
        for (int j = 0; j < size - 1; j++) {
            Node now = pq.poll();
            
            InsertNode(root, now);
            // System.out.println(now.x + " " + now.y);
        }
        
        // 전위 순회
        List<Integer> PreorderResult = new ArrayList<>();
        Preorder(PreorderResult, root);
        // for (int k = 0; k < size; k++)
        // {
        //     System.out.println(PreorderResult.get(k));
        // }
        
        // 후위 순회
        List<Integer> PostorderResult = new ArrayList<>();
        Postorder(PostorderResult, root);
        
        int[][] answer = new int[2][size];
        for (int i = 0; i < size; i++) {
            answer[0][i] = PreorderResult.get(i);
            answer[1][i] = PostorderResult.get(i);
        }        
        return answer;
    }
    
    void Preorder(List<Integer> PreorderResult, Node node)
    {
        if (node == null) return;
        PreorderResult.add(node.index);
        Preorder(PreorderResult, node.child1);
        Preorder(PreorderResult, node.child2);
    }
    
    void Postorder(List<Integer> PostorderResult, Node node)
    {
        if (node == null) return;
        
        Postorder(PostorderResult, node.child1);
        Postorder(PostorderResult, node.child2);
        PostorderResult.add(node.index);
        
    }
    
    void InsertNode(Node root, Node now)
    {
        if (root.x > now.x)
        {
            if (root.child1 == null)
            {
                root.child1 = now;
                return;
            }
            InsertNode(root.child1, now);
        }
        else
        {
            if (root.child2 == null)
            {
                root.child2 = now;
                return;
            }
            InsertNode(root.child2, now);
        }
    }
    
    class Node {
        int index;
        int x;
        int y;
        
        Node child1;
        Node child2;
        
        Node(int index, int x, int y) {
            this.index = index;
            this.x = x;
            this.y = y;
        }
    }
}