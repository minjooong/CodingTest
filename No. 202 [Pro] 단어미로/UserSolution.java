import java.util.*;

class UserSolution {	

	HashMap<String, PriorityQueue<Integer>> front2 = new HashMap<>();
	HashMap<String, PriorityQueue<Integer>> front4 = new HashMap<>();
	HashMap<String, PriorityQueue<Integer>> middle = new HashMap<>();
	HashMap<String, PriorityQueue<Integer>> back2 = new HashMap<>();
	HashMap<String, PriorityQueue<Integer>> back4 = new HashMap<>();

	HashMap<String, Integer> wordIndex = new HashMap<>();
	String[] wordList = new String[30001];
	int[] front = new int[30001];
	int[] back = new int[30001];

	String currentString;

	String ArrToString(char mWord[]) {
		int endIndex = 0;
		for (int i=0; i < mWord.length; i++) {
			if (mWord[i] == '\0') break;
			endIndex++;
		}
		return new String(mWord, 0, endIndex);
	}

	String GetMiddle(String word) {
		return word.substring(4, 7);
	}
	String GetFront2(String word) {
		return word.substring(0, 2);
	}
	String GetFront4(String word) {
		return word.substring(0, 4);
	}
	String GetBack2(String word) {
		return word.substring(9, 11);
	}
	String GetBack4(String word) {
		return word.substring(7, 11);
	}

	void init()
	{
		front2.clear();
		front4.clear();
		middle.clear();
		back2.clear();
		back4.clear();
		wordIndex.clear();
		Arrays.fill(wordList, null);
		Arrays.fill(front, 0);
		Arrays.fill(back, 0);
		currentString = null;
	}

	void addRoom(int mID, char mWord[], int mDirLen[])
	{
		String newWord = ArrToString(mWord);
		wordIndex.put(newWord, mID);
		front[mID] = mDirLen[0];
		back[mID] = mDirLen[2];
		wordList[mID] = newWord;

		front2.computeIfAbsent(GetFront2(newWord), x -> new PriorityQueue<>(Comparator.comparing(index -> wordList[index]))).offer(mID);
		front4.computeIfAbsent(GetFront4(newWord), x -> new PriorityQueue<>(Comparator.comparing(index -> wordList[index]))).offer(mID);
		middle.computeIfAbsent(GetMiddle(newWord), x -> new PriorityQueue<>(Comparator.comparing(index -> wordList[index]))).offer(mID);
		back2.computeIfAbsent(GetBack2(newWord), x -> new PriorityQueue<>(Comparator.comparing(index -> wordList[index]))).offer(mID);
		back4.computeIfAbsent(GetBack4(newWord), x -> new PriorityQueue<>(Comparator.comparing(index -> wordList[index]))).offer(mID);

		// System.out.println(newWord);
		// System.out.println(front2.get(GetFront2(newWord)).peek());
		// System.out.println(back2.get(GetBack2(newWord)).peek());

	}

	void setCurrent(char mWord[])
	{
		currentString = ArrToString(mWord);
	}

	int moveDir(int mDir)
	{
		Integer index = 0;
		int numChar;
		PriorityQueue<Integer> pq = null;
		switch (mDir) {
			case 0:
				numChar = front[wordIndex.get(currentString)];
				if (numChar == 2) {
					String frontString = GetFront2(currentString);
					pq = back2.getOrDefault(frontString, null);
					if (pq != null && !pq.isEmpty()) index = pq.peek();
					if (currentString.equals(wordList[index])) {
						int tmp = pq.poll();
						if (!pq.isEmpty()) index = pq.peek();
						else index = 0;
						pq.offer(tmp);
					}
				}
				else {
					String frontString = GetFront4(currentString);
					pq = back4.getOrDefault(frontString, null);
					if (pq != null && !pq.isEmpty()) index = pq.peek();
					if (currentString.equals(wordList[index])) {
						int tmp = pq.poll();
						if (!pq.isEmpty()) index = pq.peek();
						else index = 0;
						pq.offer(tmp);
					}
				}
				break;
			case 1:
				String middleString = GetMiddle(currentString);
				pq = middle.getOrDefault(middleString, null);
				if (pq != null && !pq.isEmpty()) index = pq.peek();
					if (currentString.equals(wordList[index])) {
						int tmp = pq.poll();
						if (!pq.isEmpty()) index = pq.peek();
						else index = 0;
						pq.offer(tmp);
					}
				break;
			case 2:
			 	numChar = back[wordIndex.get(currentString)];
				if (numChar == 2) {
					String backString = GetBack2(currentString);
					pq = front2.getOrDefault(backString, null);
					if (pq != null && !pq.isEmpty()) index = pq.peek();
					if (currentString.equals(wordList[index])) {
						int tmp = pq.poll();
						if (!pq.isEmpty()) index = pq.peek();
						else index = 0;
						pq.offer(tmp);
					}
				}
				else {
					String backString = GetBack4(currentString);
					pq = front4.getOrDefault(backString, null);
					if (pq != null && !pq.isEmpty()) index = pq.peek();
					if (currentString.equals(wordList[index])) {
						int tmp = pq.poll();
						if (!pq.isEmpty()) index = pq.peek();
						else index = 0;
						pq.offer(tmp);
					}
				}
				break;
		}
		// System.out.print(index + " : " + wordList[index]);
		// System.out.println();
		if (index != 0) currentString = wordList[index];
		return index;
	}

	void changeWord(char mWord[], char mChgWord[], int mChgLen[])
	{
		String oldWord = ArrToString(mWord);
		int index = wordIndex.get(oldWord);
		wordIndex.remove(oldWord);
		front2.get(GetFront2(oldWord)).remove(index);
		front4.get(GetFront4(oldWord)).remove(index);
		middle.get(GetMiddle(oldWord)).remove(index);
		back2.get(GetBack2(oldWord)).remove(index);
		back4.get(GetBack4(oldWord)).remove(index);

		addRoom(index, mChgWord, mChgLen);

		if (currentString.equals(oldWord)) currentString = ArrToString(mChgWord);
	}
}