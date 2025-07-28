import java.util.*;
import java.io.FileInputStream;
 
 
class Answer
{
    public static void main(String args[]) throws Exception
    {
		System.setIn(new FileInputStream("input.txt"));
 
        Scanner sc = new Scanner(System.in);
        int T;
        T=sc.nextInt();
 
        UserSolution us = new UserSolution();
        for(int test_case = 1; test_case <= T; test_case++)
        {
            int N = sc.nextInt();
            int[] A = new int[N];
            for(int i = 0; i < N; i++) A[i] = sc.nextInt();
            int K = sc.nextInt();
 
            System.out.println("#" + test_case + " " + us.solve(N, A, K));
        }
    }
}
 
 
class UserSolution {
    int N;
    int[] A;
    int K;
 
    static class State {
        int K;
        int sum;
 
        State(int K, int sum) {
            this.K = K;
            this.sum = sum;
        }
    }
 
 
 
    int solve(int N, int[] A, int K) {
        init(N, A, K);
        return bfs();
    }
 
    private int bfs() {
        // K = K일 때 K = 1까지 줄이는 게 목표입니다
        // 매 state는 {sum, k}를 가져야 함
        // - sum: 해당 state까지 더해진 합
        // - k: 해당 state의 K값
        // 매 state에서 할 수 있는 일
        // - A0 ~ An까지 적용하기
        //   - sum += k % Ai
        //   - K = k // Ai
        // - 우선순위큐에 넣어서 sum이 최소인 걸 매번 뽑고, K = 1이 된다면 마무으리를 지을 건데요
        // - K = 1이 되어도 일단은 우선순위큐에 다시 넣어야 합니다 (K = 1로 만드는 연산이 매우 비싸서 후속 state가 더 빠를 수도 있음)
        // - 아무튼 우선순위큐에서 꺼냈을 때 K == 1이다 -> sum + 1을 return합시다 (이 state일 때 해당 D를 한 번 더 더해줘야 하므로)
        HashMap<Integer, Integer> visited = new HashMap<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(state -> state.sum));
        pq.offer(new State(K, 0));
        visited.put(K, 0);
 
        while (!pq.isEmpty()) {
            State state = pq.poll();
            int k = state.K;
            int sum = state.sum;
            if(k == 0) return sum;
 
            for(int a : A) {
                int nk = k / a;
                int nSum = sum + k % a;
                int visitState = visited.getOrDefault(nk, Integer.MAX_VALUE);
                if(nSum >= visitState) continue;
                visited.put(nk, nSum);
                pq.offer(new State(nk, nSum));
            }
        }
        return -1;
    }
 
    private void init(int N, int[] A, int K) {
        this.N = N;
        this.A = A;
        this.K = K;
    }
}