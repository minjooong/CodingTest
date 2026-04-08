import java.util.*;

public class Shelter {
    static Scanner sc = new Scanner(System.in);
    static int N;
    static int K;
    static int[][] houses;
    static int[][] distance;
    static int[] tmp;
    static int ans;
    
    static void findAnswer(int point, int k) {
        if (k == K) {
            calculateAnswer();
            return;
        }
        
        for (int i = point; i < N; i++) {
            tmp[k] = i;
            findAnswer(i + 1, k + 1);
        }
    }
    
    static void calculateAnswer() {
        int max = 0;
        
        for (int i = 0; i < N; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < K; j++) {
                int a = tmp[j];
                min = Math.min(min, distance[i][a]);
            }
            max = Math.max(max, min);
        }
        ans = Math.min(ans, max);
    }
    
    public static void shelter(String[] args) {
        N = sc.nextInt();
        K = sc.nextInt();
        houses = new int[N][2];
        distance = new int[N][N];
        tmp = new int[K];
        ans = Integer.MAX_VALUE;
        
        for (int i = 0; i < N; i++) {
            int[] now = new int[2];
            now[0] = sc.nextInt();
            now[1] = sc.nextInt();
            
            houses[i] = now;
        }
        
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                int d = Math.abs(houses[i][0] - houses[j][0]) + Math.abs(houses[i][1] - houses[j][1]);
                distance[i][j] = d;
                distance[j][i] = d;
            }
        }
        
        findAnswer(0, 0);
        
        System.out.println(ans);
    }
}