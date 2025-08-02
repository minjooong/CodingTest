import java.util.*;

class UserSolution
{
    private int N;
    private int maxStamina;
    private int[][] map;
    private int[][] gateId;
    private boolean[] removed;
    private List<List<int[]>> gateGraph;
    private int maxId;
    private int[] dx = {0, 0, -1, 1};
    private int[] dy = {-1, 1, 0, 0};
    
    void init(int N, int mMaxStamina, int mMap[][])
    {
        this.N = N;
        this.maxStamina = mMaxStamina;
        this.map = new int[N][N];
        this.gateId = new int[N][N];
        this.removed = new boolean[201];
        this.gateGraph = new ArrayList<>();
        this.maxId = 0;
        
        for (int i = 0; i <= 200; i++) {
            gateGraph.add(new ArrayList<>());
            removed[i] = false;
        }
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.map[i][j] = mMap[i][j];
                this.gateId[i][j] = 0;
            }
        }
    }

    void addGate(int mGateID, int mRow, int mCol)
    {
        gateId[mRow][mCol] = mGateID;
        maxId = Math.max(maxId, mGateID);
        
        boolean[][] visited = new boolean[N][N];
        Queue<int[]> queue = new ArrayDeque<>();
        
        queue.offer(new int[]{mRow, mCol, 0});
        visited[mRow][mCol] = true;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], cost = current[2];
            
            if (gateId[x][y] != 0) {
                int targetGateId = gateId[x][y];
                if (targetGateId != mGateID) {
                    gateGraph.get(mGateID).add(new int[]{targetGateId, cost});
                    gateGraph.get(targetGateId).add(new int[]{mGateID, cost});
                }
            }
            
            if (cost == maxStamina) continue;
            
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                
                if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
                if (visited[nx][ny] || map[nx][ny] == 1) continue;
                
                visited[nx][ny] = true;
                queue.offer(new int[]{nx, ny, cost + 1});
            }
        }
    }

    void removeGate(int mGateID)
    {
        removed[mGateID] = true;
    }

    int getMinTime(int mStartGateID, int mEndGateID)
    {
        int[] dist = new int[maxId + 1];
        boolean[] inQueue = new boolean[maxId + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(mStartGateID);
        dist[mStartGateID] = 0;
        inQueue[mStartGateID] = true;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;
            
            for (int[] edge : gateGraph.get(u)) {
                int v = edge[0];
                int weight = edge[1];
                
                if (removed[v]) continue;
                
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    if (!inQueue[v]) {
                        queue.offer(v);
                        inQueue[v] = true;
                    }
                }
            }
        }
        
        return dist[mEndGateID] == Integer.MAX_VALUE ? -1 : dist[mEndGateID];
    }
}
