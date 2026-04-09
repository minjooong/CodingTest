import java.util.*;

class GameMap {
    static class Node {
        int x, y;
        int d;
        
        Node(int x, int y, int d) {
            this.x = x; this.y = y;
            this.d = d;
        }
    }
    static int[] dx = new int[] { 1, -1, 0, 0};
    static int[] dy = new int[] { 0, 0, 1, -1};
    
    public int solution(int[][] maps) {
        int answer = -1;
        
        boolean[][] isVisited = new boolean[maps.length][maps[0].length];
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> n1.d - n2.d);
        
        pq.offer(new Node(0, 0, 1));
        isVisited[0][0] = true;
        
        while (!pq.isEmpty()) {
            Node now = pq.poll();
            
            if (now.x == maps.length - 1 && now.y == maps[0].length - 1) {
                answer = now.d;
                break;
            }
            
            for (int i = 0; i < 4; i++) {
                int nx = now.x + dx[i];
                int ny = now.y + dy[i];
                
                if (nx < 0 || nx >= maps.length || ny < 0 || ny >= maps[0].length) continue;
                if (maps[nx][ny] == 1 && !isVisited[nx][ny]) {
                    pq.offer(new Node(nx, ny, now.d + 1));
                    isVisited[nx][ny] = true;
                }
            }
        }
        
        return answer;
    }
}