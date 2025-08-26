import java.util.*;

public class UserSolution {

    int N;
    HashSet<Integer> rootNodes = new HashSet<>(1000);
    HashMap<Integer, Node> nodes = new HashMap<>(17000);

    class Node {
        int id, num, sum;
        int parentNode;
        HashSet<Integer> childNodes;
        boolean isRemoved = false;

        Node(int id, int num, int parentNode) {
            this.id = id;
            this.num = num;
            this.sum = num;
            this.parentNode = parentNode;
            childNodes = new HashSet<>(3);
        }
    }
	
    public void init(int N, int mId[], int mNum[]) {
        this.N = N;
        rootNodes.clear();
        nodes.clear();

        for (int i = 0; i < N; i++) {
            Node newNode = new Node(mId[i], mNum[i], 0);
            rootNodes.add(mId[i]);
            nodes.put(mId[i], newNode);
        }

		return;
	}

	public int add(int mId, int mNum, int mParent) {
        Node parentNode = nodes.get(mParent);

        if (parentNode == null) System.out.println(mParent);
        if (parentNode.childNodes.size() >= 3) return -1;
        parentNode.childNodes.add(mId);

        Node newNode = new Node(mId, mNum, mParent);
        nodes.put(mId, newNode);

        int res = parentNode.sum += mNum;
        while (parentNode.parentNode != 0) {
            parentNode = nodes.get(parentNode.parentNode);
            parentNode.sum += mNum;
        }

		return res;
	}

	public int remove(int mId) {
        Node remNode = nodes.get(mId);

        if (remNode == null || remNode.isRemoved) return -1;
        remNode.isRemoved = true;

        int res = remNode.sum;
        nodes.get(remNode.parentNode).childNodes.remove(mId);
        while (remNode.parentNode != 0) {
            remNode = nodes.get(remNode.parentNode);
            if (remNode.isRemoved) return -1;
            remNode.sum -= res;
        }

		return res;
	}

	public int distribute(int K) {
        int totalSum = 0;
        int maxSum = 0;
        for (int id : rootNodes) {
            int sum = nodes.get(id).sum;
            totalSum += sum;
            if (maxSum < sum) maxSum = sum;
        }

        if (totalSum <= K) return maxSum;


		return 0;
	}
}