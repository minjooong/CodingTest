import java.util.*;

class UserSolution {
    class TrieNode {
        TrieNode[] children = new TrieNode[26];
        int count = 0;
    }

    TrieNode root;

    public int insert(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            int idx = ch - 'a';
            if (node.children[idx] == null)
                node.children[idx] = new TrieNode();
            node = node.children[idx];
        }
        node.count++;
        return node.count;
    }

    public int delete(TrieNode r, String word) {
        TrieNode node = r;
        int result = 0;

        for (int i=0; i < word.length(); i++) {

            if (word.charAt(i) == '?') {
                for (TrieNode n : node.children) {
                    if (n == null) continue;
                    result += delete(n, word.substring(i+1, word.length()));
                }
                return result;
            }
            else {
                int idx = word.charAt(i) - 'a';
                if (node.children[idx] == null) return 0;
                node = node.children[idx];
            }
        }
        result += node.count;
        node.count = 0;
        return result;
    }

    public int find(TrieNode r, String word) {
        TrieNode node = r;
        int result = 0;
        for (int i=0; i < word.length(); i++) {

            if (word.charAt(i) == '?') {
                for (TrieNode n : node.children) {
                    if (n == null) continue;
                    result += find(n, word.substring(i+1, word.length()));
                }
                return result;
            }
            else {
                int idx = word.charAt(i) - 'a';
                if (node.children[idx] == null) return 0;
                node = node.children[idx];
            }
        }

        result += node.count;
        return result;
    }

	void init() {
        root = new TrieNode();
		return;
	}

	int add(char str[]) {
        String word = charArrayToString(str);
		return insert(word);
	}

	int remove(char str[]) {
        String word = charArrayToString(str);
		return delete(root, word);
	}

	int search(char str[]) {
        String word = charArrayToString(str);
		return find(root, word);
	}

    private String charArrayToString(char[] arr) {
        int len = 0;
        while (len < arr.length && arr[len] != '\0') {
            len++;
        }
        return new String(arr, 0, len);
    }
}