import java.util.*;

class Network {
    static int answer;
    static boolean[] isVisited;
    
    static void findNet(int index, int[][]computers, int n) {
        // 방문한 적 있으면 return
        if (isVisited[index]) return;
        // 컴퓨터 방문 처리
        isVisited[index] = true;
        
        // 연결되었고, 방문한 적 없는 컴퓨터 리스트 확인
        int[] connectList = computers[index];
        for (int i = 0; i < n; i++) {
            if (connectList[i] == 1 && !isVisited[i]) {
                findSub(i, computers, n);
            }
        }
        
        // 만약 더 이상 없으면 answer++ 및 종료
        answer++;
        System.out.println("+" + index);
    }
    
    static void findSub(int index, int[][] computers, int n) {
        // 방문한 적 있으면 return
        if (isVisited[index]) return;
        // 컴퓨터 방문 처리
        isVisited[index] = true;
        
         // 연결되었고, 방문한 적 없는 컴퓨터 리스트 확인
        int[] connectList = computers[index];
        for (int i = 0; i < n; i++) {
            if (connectList[i] == 1 && !isVisited[i]) {
                findSub(i, computers, n);
            }
        }
    }
    
    public int solution(int n, int[][] computers) {
        answer = 0;
        isVisited = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            findNet(i, computers, n);
        }
        
        return answer;
    }
}