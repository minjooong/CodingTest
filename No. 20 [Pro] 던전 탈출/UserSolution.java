import java.util.*;

class UserSolution
{
    int N;
    int maxStamina;
    int[][] map = new int[350][350];
    Map<Integer, Map<Integer, Integer>> gates = new HashMap<>(200);

    Queue<int[]> queue = new ArrayDeque<>();
    int[][] visited = new int[350][350];
    Queue<Node> pQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.time));
    Map<Integer, Integer> visitedGate = new HashMap<>();
    int[] dx = {1, -1, 0, 0};
    int[] dy = {0, 0, 1, -1};

    class Node {
        int gateId, time;

        Node(int gateId, int time) {
            this.gateId = gateId;
            this.time = time;
        }
    }

	void init(int N, int mMaxStamina, int mMap[][])
	{
        this.N = N;
        this.maxStamina = mMaxStamina;
        for (int i=0; i < N; i++) map[i] = Arrays.copyOf(mMap[i], N);
        gates.clear();
		return;
	}

	void addGate(int mGateID, int mRow, int mCol)
	{
        int gateId = mGateID + 1;
        map[mRow][mCol] = gateId;
        gates.put(gateId, new HashMap<>());

        queue.clear();
        for (int i=0; i < N; i++) Arrays.fill(visited[i], Integer.MAX_VALUE);

        queue.offer(new int[] {mRow, mCol});
        visited[mRow][mCol] = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentTime = visited[current[0]][current[1]];
            if (currentTime >= maxStamina) continue;
            for (int i=0; i < 4; i++) {
                int nx = current[0] + dx[i];
                int ny = current[1] + dy[i];

                if (map[nx][ny] == 1) continue;
                if (visited[nx][ny] != Integer.MAX_VALUE) continue;

                visited[nx][ny] = currentTime + 1;
                if (gates.containsKey(map[nx][ny])) {
                    gates.get(map[nx][ny]).put(gateId, visited[nx][ny]);
                    gates.get(gateId).put(map[nx][ny], visited[nx][ny]);
                }
                queue.offer(new int[] {nx, ny});
            }
        }
		return;
	}

	void removeGate(int mGateID)
	{
        int gateId = mGateID + 1;
        Map<Integer, Integer> connectedGates = gates.remove(gateId);
        if (connectedGates == null) return;
        for (int connectedGate : connectedGates.keySet()) {
            gates.get(connectedGate).remove(gateId);
        }
		return;
	}

	int getMinTime(int mStartGateID, int mEndGateID)
	{
        int start = mStartGateID + 1;
        int end = mEndGateID + 1;
        pQueue.clear();
        visitedGate.clear();
        pQueue.offer(new Node(start, 0));
        visitedGate.put(start, 0);

        while (!pQueue.isEmpty()) {
            Node current = pQueue.poll();
            int currentTime = current.time;

            if (current.gateId == end) return currentTime;

            Map<Integer, Integer> connectedGates = gates.get(current.gateId);
            if (connectedGates == null) continue;
            for (int id : connectedGates.keySet()) {
                int nextTime = connectedGates.get(id) + currentTime;
                if (visitedGate.get(id) == null || visitedGate.get(id) > nextTime) {
                    visitedGate.put(id, nextTime);
                    pQueue.offer(new Node(id, nextTime));
                }
            }
        }
		return -1;
	}
}