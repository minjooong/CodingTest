import java.util.*;

class UserSolution {
    HashMap<Integer, ArrayList<Integer>> follows;
    HashMap<Integer, ArrayList<Integer>> posts;
    HashMap<Integer, Integer> likes;
    HashMap<Integer, Integer> timestamps;

	public void init(int N)
	{
        follows = new HashMap<Integer, ArrayList<Integer>>(N+1);
        posts = new HashMap<Integer, ArrayList<Integer>>(N+1);
        likes = new HashMap<Integer, Integer>();
        timestamps = new HashMap<Integer, Integer>();

        for (int i = 1; i <= N; i++) {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(i);
            follows.put(i, list);
        }   
	}

	public void follow(int uID1, int uID2, int timestamp)
	{
        ArrayList<Integer> follow = follows.get(uID1);
        if (follow == null) follow = new ArrayList<>();
        follow.add(uID2);
        follows.put(uID1, follow);
	}

	public void makePost(int uID, int pID, int timestamp)
	{
        ArrayList<Integer> post = posts.get(uID);
        if (post == null) post = new ArrayList<>();
        post.add(pID);
        posts.put(uID, post);

        likes.put(pID, 0);
        timestamps.put(pID, timestamp);
	}

	public void like(int pID, int timestamp)
	{
        likes.put(pID, likes.get(pID) + 1);
	}

	public void getFeed(int uID, int timestamp, int pIDList[])
	{
        ArrayList<Integer> follow = follows.get(uID);
        ArrayList<Integer> priorityPost = new ArrayList<>();
        ArrayList<Integer> secondaryPost = new ArrayList<>();

        for (Integer fID : follow) {
            ArrayList<Integer> pIDs = posts.get(fID);
            if (pIDs == null) continue;
            for (Integer pID : pIDs) {
                if (timestamp - timestamps.get(pID) <= 1000) priorityPost.add(pID);
                else secondaryPost.add(pID);
            }
        }

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(
            Comparator
                .comparingInt((Integer id) -> -likes.get(id))
                .thenComparing((Integer id) -> -timestamps.get(id))
        );
        priorityQueue.addAll(priorityPost);

        for (int i=0; i < 10; i++) {
            pIDList[i] = 0;
        }
        int n = priorityPost.size();
        if (n >= 10) {
            for (int i=0; i < 10; i++) {
                pIDList[i] = priorityQueue.poll();
            }
        }
        else {
            PriorityQueue<Integer> secondaryQueue = new PriorityQueue<>(Comparator.comparingInt((Integer id) -> -timestamps.get(id)));
            secondaryQueue.addAll(secondaryPost);

            for (int i=0; i < n; i++) {
                pIDList[i] = priorityQueue.poll();
            }
            for (int i=n; i < 10; i++) {
                if (secondaryQueue.isEmpty()) return;
                pIDList[i] = secondaryQueue.poll();
            }
        }
	}
}
