import java.util.*;

class  Node {
    int id, time;

    Node(int id, int time) {
        this.id = id;
        this.time = time;
    }
}

class UserSolution {
    int N, K;
    Map<Integer, Map<Integer, Integer>> network = new HashMap<>();
    PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.time));
    int[] visitedTime;

// N : 네트워크를 구성하는 소규모 그룹의 개수 ( 3 ≤ N ≤ 300 )
// K : 네트워크를 구성하는 라인의 개수 ( 10 ≤ K ≤ 30,000 )
// mNodeA[] : 라인의 한 쪽의 노드 번호 ( 1 ≤ mNodeA[] ≤ N * 100 + 30 )
// mNodeB[] : 라인의 다른 쪽의 노드 번호 ( 1 ≤ mNodeB[] ≤ N * 100 + 30 )
// mTime[]   : 라인의 전송 시간 ( 30 ≤ mTime[] ≤ 1,000 )
	public void init(int N, int K, int mNodeA[], int mNodeB[], int mTime[])
	{
        network.clear();
        visitedTime = new int[100 * N + 30];

        for (int i=0; i < K; i++) {
            Map<Integer, Integer> connectedNodes = network.get(mNodeA[i]);
            if (connectedNodes == null) connectedNodes = new HashMap<>();
            connectedNodes.put(mNodeB[i], mTime[i]);
            network.put(mNodeA[i], connectedNodes);

            connectedNodes = network.get(mNodeB[i]);
            if (connectedNodes == null) connectedNodes = new HashMap<>();
            connectedNodes.put(mNodeA[i], mTime[i]);
            network.put(mNodeB[i], connectedNodes);
        }
	}

	public void addLine(int mNodeA, int mNodeB, int mTime)
	{
        Map<Integer, Integer> connectedNodes = network.get(mNodeA);
        if (connectedNodes == null) connectedNodes = new HashMap<>();
        connectedNodes.put(mNodeB, mTime);
        network.put(mNodeA, connectedNodes);

        connectedNodes = network.get(mNodeB);
        if (connectedNodes == null) connectedNodes = new HashMap<>();
        connectedNodes.put(mNodeA, mTime);
        network.put(mNodeB, connectedNodes);
	}

	public void removeLine(int mNodeA, int mNodeB)
	{
        Map<Integer, Integer> connectedNodes = network.get(mNodeA);
        if (connectedNodes == null || !connectedNodes.containsKey(mNodeB)) return;
        connectedNodes.remove(mNodeB);
        network.put(mNodeA, connectedNodes);

        connectedNodes = network.get(mNodeB);
        if (connectedNodes == null || !connectedNodes.containsKey(mNodeA)) return;
        connectedNodes.remove(mNodeA);
        network.put(mNodeB, connectedNodes);
	}

	public int checkTime(int mNodeA, int mNodeB)
	{
        pq.clear();
        for (int i=0; i < visitedTime.length; i++) {
            visitedTime[i] = Integer.MAX_VALUE;
        }

        pq.offer(new Node(mNodeA, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            int currentId = current.id;
            int currentTime = current.time;

            if (currentId == mNodeB) return currentTime;

            Map<Integer, Integer> connectedNodes = network.get(currentId);
            for (int nextNodeId : connectedNodes.keySet()) {
                int timeToNext = connectedNodes.get(nextNodeId);
                
                int nextNodeTime = currentTime + timeToNext;
                if (nextNodeTime < visitedTime[nextNodeId]) {
                    visitedTime[nextNodeId] = nextNodeTime;
                    pq.offer(new Node(nextNodeId, nextNodeTime));
                }

            }
        }

		return 0;
	}
}