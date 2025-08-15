import java.util.*;

class UserSolution
{
    int[] city = new int[10000];
    PriorityQueue<Entry> pq = new PriorityQueue<>(
        Comparator.comparingInt((Entry e) -> e.cost)
        .reversed().thenComparing(
        Comparator.comparingInt((Entry e) -> e.index)));

    int N, size;
    int[] roadSumTree = new int[40000];
    int[] roadCount = new int[10000];

	void init(int N, int mPopulation[])
	{
        this.N = N;
        size = 1;
        while (size < N - 1) size <<= 1;

        city = Arrays.copyOf(mPopulation, 10000);
        Arrays.fill(roadCount, 1);
        pq.clear(); // 초기화를 잘 하자.

        for (int i=0; i < N - 1; i++) {
            roadSumTree[size + i] = city[i] + city[i+1];
            pq.offer(new Entry(i, roadSumTree[size + i]));
        }
        for (int i=N-1; i < size; i++) {
            roadSumTree[size + i] = 0;
        }

        for (int i=size-1; i > 0; i--) {
            roadSumTree[i] = roadSumTree[2 * i] + roadSumTree[2 * i + 1];
        }

		return;
	}

	int expand(int M)
	{
        int newCost = 0;
        for (int i=0; i < M; i++) {
            Entry entry = pq.poll();
            roadCount[entry.index] += 1;
            newCost = (city[entry.index] + city[entry.index + 1]) / roadCount[entry.index];
            pq.offer(new Entry(entry.index, newCost));
            update(entry.index, newCost);
        }
		return newCost;
	}

    void update(int index, int newCost) {
        int i = size + index;
        roadSumTree[i] = newCost;
        i /= 2;
        while (i > 0) {
            roadSumTree[i] = roadSumTree[2 * i] + roadSumTree[2 * i + 1];
            i /= 2;
        }
    }
	
	int calculate(int mFrom, int mTo)
	{
        int result = 0;
        if (mFrom > mTo) {
            int tmp = mFrom;
            mFrom = mTo;
            mTo = tmp;
        }
        mFrom += size;
        mTo += size;
        mTo--;

        while (mFrom <= mTo) {
            if ((mFrom & 1) == 1) result += roadSumTree[mFrom++];
            if ((mTo & 1) == 0) result += roadSumTree[mTo--];
            mFrom >>= 1;
            mTo >>= 1;
        }
		return result;
	}
	
    boolean possible(int mFrom, int mTo, int max, int K) {
        int tmpSum = 0;
        int kCount = 1;
        for (int i = mFrom; i <= mTo; i++) {
            if (tmpSum + city[i] > max) {
                tmpSum = 0;
                kCount++;
                if (kCount > K) return false;
            }
            tmpSum += city[i];
        }

        return true;
    }

	int divide(int mFrom, int mTo, int K)
	{
        int left = 1;
        int right = 10000000;

        while (left != right) {
            int max = left + (right - left) / 2;

            if (possible(mFrom, mTo, max, K)) {
                right = max;
            }
            else {
                left = max + 1;
            }
        }
		return right;
	}
}

class Entry {
    int index, cost;

    Entry(int index, int cost) {
        this.index = index;
        this.cost = cost;
    }
}