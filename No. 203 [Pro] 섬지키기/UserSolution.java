import java.util.*;

class UserSolution
{
    int N;
    int[][] map = new int[20][20];

    HashMap<String, List<Pair>> diffCache = new HashMap<>();

    class Pair {
        int pos;
        char dir;
        boolean reverse = false;

        Pair(int pos, char dir) {
            this.pos = pos;
            this.dir = dir;
        }
        Pair(int pos, char dir, boolean reverse) {
            this.pos = pos;
            this.dir = dir;
            this.reverse = reverse;
        }

        @Override
        public boolean equals(Object o) {
            Pair p = (Pair) o;
            if (p.pos == this.pos && p.dir == this.dir) return true;
            else return false;
        }

        @Override
        public int hashCode() {
            return 1000 * dir + pos;
        }
    }

	public void init(int N, int mMap[][])
	{
        this.N = N;
        for (int i = 0; i < N; i++) {
            map[i] = Arrays.copyOf(mMap[i], N);
        }
        // for (int i = 0; i < N; i++) {
        //     for (int j = 0; j < N; j++) {
        //         System.out.print(map[i][j] + " ");
        //     }
        //     System.out.println();
        // }
        diffCache.clear();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N-1; j++) {
                int[] diffArr = new int[N-1];
                for (int l = j; l < N-1; l++) {
                    int heightDiff = map[i][l] - map[i][l+1];
                    diffArr[l] = heightDiff;
                    String key = makeKey(Arrays.copyOfRange(diffArr, j, l + 1));
  
                    int pos = i * N + j;
                    diffCache.compute(key, (k,v) -> {
                        if (v == null) v = new ArrayList<Pair>();
                        v.add(new Pair(pos, 'w'));
                        return v;
                    });
                    // System.out.println(key);
                }
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N-1; j++) {
                int[] diffArr = new int[N-1];
                for (int l = j; l < N-1; l++) {
                    int heightDiff = map[l][i] - map[l + 1][i];
                    diffArr[l] = heightDiff;
                    String key = makeKey(Arrays.copyOfRange(diffArr, j, l + 1));
         
                    int pos = j * N + i;
                    diffCache.compute(key, (k,v) -> {
                        if (v == null) v = new ArrayList<Pair>();
                        v.add(new Pair(pos, 'h'));
                        return v;
                    });
                    // System.out.println(key);
                }
            }
        }
    
        // for (Pair pair : diffCache.get(makeKey(new int[] {-1,1}))) {
        //     System.out.println(pair.pos + " " + pair.dir);
        // }
	}

    String makeKey(int[] diffArr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < diffArr.length; i++) {
            sb.append(diffArr[i]);
        }
        return sb.toString();
    }

    HashSet<Pair> set = new HashSet<>();
	public int numberOfCandidate(int M, int mStructure[])
	{
        if (M == 1) {
            // System.out.println(N*N);
            return N * N;
        }

        set.clear();
        int[] reverse = new int[M];
        for (int i = 0; i < M; i++) {
            reverse[i] = mStructure[M-i-1];
        }

        int[] diff1 = new int[M-1];
        int[] diff2 = new int[M-1];
        for (int i = 0; i < M-1; i++) {
            int diff = mStructure[i+1] - mStructure[i];
            diff1[i] = diff;
        }
        for (int i = 0; i < M-1; i++) {
            int diff = reverse[i+1] - reverse[i];
            diff2[i] = diff;
        }

        String key1 = makeKey(diff1);
        String key2 = makeKey(diff2);

        List<Pair> candidate1 = diffCache.get(key1);
        List<Pair> candidate2 = diffCache.get(key2);

        if (candidate1 != null)
            for (Pair pair : candidate1) {
                set.add(new Pair(pair.pos, pair.dir, false));
            }
        if (candidate2 != null)
            for (Pair pair : candidate2) {
                set.add(new Pair(pair.pos, pair.dir, true));
            }
        // System.out.println(set.size());
		return set.size();
	}

	public int maxArea(int M, int mStructure[], int mSeaLevel)
	{
        int count = numberOfCandidate(M, mStructure);
        int maxResult = 0;
        int[][] tmpMap = new int[N + 2][N + 2];

        if (count == 0) return -1;
        if (M == 1) {
            for (int j = 0; j < N + 2; j++) {
                if (j == 0 || j == N + 1) {
                    Arrays.fill(tmpMap[j], 0);
                    continue;
                }
                tmpMap[j][0] = 0;
                for (int k  = 1; k < N + 1; k++) {
                    tmpMap[j][k] = map[j-1][k-1];
                }
                tmpMap[j][N + 1] = 0;
            }

            for (int i = 0; i < count; i++) {
                tmpMap[i / N + 1][(i % N) + 1] += mStructure[0];

                int result = bfs(N, tmpMap, mSeaLevel);
                if (maxResult < result) maxResult = result;

                tmpMap[i / N + 1][(i % N) + 1] -= mStructure[0];
            }
            return maxResult;
        }


        for (Pair pair : set) {
            for (int j = 0; j < N + 2; j++) {
                if (j == 0 || j == N + 1) {
                    Arrays.fill(tmpMap[j], 0);
                    continue;
                }
                tmpMap[j][0] = 0;
                for (int k  = 1; k < N + 1; k++) {
                    tmpMap[j][k] = map[j-1][k-1];
                }
                tmpMap[j][N + 1] = 0;
            }

            if (pair.dir == 'w') {
                for (int i = 0; i < M; i++) {
                    if (!pair.reverse) {
                        tmpMap[(pair.pos / N) + 1][(pair.pos % N) + 1 + i] += mStructure[i];
                    }
                    else {
                        tmpMap[(pair.pos / N) + 1][(pair.pos % N) + 1 + i] += mStructure[M - i - 1];
                    }
                }
            }
            else {
                for (int i = 0; i < M; i++) {
                    if (!pair.reverse) {
                        tmpMap[(pair.pos / N) + 1 + i][(pair.pos % N) + 1] += mStructure[i];
                    }
                    else {
                        tmpMap[(pair.pos / N) + 1 + i][(pair.pos % N) + 1] += mStructure[M - i - 1];
                    }
                }
            }
            // System.out.println(Arrays.toString(mStructure));
            int result = bfs(N, tmpMap, mSeaLevel);
            if (maxResult < result) {
                // System.out.println(mSeaLevel);
                // for (int i = 0; i < N; i++) {
                //     for (int j = 0; j < N; j++) {
                //         System.out.print(map[i][j] + " ");
                //     }
                //     System.out.println();
                // }
                // for (int i = 0; i < N + 2; i++) {
                //     for (int j = 0; j < N + 2; j++) {
                //         System.out.print(tmpMap[i][j] + " ");
                //     }
                //     System.out.println();
                // }
                // System.out.println(pair.reverse);
                maxResult = result;
            }
        }
        // System.out.println(maxResult);
		return maxResult;
	}

    int[] dx = new int[] { 1, -1, 0, 0};
    int[] dy = new int[] { 0, 0, 1, -1};
    public int bfs(int N, int[][] island, int seaLevel) {
        int visitNum = 0;
        boolean[][] visited = new boolean[N + 2][N + 2];
        Queue<Point> queue = new ArrayDeque<>();
        
        queue.offer(new Point(0, 0));
        visited[0][0] = true;
        visitNum++;

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            for (int i = 0; i < 4; i++) {
                int nx = point.x + dx[i];
                int ny = point.y + dy[i];

                if (nx < 0 || nx > N+1 || ny < 0 || ny > N+1) continue;
                if (island[nx][ny] >= seaLevel) continue;
                if (visited[nx][ny]) continue;

                queue.offer(new Point(nx, ny));
                visited[nx][ny] = true;
                visitNum++;
            }
        }
        return (N+2) * (N+2) - visitNum;
    }

    class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}