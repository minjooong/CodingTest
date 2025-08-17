import java.util.*;

class UserSolution {
	
    HashMap<Integer, List<Integer>> alerts = new HashMap<>(500); // UserId, NewsIds
	HashMap<Integer, News> news = new HashMap<>(30000); // NewsId, News
    HashMap<Integer, List<Integer>> channels = new HashMap<>(500); // channel, subscribers

    PriorityQueue<Integer> toSend = new PriorityQueue<>(Comparator.comparingInt((Integer id) -> news.get(id).time).thenComparing(Comparator.naturalOrder()));

    void init(int N, int K)
	{
        alerts.clear();
        news.clear();
        channels.clear();
        toSend.clear();
	}

	void registerUser(int mTime, int mUID, int mNum, int mChannelIDs[])
	{
        send(mTime);

        for (int i = 0; i < mNum; i++) {
            if (!channels.containsKey(mChannelIDs[i])) {
                channels.put(mChannelIDs[i], new ArrayList<Integer>());
            }
            channels.get(mChannelIDs[i]).add(mUID);
        }
	}

	int offerNews(int mTime, int mNewsID, int mDelay, int mChannelID)
	{
        News newNews = new News(mTime + mDelay, mChannelID);
        news.put(mNewsID, newNews);
        toSend.offer(mNewsID);
		return channels.get(mChannelID).size();
	}

    void send(int mTime) {
        while (!toSend.isEmpty()) {
            int newsId = toSend.peek();
            News n = news.get(newsId);

            if (n.time > mTime) {
                return;
            }

            List<Integer> subscribers = channels.get(n.channelId);
            for (int j = 0; j < subscribers.size(); j++) {
                if (!alerts.containsKey(subscribers.get(j))) {
                    alerts.put(subscribers.get(j), new ArrayList<Integer>());
                }
                alerts.get(subscribers.get(j)).add(newsId);
            }
            toSend.poll();
        }
    }
	void cancelNews(int mTime, int mNewsID)
	{
        news.get(mNewsID).isDeleted = true;
	}

	int checkUser(int mTime, int mUID, int mRetIDs[])
	{
        send(mTime);
        List<Integer> alertList = alerts.get(mUID);
        if (alertList ==  null) return 0;

        int count = 0;
        for (int i = alertList.size() - 1; i >= 0; i--) {
            int id = alertList.get(i);
            News n = news.get(id);
            if (n.isDeleted) continue;
            if (count <= 2) {
                mRetIDs[count] = id;
            }
            count++;
        }

        alerts.get(mUID).clear();
		return count;
	}
}

class News {
    int time, channelId;
    boolean isDeleted;

    News(int time, int channelId) {
        this.time = time;
        this.channelId = channelId;
        this.isDeleted = false;
    }
}