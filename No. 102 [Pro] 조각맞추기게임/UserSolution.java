import java.util.*;

class UserSolution {

    int[] wall = new int[100_000];
    int rear;

    void init() {
        Arrays.fill(wall, 0);
        rear = 0;
    }

    void makeWall(int mHeights[]) {
        for (int i=0; i < 5; i++) {
            wall[rear] = mHeights[i];
            rear++;
        }
    }

    int matchPiece(int mHeights[]) {
        int count;

        for (int left = rear-5; left >= 0; left--) {
            count = wall[left] + mHeights[4];
            if (count == (wall[left + 1] + mHeights[3]) &&
                count == (wall[left + 2] + mHeights[2]) &&
                count == (wall[left + 3] + mHeights[1]) &&
                count == (wall[left + 4] + mHeights[0])) {
                
                for (int i=left; i < rear - 5; i++) {
                    wall[i] = wall[i+5];
                }
                rear -= 5;
                return left + 1;
            }
        }
        
        return -1;
    }
}
