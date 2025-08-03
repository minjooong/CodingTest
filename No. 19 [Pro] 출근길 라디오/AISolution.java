import java.util.*;

class AISolution
{   
    private int N, M;
    private int[] segmentType;
    private int[] segmentTime; // 도로 시간 정보
    private List<Integer>[] typeToSegments;
    
    @SuppressWarnings("unchecked")
    void init(int N, int M, int mType[], int mTime[])
    {
        this.N = N;
        this.M = M;
        
        segmentType = new int[N-1];
        segmentTime = new int[N-1];
        
        for (int i = 0; i < N-1; i++) {
            segmentType[i] = mType[i];
            segmentTime[i] = mTime[i];
        }
        
        typeToSegments = new List[M];
        for (int i = 0; i < M; i++) {
            typeToSegments[i] = new ArrayList<>();
        }
        
        for (int i = 0; i < N-1; i++) {
            typeToSegments[mType[i]].add(i);
        }
    }

    void destroy()
    {
    }

    void update(int mID, int mNewTime)
    {
        segmentTime[mID] = mNewTime;
    }

    int updateByType(int mTypeID, int mRatio256)
    {
        int totalSum = 0;
        
        for (int segmentId : typeToSegments[mTypeID]) {
            int newTime = (segmentTime[segmentId] * mRatio256) / 256;
            segmentTime[segmentId] = newTime;
            totalSum += newTime;
        }
        
        return totalSum;
    }

    int calculate(int mA, int mB)
    {
        int totalTime = 0;
        
        if (mA < mB) {
            for (int i = mA; i < mB; i++) {
                totalTime += segmentTime[i];
            }
        } else {
            for (int i = mA - 1; i >= mB; i--) {
                totalTime += segmentTime[i];
            }
        }
        
        return totalTime;
    }
}