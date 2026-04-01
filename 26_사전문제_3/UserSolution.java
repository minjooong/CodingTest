import java.util.*;

class UserSolution {
    static class Worm {
        int id;
        int idx;
        int len;
        int pot;
        int dir;
        int straightCount;
        boolean alive = true;
        int pendingPot;
        Deque<Point> body = new ArrayDeque<>();
    }

    static class Point {
        int r, c;

        Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static class MoveRequest {
        int wormIndex;
        int r, c;

        MoveRequest(int idx, int r, int c) {
            this.wormIndex = idx;
            this.r = r;
            this.c = c;
        }
    }

    int[][] board;
    int N;
    int currentTime;

    ArrayList<Worm> worms;
    HashMap<Integer, Worm> wormMap;

    static final int[] dr = { -1, 0, 1, 0 };
    static final int[] dc = { 0, 1, 0, -1 };

    public void init(int N) {
        this.N = N;
        this.currentTime = 0;
        this.worms = new ArrayList<>();
        this.wormMap = new HashMap<>();
        this.board = new int[N][N];
    }

    public void join(int mTime, int mID, int mX, int mY, int mLength) {
        simulateTo(mTime);

        Worm w = new Worm();
        w.id = mID;
        w.idx = worms.size();
        w.len = mLength;
        w.pot = 0;
        w.dir = 0; // North
        w.alive = true;
        w.pendingPot = 0;
        w.straightCount = mLength - 1; // Trigger immediate check

        for (int i = 0; i < mLength; i++) {
            int r = mY + i;
            int c = mX;
            w.body.addLast(new Point(r, c));
            board[r][c] = mID;
        }

        worms.add(w);
        wormMap.put(mID, w);
    }

    public Solution.RESULT top5(int mTime) {
        simulateTo(mTime);

        PriorityQueue<Worm> pq = new PriorityQueue<>((a, b) -> {
            if (a.len != b.len)
                return Integer.compare(b.len, a.len); // Descending length
            return Integer.compare(b.id, a.id); // Descending ID
        });

        for (Worm w : worms) {
            if (w.alive) {
                pq.add(w);
            }
        }

        Solution.RESULT res = new Solution.RESULT();
        res.cnt = 0;

        int count = 0;
        while (!pq.isEmpty() && count < 5) {
            Worm w = pq.poll();
            res.IDs[count++] = w.id;
        }
        res.cnt = count;
        return res;
    }

    private void simulateTo(int targetTime) {
        while (currentTime < targetTime) {
            step();
            currentTime++;
        }
    }

    private void step() {
        if (worms.isEmpty())
            return;

        ArrayList<MoveRequest> moves = new ArrayList<>();
        HashMap<Long, Integer> tailOwner = new HashMap<>();
        HashSet<Long> tailsToVacate = new HashSet<>();

        // 1. Determine moves
        for (int i = 0; i < worms.size(); i++) {
            Worm w = worms.get(i);
            if (!w.alive)
                continue;

            // Check rotation using straightCount
            if (w.straightCount >= w.len - 1) {
                w.dir = (w.dir + 1) % 4;
                w.straightCount = 0;
            }
            w.straightCount++;

            Point head = w.body.peekFirst();
            int nr = head.r + dr[w.dir];
            int nc = head.c + dc[w.dir];

            moves.add(new MoveRequest(i, nr, nc));

            // Track tail vacation
            if (w.pot <= 0) {
                Point tail = w.body.peekLast();
                long key = (long) tail.r * N + tail.c;
                tailsToVacate.add(key);
                tailOwner.put(key, w.id);
            }
        }

        if (moves.isEmpty())
            return;

        // Sort moves to group by destination
        Collections.sort(moves, (a, b) -> {
            if (a.r != b.r)
                return Integer.compare(a.r, b.r);
            return Integer.compare(a.c, b.c);
        });

        HashSet<Integer> deadWormIndices = new HashSet<>();
        int[] crashedInto = new int[worms.size()];
        Arrays.fill(crashedInto, -1);

        int idx = 0;
        while (idx < moves.size()) {
            int end = idx;
            while (end < moves.size() &&
                    moves.get(end).r == moves.get(idx).r &&
                    moves.get(end).c == moves.get(idx).c) {
                end++;
            }

            int r = moves.get(idx).r;
            int c = moves.get(idx).c;
            boolean isOOB = (r < 0 || r >= N || c < 0 || c >= N);
            int count = end - idx;

            if (isOOB) {
                for (int k = idx; k < end; k++) {
                    deadWormIndices.add(moves.get(k).wormIndex);
                }
            } else {
                int occupantID = board[r][c];
                long key = (long) r * N + c;
                boolean willBeVacated = tailsToVacate.contains(key);

                // Check collision with existing board state
                if (occupantID != 0 && !willBeVacated) {
                    // Collided with a body part that is NOT moving away
                    for (int k = idx; k < end; k++) {
                        int wIdx = moves.get(k).wormIndex;
                        deadWormIndices.add(wIdx);
                        Worm victim = wormMap.get(occupantID);
                        if (victim != null) {
                            crashedInto[wIdx] = victim.idx;
                        }
                    }
                } else if (count > 1) {
                    // Multiple worms moving to the same cell -> Head collision
                    // Strictly all die
                    for (int k = idx; k < end; k++) {
                        int wIdx = moves.get(k).wormIndex;
                        deadWormIndices.add(wIdx);
                    }
                }
                // else: count == 1 and cell is either empty or being vacated -> Safe move
            }
            idx = end;
        }

        // 2. Calculate Potentials
        for (int wIdx = 0; wIdx < worms.size(); wIdx++) {
            if (!deadWormIndices.contains(wIdx))
                continue;
            if (crashedInto[wIdx] == -1)
                continue;

            int targetIdx = crashedInto[wIdx];
            if (!deadWormIndices.contains(targetIdx)) {
                // Only give pot if target is alive
                worms.get(targetIdx).pendingPot += worms.get(wIdx).len;
            }
        }

        // 3. Apply moves and updates
        for (int i = 0; i < worms.size(); i++) {
            Worm w = worms.get(i);
            if (!w.alive)
                continue;

            if (deadWormIndices.contains(i)) {
                w.alive = false;
                for (Point p : w.body) {
                    if (p.r >= 0 && p.r < N && p.c >= 0 && p.c < N && board[p.r][p.c] == w.id) {
                        board[p.r][p.c] = 0;
                    }
                }
            } else {
                // Move head
                int nr = w.body.peekFirst().r + dr[w.dir];
                int nc = w.body.peekFirst().c + dc[w.dir];
                w.body.addFirst(new Point(nr, nc));
                board[nr][nc] = w.id;

                // Handle tail / growth
                if (w.pot > 0) {
                    w.len++;
                    w.pot--;
                } else {
                    Point tail = w.body.removeLast();
                    if (board[tail.r][tail.c] == w.id) {
                        board[tail.r][tail.c] = 0;
                    }
                }

                // Apply pending pot
                if (w.pendingPot > 0) {
                    w.pot += w.pendingPot;
                    w.pendingPot = 0;
                }
            }
        }
    }
}
