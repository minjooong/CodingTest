import java.util.*;

class UserSolution {
    int N;
    HashMap<Integer, Tank> tanks = new HashMap<>(20);
    HashMap<Integer, Integer> available = new HashMap<>(20);

    HashMap<String, HashSet<Integer>> hashCount = new HashMap<>();

    class Tank {
        int id;
        int width, height;

        int lengths[];
        int upShapes[];

        int count[];

        Tank(int id, int width, int height, int[] lengths, int[] upShapes) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.lengths = Arrays.copyOf(lengths, width);
            this.upShapes = Arrays.copyOf(upShapes, width);
            this.count = new int[height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < lengths[i]; j++) {
                    count[j]++;
                }
            }
        }
    }


	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {
        this.N = N;
        tanks.clear();
        available.clear();
        hashCount.clear();

        for (int i = 0; i < N; i++) {
            Tank newTank = new Tank(mIDs[i], mWidth, mHeight, mLengths[i], mUpShapes[i]);
            tanks.put(mIDs[i], newTank);
        }
        for (Tank tank : tanks.values()) {
            for (int i = 0; i < tank.width-2; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(tank.upShapes[i]).append(" ");
                sb.append(tank.upShapes[i+1]).append(" ");
                sb.append(tank.upShapes[i+2]);

                if (!hashCount.containsKey(sb.toString())) {
                    hashCount.put(sb.toString(), new HashSet<>());
                }
                hashCount.get(sb.toString()).add(1000*tank.id + i);
            }
        }
	}

    public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
        int count = 0;
        
        StringBuilder sb = new StringBuilder();
        sb.append(mDownShapes[0]).append(" ");
        sb.append(mDownShapes[1]).append(" ");
        sb.append(mDownShapes[2]);
        if (!hashCount.containsKey(sb.toString())) {
            return 0;
        }

        HashSet<Integer> set = hashCount.get(sb.toString());
        for (int x : set) {
            int tankId = x / 1000;
            int index = x % 1000;
            Tank tank = tanks.get(tankId);

            if (tank.lengths[index] + mLengths[0] > tank.height) continue;
            if (tank.lengths[index] + mLengths[0] <= tank.lengths[index+1]) continue;

            if (tank.lengths[index+1] + mLengths[1] > tank.height) continue;
            if (tank.lengths[index+1] + mLengths[1] <= tank.lengths[index]) continue;
            if (tank.lengths[index+1] + mLengths[1] <= tank.lengths[index+2]) continue;

            if (tank.lengths[index+2] + mLengths[2] > tank.height) continue;
            if (tank.lengths[index+2] + mLengths[2] <= tank.lengths[index+1]) continue;

            count++;
        }

		return count;
	}

	public int checkStructuresForAdd(int mLengths[], int mUpShapes[], int mDownShapes[]) {
        int count = 0;
        for (Tank tank : tanks.values()) {
            for (int i = 0; i < tank.width-2; i++) {
                if (tank.upShapes[i] != mDownShapes[0]) continue;
                if (tank.lengths[i] + mLengths[0] > tank.height) continue;
                if (tank.lengths[i] + mLengths[0] <= tank.lengths[i+1]) continue;

                if (tank.upShapes[i+1] != mDownShapes[1]) continue;
                if (tank.lengths[i+1] + mLengths[1] > tank.height) continue;
                if (tank.lengths[i+1] + mLengths[1] <= tank.lengths[i]) continue;
                if (tank.lengths[i+1] + mLengths[1] <= tank.lengths[i+2]) continue;

                if (tank.upShapes[i+2] != mDownShapes[2]) continue;
                if (tank.lengths[i+2] + mLengths[2] > tank.height) continue;
                if (tank.lengths[i+2] + mLengths[2] <= tank.lengths[i+1]) continue;

                count++;
                int index = i;
                available.compute(tank.id, (k, v) -> {
                    if (v == null) return index;
                    if (v > index) return index;
                    return v;
                });
            }
        }

		return count;
	}

	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
        available.clear();
        checkStructuresForAdd(mLengths, mUpShapes, mDownShapes);
        if (available.isEmpty()) return 0;

        Set<Integer> tankIdSet = available.keySet();
        List<Integer> tankIdList = new ArrayList<>(tankIdSet);
        Collections.sort(tankIdList);

        int tankId = tankIdList.get(0);
        int index = available.get(tankId);
        
        Tank tankToAddStructures = tanks.get(tankId);

        for (int i = index - 2; i <= index + 2; i++) {
            if (i < 0 || i+2 > tankToAddStructures.width - 1) continue;
            StringBuilder sb = new StringBuilder();
            sb.append(tankToAddStructures.upShapes[i]).append(" ");
            sb.append(tankToAddStructures.upShapes[i+1]).append(" ");
            sb.append(tankToAddStructures.upShapes[i+2]);
            hashCount.get(sb.toString()).remove(1000 * tankId + i);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = tankToAddStructures.lengths[index + i]; j < tankToAddStructures.lengths[index + i] + mLengths[i]; j++) {
                tankToAddStructures.count[j]++;
            }
            tankToAddStructures.lengths[index + i] += mLengths[i];
            tankToAddStructures.upShapes[index + i] = mUpShapes[i];
        }

        for (int i = index - 2; i <= index + 2; i++) {
            if (i < 0 || i+2 > tankToAddStructures.width - 1) continue;
            StringBuilder sb = new StringBuilder();
            sb.append(tankToAddStructures.upShapes[i]).append(" ");
            sb.append(tankToAddStructures.upShapes[i+1]).append(" ");
            sb.append(tankToAddStructures.upShapes[i+2]);

            if (!hashCount.containsKey(sb.toString())) {
                hashCount.put(sb.toString(), new HashSet<>());
            }
            hashCount.get(sb.toString()).add(1000*tankId + i);
        }

		return tankId * 1000 + index + 1;
	}

	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0;

        for (Tank tank : tanks.values()) {
            int remainWaterCount = mWater;
            int maxHeight = 0;
            for (int i = 0; i < tank.height; i++) {
                int empty = tank.width - tank.count[i];
                if (remainWaterCount < empty) break;
                remainWaterCount -= empty;
                maxHeight ++;
            }

            if (remainWaterCount == mWater) continue;
            if (maxHeight > ret.height) {
                ret.ID = tank.id;
                ret.height = maxHeight;
                ret.used = mWater - remainWaterCount;
            }
            else if (maxHeight == ret.height) {
                if (mWater - remainWaterCount > ret.used) {
                    ret.ID = tank.id;
                    ret.height = maxHeight;
                    ret.used = mWater - remainWaterCount;
                }
                else if (mWater - remainWaterCount == ret.used) {
                    if (tank.id < ret.ID) {
                        ret.ID = tank.id;
                        ret.height = maxHeight;
                        ret.used = mWater - remainWaterCount;
                    }
                }
            }
        }

		return ret;
	}
}