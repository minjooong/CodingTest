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

        public MoveRequest(int idx, int r, int c) {
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
        w.dir = 0;
        w.alive = true;
        w.straightCount = mLength - 1;
        w.pendingPot = 0;

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
                return Integer.compare(b.len, a.len);
            return Integer.compare(b.id, a.id);
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

        for (int i = 0; i < worms.size(); i++) {
            Worm w = worms.get(i);
            if (!w.alive)
                continue;

            if (w.straightCount >= w.len - 1) {
                w.dir = (w.dir + 1) % 4;
                w.straightCount = 0;
            }

            w.straightCount++;

            Point head = w.body.peekFirst();
            int nr = head.r + dr[w.dir];
            int nc = head.c + dc[w.dir];

            moves.add(new MoveRequest(i, nr, nc));

            if (w.pot <= 0) {
                Point tail = w.body.peekLast();
                long key = (long) tail.r * N + tail.c;
                tailsToVacate.add(key);
                tailOwner.put(key, w.id);
            }
        }

        if (moves.isEmpty())
            return;

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
                    int wIdx = moves.get(k).wormIndex;
                    deadWormIndices.add(wIdx);
                }
            } else {
                int occupantID = board[r][c];
                long key = (long) r * N + c;
                boolean willBeVacated = tailsToVacate.contains(key);

                HashSet<Integer> movingWormsIds = new HashSet<>();
                for (int k = idx; k < end; k++) {
                    movingWormsIds.add(worms.get(moves.get(k).wormIndex).id);
                }

                Integer tailOwnerId = tailOwner.get(key);
                boolean oneOfThemOwnsTail = tailOwnerId != null && movingWormsIds.contains(tailOwnerId);

                if (occupantID != 0 && !willBeVacated) {
                    for (int k = idx; k < end; k++) {
                        int wIdx = moves.get(k).wormIndex;
                        deadWormIndices.add(wIdx);
                        Worm victim = wormMap.get(occupantID);
                        if (victim != null) {
                            crashedInto[wIdx] = victim.idx;
                        }
                    }
                } else if (count > 1) {
                    if (willBeVacated && oneOfThemOwnsTail) {
                        for (int k = idx; k < end; k++) {
                            int wIdx = moves.get(k).wormIndex;
                            Worm w = worms.get(wIdx);
                            if (w.id != tailOwnerId) {
                                deadWormIndices.add(wIdx);
                                Worm victim = wormMap.get(tailOwnerId);
                                if (victim != null) {
                                    crashedInto[wIdx] = victim.idx;
                                }
                            }
                        }
                    } else {
                        for (int k = idx; k < end; k++) {
                            int wIdx = moves.get(k).wormIndex;
                            deadWormIndices.add(wIdx);
                        }
                    }
                }
            }
            idx = end;
        }

        for (int wIdx = 0; wIdx < worms.size(); wIdx++) {
            if (!deadWormIndices.contains(wIdx))
                continue;
            if (crashedInto[wIdx] == -1)
                continue;

            int targetIdx = crashedInto[wIdx];

            if (!deadWormIndices.contains(targetIdx)) {
                worms.get(targetIdx).pendingPot += worms.get(wIdx).len;
            }
        }

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
                int nr = w.body.peekFirst().r + dr[w.dir];
                int nc = w.body.peekFirst().c + dc[w.dir];

                w.body.addFirst(new Point(nr, nc));
                board[nr][nc] = w.id;

                if (w.pot > 0) {
                    w.len++;
                    w.pot--;
                } else {
                    Point tail = w.body.removeLast();
                    if (board[tail.r][tail.c] == w.id) {
                        board[tail.r][tail.c] = 0;
                    }
                }

                if (w.pendingPot > 0) {
                    w.pot += w.pendingPot;
                    w.pendingPot = 0;
                }
            }
        }
    }
}
