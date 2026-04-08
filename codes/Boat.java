import java.util.*;

class Boat {
    static int answer;
    public int solution(int[] people, int limit) {
        answer = 0;
        Arrays.sort(people);

        boolean[] isGone = new boolean[people.length];
        
        int index = 0;
        for (int i = people.length - 1; i >= 0; i--) {
            if (isGone[i]) continue;
            int now = people[i];
            int remain = limit - now;
            
            if (people[index] <= remain) {
                remain -= people[index];
                isGone[index] = true;
                index++;
            }
            
            answer++;
        }
        
        return answer;
    }
}