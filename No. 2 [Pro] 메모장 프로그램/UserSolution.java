import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class UserSolution
{
    LinkedList<Character> list;
    int H, W;
    int cursor;

	void init(int H, int W, char mStr[])
	{
        this.H = H;
        this.W = W;
        list = new LinkedList<Character>();
        for (char c : mStr) {
            if ( c != '\0' ) list.add(c);
        }
        cursor = 0;
	}
	
	void insert(char mChar)
	{
        list.add(cursor, mChar);
        cursor++;
	}

	char moveCursor(int mRow, int mCol)
	{
        int length = list.size();
        cursor = (mRow-1) * W + mCol-1;

        if (cursor >= length) {
            cursor = length;
            return '$';
        }

		return list.get(cursor);
	}

	int countCharacter(char mChar)
	{
        int count = 0;
        for (int i = cursor; i < list.size(); i++) {
            if (list.get(i) == mChar) {
                count++;
            }
        }

        return count;
	}
}