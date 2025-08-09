import java.util.*;

class UserSolution {

    int[] wall = new int[100_000];
    int rear;

    Map<String, Integer> hash = new HashMap<>();

    void init() {
        Arrays.fill(wall, 0);
        rear = 0;

        hash.clear();
    }

    void makeWall(int mHeights[]) {
        for (int i=0; i < 5; i++) {
            wall[rear] = mHeights[i];
            
            if (rear >= 4) {
                int[] diffList = new int[] {wall[rear-4] - wall[rear-3], wall[rear-3] - wall[rear-2], wall[rear-2] - wall[rear-1], wall[rear-1] - wall[rear-0]};
                hash.merge(Arrays.toString(diffList), 1, Integer::sum);
            }
        
            rear++;
        }
    }

    int matchPiece(int mHeights[]) {
        int[] diffList = new int[] {mHeights[3]-mHeights[4], mHeights[2]-mHeights[3], mHeights[1]-mHeights[2], mHeights[0]-mHeights[1]};
        if (hash.getOrDefault(Arrays.toString(diffList), 0) <= 0) return -1;

        int count;
        for (int left = rear-5; left >= 0; left--) {
            count = wall[left] + mHeights[4];
            if (count == (wall[left + 1] + mHeights[3]) &&
                count == (wall[left + 2] + mHeights[2]) &&
                count == (wall[left + 3] + mHeights[1]) &&
                count == (wall[left + 4] + mHeights[0])) {

                for (int i=left; i < left + 9 && i < rear-1; i++) {
                    if (i >= 4) {
                        diffList = new int[] {wall[i-4] - wall[i-3], wall[i-3] - wall[i-2], wall[i-2] - wall[i-1], wall[i-1] - wall[i-0]};
                        hash.compute(Arrays.toString(diffList), (k,v) -> v - 1);
                    }
                }

                for (int i=left; i < rear - 5; i++) {
                    wall[i] = wall[i+5];

                    if (i >= 4 && i < left + 4) {
                        diffList = new int[] {wall[i-4] - wall[i-3], wall[i-3] - wall[i-2], wall[i-2] - wall[i-1], wall[i-1] - wall[i-0]};
                        hash.merge(Arrays.toString(diffList), 1, Integer::sum);
                    }
                }
                rear -= 5;

                return left + 1;
            }
        }
        return -1;
    }
}
