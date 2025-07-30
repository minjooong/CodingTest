import java.util.*;

class UserSolution
{
    Deque<Character> S = new ArrayDeque<>();
    boolean isReversed;
    HashMap<String, Integer> cnt = new HashMap<>();

    // char 배열을 String으로 변환하는 헬퍼 메소드
    private String charArrayToString(char[] arr) {
        int len = 0;
        while (len < arr.length && arr[len] != '\0') {
            len++;
        }
        return new String(arr, 0, len);
    }

    // Deque의 끝에서부터 len개 문자를 가져와서 문자열 생성 (정방향용)
    private String getLastSubstring(int len) {
        if (len > S.size()) return "";
        
        StringBuilder sb = new StringBuilder();
        Iterator<Character> iter = S.descendingIterator();
        for (int i = 0; i < len && iter.hasNext(); i++) {
            sb.insert(0, iter.next());
        }
        return sb.toString();
    }

    // Deque의 앞에서부터 len개 문자를 가져와서 문자열 생성 (역방향용)
    private String getFirstSubstring(int len) {
        if (len > S.size()) return "";
        
        StringBuilder sb = new StringBuilder();
        Iterator<Character> iter = S.iterator();
        for (int i = 0; i < len && iter.hasNext(); i++) {
            sb.append(iter.next());
        }
        return sb.toString();
    }

    void init(char mStr[])
    {
        S.clear();
        cnt.clear();
        isReversed = false;
        
        String str = charArrayToString(mStr);
        int N = str.length();
        
        for (char c : str.toCharArray()) {
            S.addLast(c);
        }

        // 모든 부분 문자열(길이 1~4) 카운트 계산
        for (int i = 0; i < N; i++) {
            for (int L = 1; L <= 4 && i + L <= N; L++) {
                String sub = str.substring(i, i + L);
                cnt.put(sub, cnt.getOrDefault(sub, 0) + 1);
            }
        }
    }
    
    void appendWord(char mWord[])
    {
        String word = charArrayToString(mWord);
        int lenw = word.length();
        
        if (!isReversed) {
            // 정방향: 끝에 추가
            for (int idx = 0; idx < lenw; idx++) {
                S.addLast(word.charAt(idx));
                
                // 새로 추가된 문자로 인해 생성되는 모든 부분 문자열 카운트 증가
                for (int L = 1; L <= 4 && L <= S.size(); L++) {
                    String sub = getLastSubstring(L);
                    cnt.put(sub, cnt.getOrDefault(sub, 0) + 1);
                }
            }
        } else {
            // 역방향: 앞에 추가
            for (int idx = 0; idx < lenw; idx++) {
                S.addFirst(word.charAt(idx));
                
                // 새로 추가된 문자로 인해 생성되는 모든 부분 문자열 카운트 증가
                for (int L = 1; L <= 4 && L <= S.size(); L++) {
                    String sub = getFirstSubstring(L);
                    cnt.put(sub, cnt.getOrDefault(sub, 0) + 1);
                }
            }
        }
    }

    void cut(int k)
    {
        if (!isReversed) {
            // 정방향: 끝에서부터 제거
            for (int t = 0; t < k; t++) {
                // 제거될 문자로 인해 사라지는 모든 부분 문자열 카운트 감소
                for (int L = 1; L <= 4 && L <= S.size(); L++) {
                    String sub = getLastSubstring(L);
                    int count = cnt.getOrDefault(sub, 0);
                    if (count <= 1) {
                        cnt.remove(sub);
                    } else {
                        cnt.put(sub, count - 1);
                    }
                }
                S.removeLast();
            }
        } else {
            // 역방향: 앞에서부터 제거
            for (int t = 0; t < k; t++) {
                // 제거될 문자로 인해 사라지는 모든 부분 문자열 카운트 감소
                for (int L = 1; L <= 4 && L <= S.size(); L++) {
                    String sub = getFirstSubstring(L);
                    int count = cnt.getOrDefault(sub, 0);
                    if (count <= 1) {
                        cnt.remove(sub);
                    } else {
                        cnt.put(sub, count - 1);
                    }
                }
                S.removeFirst();
            }
        }
    }

    void reverse()
    {
        isReversed = !isReversed;
    }

    int countOccurrence(char mWord[])
    {
        String word = charArrayToString(mWord);
        
        // 역방향일 때는 찾고자 하는 단어를 뒤집어서 검색
        if (isReversed) {
            word = new StringBuilder(word).reverse().toString();
        }
        
        return cnt.getOrDefault(word, 0);
    }
}
