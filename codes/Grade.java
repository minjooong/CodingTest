import java.util.*;

class Grade {
    static class Node {
        int index;
        List<Integer> win, lose;
        
        Node(int index) {
            this.index = index;
            win = new ArrayList<>();
            lose = new ArrayList<>();
        }
    }
    static Node[] nodes;
    
    public int solution(int n, int[][] results) {
        int answer = 0;
        
        nodes = new Node[n + 1];
        
        for (int i = 0; i < results.length; i++) {
            int a = results[i][0];
            int b = results[i][1];
            
            if (nodes[a] == null) nodes[a] = new Node(a);
            if (nodes[b] == null) nodes[b] = new Node(b);
            
            nodes[a].lose.add(b);
            nodes[b].win.add(a);
        }
        
        
        // for (int i = 1; i <= n; i++) System.out.println(nodes[i].index);
        
        boolean[] isVisited;
        Deque<Node> dq = new ArrayDeque<>();
        
        
        for (int i = 1; i <= n; i++) {
            if (nodes[i] == null) continue;
            
            isVisited = new boolean[n + 1];
            dq.clear();
            
            int winNum = 0;

            dq.offer(nodes[i]);
            isVisited[i] = true;
            
            while (!dq.isEmpty()) {
                Node now = dq.poll();
                
                if (now.win.isEmpty()) continue;
                
                for (int idx : now.win) {
                    if (isVisited[idx]) continue;
                    
                    dq.offer(nodes[idx]);
                    isVisited[idx] = true;
                    winNum++;
                    // System.out.println(i + " - " + now.index + " : " + idx);
                }
            }
            // System.out.println(winNum);
            
            
            isVisited = new boolean[n + 1];
            dq.clear();
            
            int loseNum = 0;
            
            dq.offer(nodes[i]);
            isVisited[i] = true;
            while (!dq.isEmpty()) {
                Node now = dq.poll();
                
                if (now.lose.isEmpty()) continue;

                for (int idx : now.lose) {
                    if (isVisited[idx]) continue;
                    
                    dq.offer(nodes[idx]);
                    isVisited[idx] = true;
                    loseNum++;
                    // System.out.println(i + " - " + now.index + " : " + idx);
                }
            }
            
            if (winNum + loseNum + 1 == n) answer++;
        }
        
        return answer;
    }
}