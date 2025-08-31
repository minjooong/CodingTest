import java.util.*;

class UserSolution {
    int N;
    HashMap<Integer, Tank> tanks = new HashMap<>(20);
    HashMap<Integer, Integer> available = new HashMap<>(20);

    class Tank {
        int id;
        int width, height;

        int lengths[];
        int upShapes[];

        Tank(int id, int width, int height, int[] lengths, int[] upShapes) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.lengths = Arrays.copyOf(lengths, width);
            this.upShapes = Arrays.copyOf(upShapes, width);
        }
    }


	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {
        this.N = N;
        tanks.clear();
        available.clear();

        for (int i = 0; i < N; i++) {
            Tank newTank = new Tank(mIDs[i], mWidth, mHeight, mLengths[i], mUpShapes[i]);
            tanks.put(mIDs[i], newTank);
        }

	}

	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

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
        checkStructures(mLengths, mUpShapes, mDownShapes);
        if (available.isEmpty()) return 0;

        Set<Integer> tankIdSet = available.keySet();
        List<Integer> tankIdList = new ArrayList<>(tankIdSet);
        Collections.sort(tankIdList);

        int tankId = tankIdList.get(0);
        int index = available.get(tankId);

        Tank tankToAddStructures = tanks.get(tankId);
        for (int i = 0; i < 3; i++) {
            tankToAddStructures.lengths[index + i] += mLengths[i];
            tankToAddStructures.upShapes[index + i] = mUpShapes[i];
        }

		return tankId * 1000 + index + 1;
	}

	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0;


		return ret;
	}
}