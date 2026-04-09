import java.util.*;

class FarNode {
    static class Node {
        int index;
        int distance;
        List<Integer> connected;
        
        Node(int index) {
            this.index = index;
            this.distance = Integer.MAX_VALUE;
            this.connected = new ArrayList<>();
        }
    }
    
    static Node[] nodes;
    
    public int solution(int n, int[][] edge) {
        int answer = 0;
        
        nodes = new Node[n + 1];
        
        for (int i = 0; i < edge.length; i++) {
            int a = edge[i][0];
            int b = edge[i][1];
            
            if (nodes[a] == null) {
                Node now = new Node(a);
                nodes[a] = now;
            }
            nodes[a].connected.add(b);
            
            if (nodes[b] == null) {
                Node now = new Node(b);
                nodes[b] = now;
            }
            nodes[b].connected.add(a);
        }
        
        // for (int i = 1; i <= n; i++) System.out.println(nodes[i].index);       
        
        boolean[] isVisited = new boolean[n + 1];
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> n1.distance - n2.distance);
        
        int max = 0;
        
        nodes[1].distance = 0;
        pq.offer(nodes[1]);
        isVisited[1] = true;
        
        while(!pq.isEmpty()) {
            Node now = pq.poll();
            
            for (Integer i : now.connected) {
                if (isVisited[i]) continue;
                
                Node next = nodes[i];
                
                next.distance = now.distance + 1;
                pq.offer(next);
                isVisited[i] = true;
                
                if (next.distance == max) answer++;
                if (next.distance > max) {
                    max = next.distance;
                    answer = 1;
                }
            }
        }
        
        
        // for (int i = 1; i <= n; i++) System.out.println(nodes[i].distance);   
        return answer;
    }
}