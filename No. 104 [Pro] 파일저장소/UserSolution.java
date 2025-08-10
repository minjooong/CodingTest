import java.util.*;

class UserSolution {

    TreeMap<Integer, Integer> empty = new TreeMap<>();
    HashMap<Integer, TreeMap<Integer, Integer>> files = new HashMap<>();
    TreeMap<Integer, Integer> start = new TreeMap<>();
    TreeMap<Integer, Integer> end = new TreeMap<>();

	public void init(int N) {
		empty.clear();
        files.clear();
        start.clear();
        end.clear();

        empty.put(1, N);
        return;
	}

	public int add(int mId, int mSize) {
		int rSize = mSize;
        int eSize = 0;
        for (Map.Entry<Integer, Integer> entry : empty.entrySet()) {
            int size = entry.getValue() - entry.getKey() + 1;
            eSize += size;
            if (eSize >= rSize) break;
        }
        if (eSize < rSize) return -1;

        while (rSize > 0) {
            Map.Entry<Integer, Integer> first = empty.firstEntry();
            
            int size = first.getValue() - first.getKey() + 1;
            if (size <= rSize) {
                rSize -= size;
                files.computeIfAbsent(mId, k -> new TreeMap<Integer, Integer>()).put(first.getKey(), first.getValue());
                empty.remove(first.getKey());

                start.put(first.getKey(), mId);
                end.put(first.getValue(), mId);
            }
            else if (size > rSize) {
                int endIndex = first.getKey() + rSize - 1;
                files.computeIfAbsent(mId, k -> new TreeMap<Integer, Integer>()).put(first.getKey(), endIndex);
                empty.remove(first.getKey());
                empty.put(endIndex + 1, first.getValue());
                rSize = 0;

                start.put(first.getKey(), mId);
                end.put(endIndex, mId);
            }
        }
        return files.get(mId).firstKey();
	}

	public int remove(int mId) {
        int pieceNum = files.get(mId).size();
        for (Map.Entry<Integer, Integer> entry : files.get(mId).entrySet()) {
            if (files.get(mId).containsKey(entry.getValue() + 1)) pieceNum--;
        }

        for (Map.Entry<Integer, Integer> entry : files.get(mId).entrySet()) {
            empty.put(entry.getKey(), entry.getValue());

            start.remove(entry.getKey());
            end.remove(entry.getValue());
        }
        files.remove(mId);
		return pieceNum;
	}

	public int count(int mStart, int mEnd) {
        HashSet<Integer> fileIds = new HashSet<>();

        Iterator<Map.Entry<Integer, Integer>> startIt = start.entrySet().iterator();
        Iterator<Map.Entry<Integer, Integer>> endIt = end.entrySet().iterator();

        while (startIt.hasNext()) {
            Map.Entry<Integer, Integer> sEntry = startIt.next();
            Map.Entry<Integer, Integer> eEntry = endIt.next();

            if (!(sEntry.getKey() > mEnd || eEntry.getKey() < mStart)) {
                fileIds.add(sEntry.getValue());
            } 
        }

        return fileIds.size();
	}
}