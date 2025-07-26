import java.util.*;

class UserSolution
{
    ArrayList<LinkedList<Character>> list;
    ArrayList<int[]> countList;
    int H, W;
    int cRow, cCol; // 현재 마지막 글자 위치
    int row, col;

	void init(int H, int W, char mStr[])
	{
        this.H = H;
        this.W = W;
        this.list = new ArrayList<>();
        this.countList = new ArrayList<>();
        for (int i = 0; i < H; i++) {
            list.add(new LinkedList<Character>());
            countList.add(new int[26]);
        }
        row = col = 0;
        for (char c : mStr) {
            if ( c == '\0' ) continue;

            list.get(row).addLast(c);
            countList.get(row)[c - 'a']++;
            if (++col >= W) {
                col = 0;
                row++;
            }
        }
        col -= 1;
        cRow = row;
        cCol = col;
        row = col = 0;
	}
	
	void insert(char mChar)
	{
        list.get(row).add(col, mChar);
        countList.get(row)[mChar - 'a']++;

        for (int i = row; i <= cRow; i++) {
            if (list.get(i).size() > W) {
                char overFlow = list.get(i).removeLast();
                countList.get(i)[overFlow - 'a']--;
                list.get(i+1).addFirst(overFlow);
                countList.get(i+1)[overFlow - 'a']++;
            }
        }
        if (++col >= W) {
            col = 0;
            row++;
        }
        if (++cCol >= W) {
            cCol = 0;
            cRow++;
        }
	}

	char moveCursor(int mRow, int mCol)
	{
        if (mRow > cRow+1 || (mRow == cRow+1 && mCol > cCol+1)) {
            row = cRow;
            col = cCol+1;
            return '$';
        }
        row = mRow-1;
        col = mCol-1;
		return list.get(row).get(col);
	}

	int countCharacter(char mChar)
	{
        int count = 0;
        ListIterator<Character> it = list.get(row).listIterator(col);
        while (it.hasNext()) {
            Character ch = it.next();
            if (ch == mChar) {
                count++;
            }
        }

        // 시간복잡도 O(HW) -> 시간초과 (40,000 * 300 * 300 = 3,600,000,000)
        // for (int i = row+1; i <= cRow; i++) {
        //     for (char c : list.get(i)) {
        //         if (c == mChar) {
        //             count++;
        //         }
        //     }
        // }
        for (int i = row+1; i <= cRow; i++) {
            count += countList.get(i)[mChar - 'a'];
        }

        return count;
	}
}