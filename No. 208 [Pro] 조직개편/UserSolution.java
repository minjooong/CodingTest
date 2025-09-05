import java.util.*;

class UserSolution {
    HashMap<Integer, Node> nodes = new HashMap<>();
    Node root;

    class Node {
        int id, num;
        int sum;
        Node[] child;
        Node parent;
        boolean isRemoved = false;

        Node(int id, int num, int parentId) {
            this.id = id;
            this.num = num;
            this.sum = num;
            this.child = new Node[2];
            if (parentId != 0) this.parent = nodes.get(parentId);
        }
    }
    
    public void init(int mId, int mNum) {
        Node newNode = new Node(mId, mNum, 0);
        nodes.put(mId, newNode);
        root = newNode;
        return;
    }

    public int add(int mId, int mNum, int mParent) {
        Node parent = nodes.get(mParent);
        if (parent.child[0] != null && parent.child[1] != null) return -1;

        Node newNode = new Node(mId, mNum, mParent);
        nodes.put(mId, newNode);

        if (parent.child[0] == null) parent.child[0] = newNode;
        else parent.child[1] = newNode;

        int ret;
        ret = parent.sum += mNum;
        while (parent.parent != null) {
            parent = parent.parent;
            parent.sum += mNum;
        }
 
        return ret;
    }

    public int remove(int mId) {
        Node remNode = nodes.get(mId);
        if (remNode == null || remNode.isRemoved) return -1;
    
        Node parent = remNode.parent;
        if (parent.isRemoved) return -1;
        while (parent.parent != null) {
            parent = parent.parent;
            if (parent.isRemoved) return -1;
        }
        remNode.isRemoved = true;

        parent = remNode.parent;
        for (int i = 0; i < 2; i++) {
            if (parent.child[i] == null) continue;
            if (parent.child[i].id == mId) parent.child[i] = null;
        }

        parent.sum -= remNode.sum;
        while (parent.parent != null) {
            parent = parent.parent;
            parent.sum -= remNode.sum;
        }
        return remNode.sum;
    }

    public int reorganize(int M, int K) {
        return 0;
    }
}