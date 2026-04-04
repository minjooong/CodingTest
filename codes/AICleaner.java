import java.util.*;

public class AICleaner {
    public static void main(String[] args) {
        // Please write your code here.
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt(); // 격자 크기
        int K = sc.nextInt(); // 로봇 청소기 개수
        int L = sc.nextInt(); // 테스트 횟수

        int sum = 0;
        int[][] space = new int[N][N];
        boolean[][] cSpace = new boolean[N][N];
        int[][] cleaner = new int[K][2];

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                space[i][j] = sc.nextInt();
                if (space[i][j] != -1)
                {
                    sum += space[i][j];
                }
            }
        }
// System.out.println(sum);
        for (int k = 0; k < K; k++)
        {
            cleaner[k][0] = sc.nextInt() - 1;
            cleaner[k][1] = sc.nextInt() - 1;

            cSpace[cleaner[k][0]][cleaner[k][1]] = true;
        }


        int[] dx = {0, -1, 0, 1};
        int[] dy = {-1, 0, 1, 0};
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> {
            if (n1.d != n2.d)
            {
                return n1.d - n2.d;
            }
            if (n1.x != n2.x)
            {
                return n1.x - n2.x;
            }
            return n1.y - n2.y;
        });
        boolean[][] visited;

        for (int l = 0; l < L; l++)
        {
            // 1. 청소기 이동
            for (int k = 0; k < K; k++)
            {
                pq.clear();
                visited = new boolean[N][N];

                // 현재 청소기 위치
                int[] start = cleaner[k];

                pq.add(new Node(0, start[0], start[1]));
                visited[start[0]][start[1]] = true;

                while (!pq.isEmpty())
                {
                    Node now = pq.poll();

                    // 먼지 찾았을 때
                    if (space[now.x][now.y] > 0)
                    {
                        cSpace[cleaner[k][0]][cleaner[k][1]] = false;
                        cleaner[k][0] = now.x;
                        cleaner[k][1] = now.y;
                        cSpace[cleaner[k][0]][cleaner[k][1]] = true;
                        break;
                    }

                    // 4방향 탐색
                    for (int i = 0; i < 4; i++)
                    {
                        int nx = now.x + dx[i];
                        int ny = now.y + dy[i];

                        // 정상 경로일 경우
                        if (nx >= 0 && ny >= 0  && nx < N && ny < N && space[nx][ny] != -1 && !cSpace[nx][ny] && !visited[nx][ny])
                        {
                            visited[nx][ny] = true;
                            pq.add(new Node(now.d + 1, nx, ny));
                        }
                    }
                }
            }

            // 2. 청소
            for (int k = 0; k < K; k++)
            {
                int[] now = cleaner[k];

                int min = 101;
                int direct = -1;

                for (int i = 0; i < 4; i++)
                {
                    int nx = now[0] + dx[i];
                    int ny = now[1] + dy[i];

                    if (nx >= 0 && ny >= 0 && nx < N && ny < N)
                    {
                        if (Math.min(space[nx][ny], 20) < min)
                        {
                            direct = i;
                            if (space[nx][ny] != -1)
                                min = Math.min(space[nx][ny], 20);
                            else
                            {
                                min = 0;
                                break;
                            }
                        }
                    }
                    else
                    {
                        direct = i;
                        min = 0;
                        break;
                    }
                }
    
                sum -= Math.min(space[now[0]][now[1]], 20);
                space[now[0]][now[1]] -= Math.min(space[now[0]][now[1]], 20);
                for (int i = 0; i < 4; i++)
                {
                    int nx = now[0] + dx[i];
                    int ny = now[1] + dy[i];
                    
                    if (nx >= 0 && ny >= 0 && nx < N && ny < N && i != direct && space[nx][ny] != -1)
                    {
                        sum -= Math.min(space[nx][ny], 20);
                        space[nx][ny] -= Math.min(space[nx][ny], 20);
                    }
                }
            }

            // 3. 먼지 축적
            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++)
                {
                    if (space[i][j] > 0)
                    {
                        space[i][j] += 5;
                        sum += 5;
                    }
                }
            }

            // 4. 먼지 확산
            int[][] tmp = new int[N][N];

            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++)
                {
                    if (space[i][j] > 0)
                    {
                        for (int k = 0; k < 4; k++)
                        {
                            int nx = i + dx[k];
                            int ny = j + dy[k];

                            if (nx >= 0 && ny >= 0 && nx < N && ny < N  && space[nx][ny] != -1)
                            {
                                tmp[nx][ny] += space[i][j];
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++)
                {
                    if (space[i][j] == 0)
                    {
                        space[i][j] += tmp[i][j] / 10;
                        sum += tmp[i][j] / 10;
                    }
                }
            }

            // 5. 출력
            System.out.println(sum);
        }
    }

}

class Node {
    int d;
    int x;
    int y;
    Node(int d, int x, int y)
    {
        this.d = d;
        this.x = x;
        this.y = y;
    }
}