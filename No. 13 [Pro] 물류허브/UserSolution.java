import java.util.*;

class UserSolution {

    int N;
    HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> cost = new HashMap<>();

    HashMap<Integer, ArrayList<Integer>> reverseGraph = new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> reverseCost = new HashMap<>();

    HashMap<Integer, Integer> result;
    HashMap<Integer, Integer> reverseResult;

    PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));

    class Node {
        int node;
        int cost;

        Node(int node, int cost) {
            this.node = node;
            this.cost = cost;
        }
    }

	public int init(int N, int sCity[], int eCity[], int mCost[]) {
        graph.clear();
        cost.clear();
        reverseGraph.clear();
        reverseCost.clear();

        this.N = N;
        for (int i=0; i < N; i++) {
            add(sCity[i], eCity[i], mCost[i]);
        }

		return graph.size();
	}

	public void add(int sCity, int eCity, int mCost) {
        ArrayList<Integer> gList = graph.get(sCity) != null ? graph.get(sCity) : new ArrayList<Integer>();
        ArrayList<Integer> cList = cost.get(sCity) != null ? cost.get(sCity) : new ArrayList<Integer>();

        gList.add(eCity);
        cList.add(mCost);

        graph.put(sCity, gList);
        cost.put(sCity, cList);

        gList = reverseGraph.get(eCity) != null ? reverseGraph.get(eCity) : new ArrayList<Integer>();
        cList = reverseCost.get(eCity) != null ? reverseCost.get(eCity) : new ArrayList<Integer>();
        
        gList.add(sCity);
        cList.add(mCost);

        reverseGraph.put(eCity, gList);
        reverseCost.put(eCity, cList);

		return;
	}

	public int cost(int mHub) {
        result = new HashMap<>(graph.size());
        reverseResult = new HashMap<>(graph.size());
        for (int key : graph.keySet()) {
            result.put(key, Integer.MAX_VALUE);
            reverseResult.put(key, Integer.MAX_VALUE);
        }
        result.put(mHub, 0);
        reverseResult.put(mHub, 0);

        queue.clear();
        queue.offer(new Node(mHub, 0));

        while (!queue.isEmpty()) {
            Node n = queue.poll();
            ArrayList<Integer> nList = graph.get(n.node);
            if (nList == null) continue;
            ArrayList<Integer> cList = cost.get(n.node);
        
            for (int i=0; i < nList.size(); i++) {
                int newNode = nList.get(i);
                int newCost = n.cost + cList.get(i);
                if (newCost < result.get(newNode)) {
                    result.put(newNode, newCost);
                    queue.offer(new Node(newNode, newCost));
                }
            }
        }

        queue.clear();
        queue.offer(new Node(mHub, 0));

        while (!queue.isEmpty()) {
            Node n = queue.poll();

            ArrayList<Integer> nList = reverseGraph.get(n.node);
            if (nList == null) continue;
            ArrayList<Integer> cList = reverseCost.get(n.node);

            for (int i=0; i < nList.size(); i++) {
                int newNode = nList.get(i);
                int newCost = n.cost + cList.get(i);

                if (newCost < reverseResult.get(newNode)) {
                    reverseResult.put(newNode, newCost);
                    queue.offer(new Node(newNode, newCost));
                }
            }
        }

        int ans = 0;
        for (int key : result.keySet()) {
            ans += result.get(key);
            ans += reverseResult.get(key);
        }

		return ans;
	}
}