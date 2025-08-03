import java.util.*;

class UserSolution
{
	int[] segmentTree; // 소요 시간
	Map<Integer, List<Integer>> types;
	int size;

	void init(int N, int M, int mType[], int mTime[])
	{
		size = 1;
		while (size < N-1) size <<= 1;
		segmentTree = new int[2 * size];
		types = new HashMap<Integer, List<Integer>>(M);

		for (int i=0; i < N-1; i++) {
			segmentTree[size + i] = mTime[i];
			types.computeIfAbsent(mType[i], k -> new ArrayList<>()).add(i);
		}

		for (int i = size - 1; i > 0; i--) {
			segmentTree[i] = segmentTree[i << 1] + segmentTree[i << 1 | 1];
		}
	}

	void destroy()
	{

	}

	void update(int mID, int mNewTime)
	{
		int i = size + mID;
		segmentTree[i] = mNewTime;
		i >>= 1;
		while (i > 0) {
			segmentTree[i] = segmentTree[i << 1] + segmentTree[i << 1 | 1];
			i >>= 1;
		}
	}

	int updateByType(int mTypeID, int mRatio256)
	{
		int sum = 0;
		List<Integer> roadsByType = types.get(mTypeID);
		if (roadsByType == null) return 0;
		for (int roadId : roadsByType) {
			int i = size + roadId;
			segmentTree[i] *= (double)mRatio256/256;
			sum += segmentTree[i];
			i >>= 1;
			while (i > 0) {
				segmentTree[i] = segmentTree[i << 1] + segmentTree[i << 1 | 1];
				i >>= 1;
			}
		}
		return sum;
	}

	int calculate(int mA, int mB)
	{
		int sum = 0;
		if (mA > mB) {
			int tmp = mA;
			mA = mB;
			mB = tmp;
		}
		mA += size;
		mB += size - 1;
		while (mA <= mB) {
			if ((mA & 1) == 1) sum += segmentTree[mA++];
			if ((mB & 1) == 0) sum += segmentTree[mB--];
			mA >>= 1;
			mB >>= 1;
		}
		return sum;
	}
}