import java.util.*;

class UserSolution
{
	private static final int MAX_LENGTH				= 8;
	HashMap<String, Integer> wordDb = new HashMap<>();
	HashMap<String, List<String>> relateDb = new HashMap<>();
	
	Node root;
	
	void init()
	{
		wordDb.clear();
		relateDb.clear();
		
		root = new Node();
		return;
	}

	void search(char mStr[], int mCount)
	{
		String word = toString(mStr);
		if (!wordDb.containsKey(word)) insert(word);
		wordDb.merge(word, mCount, Integer::sum);
	
		
		List<String> relates = relateDb.get(word);
		if (relates == null || relates.isEmpty()) return;
		for (String relate : relates) {
			wordDb.compute(relate, (k, v) -> v + mCount);
		}
		
		return;
	}

	Solution.Result recommend(char mStr[])
	{
		Solution.Result res = new Solution.Result();
		
		List<String> recommends = new ArrayList<>();
		String word = toString(mStr);
		
		for (int i=0; i <= word.length(); i++) {
			String prefix = word.substring(0, i);
			recommends = search(prefix);
			recommends.sort(
				    Comparator.comparingInt((String x) -> wordDb.get(x))
				              .reversed()
				              .thenComparing(Comparator.naturalOrder()));
			
			int rank = recommends.indexOf(word);
			if (rank < 5) {
				res.mOrder = i;
				res.mRank = rank + 1;
				search(mStr, 1);
				return res;
			}
		}
		return res;
	}

	int relate(char mStr1[], char mStr2[])
	{
		String word1 = toString(mStr1);
		String word2 = toString(mStr2);
		int count = wordDb.get(word1) + wordDb.get(word2);
		wordDb.compute(word1, (k, v) -> count);
		wordDb.compute(word2, (k, v) -> count);
		
		List<String> relates1 = relateDb.getOrDefault(word1, new ArrayList<>());
		List<String> relates2 = relateDb.getOrDefault(word2, new ArrayList<>());
		relates1.addAll(relates2);

		relateDb.compute(word1, (k, v) -> {
		    v = new ArrayList<>(relates1);
		    v.add(word2);
		    return v;
	    });
		relateDb.compute(word2, (k, v) -> {
		    v = new ArrayList<>(relates1);
		    v.add(word1);
		    return v;
	    });

		for (String relate : relates1) {
			wordDb.compute(relate, (k, v) -> count);
			relateDb.compute(relate, (k, v) -> {
			    v = new ArrayList<>(relates1);
			    v.add(word1);
			    v.add(word2);
			    v.remove(relate);
			    return v;
		    });
		}
		return count;
	}

	void rank(char mPrefix[], int mRank, char mReturnStr[])
	{
		String word = toString(mPrefix);
		
		List<String> recommends = search(word);
		recommends.sort(
			    Comparator.comparingInt((String x) -> wordDb.get(x))
			              .reversed()
			              .thenComparing(Comparator.naturalOrder()));		

		String2Char(recommends.get(mRank - 1), mReturnStr, MAX_LENGTH);
		return;
	}
	
	String toString(char mStr[]) {
		int len = 0;
		while(len < mStr.length && mStr[len] != '\0') len++;
		return new String(mStr, 0, len);
	}
	
	private static void String2Char(String s, char[] b, int maxlen)
	{
		int n = s.length();
		for (int i = 0; i < n; ++i)
			b[i] = s.charAt(i);
		for (int i = n; i < maxlen; ++i)
			b[i] = '\0';
	}
	
	void insert(String word) {
		Node node = root;
		
		for (int i=0; i < word.length(); i++) {
			char c = word.charAt(i);
			node.child.putIfAbsent(c, new Node());
			node.recommands.add(word);
			node = node.child.get(c);
		}
		node.recommands.add(word);
		node.isEnd = true;
	}
	
	List<String> search(String word) {
		Node node = root;
		
		for (int i=0; i < word.length(); i++) {
			char c = word.charAt(i);
			node = node.child.get(c);
		}
		return node.recommands;
	}
	
}

class Node {
	HashMap<Character, Node> child;
	List<String> recommands;
	boolean isEnd;
	
	Node() {
		this.child = new HashMap<>();
		this.recommands = new ArrayList<>();
		this.isEnd = false;
	}
}