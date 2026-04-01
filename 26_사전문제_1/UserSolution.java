class UserSolution {

    private int[][] grid = new int[8][8]; // [y][x]. y=0 is bottom.
    private int[][] mTiles;
    private int[] colHead = new int[8];
    private int N_TILES;

    private static final int EMPTY = 0;
    private boolean[][] toRemove = new boolean[8][8];

    void init(int N, int mTiles[][]) {
        this.N_TILES = N;
        this.mTiles = mTiles;
        for (int i = 0; i < 8; i++) {
            colHead[i] = 0;
        }
        // Reset grid
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                grid[y][x] = EMPTY;
            }
        }
    }

    int[] takeTurn() {
        // Phase 1: Prepare Grid
        while (true) {
            boolean changed = false;

            // 1-a. Fill holes
            if (fillGrid()) {
                changed = true;
            }

            // 1-b. Remove matches (no score, but MUST REMOVE)
            if (removeMatches(true) > 0) {
                changed = true;
                continue; // If we removed, we need to fill again immediately
            }

            // 1-c. Check for possible moves
            if (!hasPossibleSwap()) {
                clearGrid();
                changed = true;
                continue; // Removed all, so next loop will fill
            }

            if (!changed)
                break;
        }

        // Phase 2: Select Best Move
        int bestScore = -1;
        int bestY = -1, bestX = -1;
        int bestAdjY = -1, bestAdjX = -1;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                // Try Swap Right
                if (x + 1 < 8) {
                    int s = simulateSwap(y, x, y, x + 1);
                    if (s > bestScore) {
                        bestScore = s;
                        bestY = y;
                        bestX = x;
                        bestAdjY = y;
                        bestAdjX = x + 1;
                    }
                }
                // Try Swap Up
                if (y + 1 < 8) {
                    int s = simulateSwap(y, x, y + 1, x);
                    if (s > bestScore) {
                        bestScore = s;
                        bestY = y;
                        bestX = x;
                        bestAdjY = y + 1;
                        bestAdjX = x;
                    }
                }
            }
        }

        // Phase 3: Execute and Cascade
        int totalScore = 0;
        if (bestScore > 0) {
            // Apply swap
            int temp = grid[bestY][bestX];
            grid[bestY][bestX] = grid[bestAdjY][bestAdjX];
            grid[bestAdjY][bestAdjX] = temp;

            // Cycle of remove -> fill
            while (true) {
                int s = removeMatches(true);
                if (s == 0)
                    break;
                totalScore += s;
                fillGrid();
            }
        }

        return new int[] { totalScore, bestY, bestX, bestAdjY, bestAdjX };
    }

    // Returns true if any tile was added/moved
    private boolean fillGrid() {
        boolean changed = false;
        for (int x = 0; x < 8; x++) {
            // 1. Shift down existing tiles in-place
            int writeIdx = 0;
            boolean shiftNeeded = false;
            for (int y = 0; y < 8; y++) {
                if (grid[y][x] != EMPTY) {
                    if (y != writeIdx) {
                        grid[writeIdx][x] = grid[y][x]; // Move to writeIdx
                        shiftNeeded = true;
                    }
                    writeIdx++;
                }
            }

            if (shiftNeeded) {
                for (int k = writeIdx; k < 8; k++)
                    grid[k][x] = EMPTY;
                changed = true;
            }

            // 2. Fill remaining from reserve
            while (writeIdx < 8) {
                if (colHead[x] < N_TILES) {
                    grid[writeIdx++][x] = mTiles[colHead[x]++][x];
                    changed = true;
                } else {
                    if (grid[writeIdx][x] != EMPTY) {
                        grid[writeIdx][x] = EMPTY;
                        changed = true;
                    }
                    writeIdx++;
                }
            }
        }
        return changed;
    }

    private void clearGrid() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                grid[y][x] = EMPTY;
            }
        }
    }

    // Returns score gained. If doRemove is true, sets matched tiles to EMPTY.
    private int removeMatches(boolean doRemove) {
        if (doRemove) {
            for (int y = 0; y < 8; y++)
                for (int x = 0; x < 8; x++)
                    toRemove[y][x] = false;
        }

        int score = 0;
        boolean foundAny = false;

        // Horizontal runs
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 6; x++) {
                int type = grid[y][x];
                if (type == EMPTY)
                    continue;
                int count = 1;
                while (x + count < 8 && grid[y][x + count] == type) {
                    count++;
                }
                if (count >= 3) {
                    if (count == 3)
                        score += 1;
                    else if (count == 4)
                        score += 4;
                    else
                        score += 9;

                    if (doRemove) {
                        for (int k = 0; k < count; k++) {
                            toRemove[y][x + k] = true;
                        }
                    }
                    foundAny = true;
                    x += count - 1;
                }
            }
        }

        // Vertical runs
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 6; y++) {
                int type = grid[y][x];
                if (type == EMPTY)
                    continue;
                int count = 1;
                while (y + count < 8 && grid[y + count][x] == type) {
                    count++;
                }
                if (count >= 3) {
                    if (count == 3)
                        score += 1;
                    else if (count == 4)
                        score += 4;
                    else
                        score += 9;

                    if (doRemove) {
                        for (int k = 0; k < count; k++) {
                            toRemove[y + k][x] = true;
                        }
                    }
                    foundAny = true;
                    y += count - 1;
                }
            }
        }

        if (doRemove && foundAny) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    if (toRemove[y][x]) {
                        grid[y][x] = EMPTY;
                    }
                }
            }
        }

        return foundAny ? score : 0;
    }

    private boolean hasPossibleSwap() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                // Right
                if (x + 1 < 8) {
                    if (simulateSwap(y, x, y, x + 1) > 0)
                        return true;
                }
                // Up
                if (y + 1 < 8) {
                    if (simulateSwap(y, x, y + 1, x) > 0)
                        return true;
                }
            }
        }
        return false;
    }

    // Simulates swap and returning score. Does NOT modify grid permanently.
    private int simulateSwap(int y1, int x1, int y2, int x2) {
        if (grid[y1][x1] == grid[y2][x2])
            return 0;

        // BACKUP
        int t1 = grid[y1][x1];
        int t2 = grid[y2][x2];
        grid[y1][x1] = t2;
        grid[y2][x2] = t1;

        // Calculate
        int score = removeMatches(false);

        // RESTORE
        grid[y1][x1] = t1;
        grid[y2][x2] = t2;

        return score;
    }
}
