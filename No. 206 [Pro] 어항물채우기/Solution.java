import java.util.Scanner;

class Solution {
    private static UserSolution usersolution = new UserSolution();

    private final static int CMD_INIT = 1;
    private final static int CMD_ADD = 2;
	private final static int CMD_CHECK = 3;
    private final static int CMD_POUR = 4;

    private final static int MAX_N = 20;
    private final static int MAX_WIDTH = 500;

    static class Result {
        int ID;
        int height;
        int used;
    }

    private static int mIDs[] = new int[MAX_N];
    private static int mLengths_tanks[][] = new int[MAX_N][MAX_WIDTH];
    private static int mUpShapes_tanks[][] = new int[MAX_N][MAX_WIDTH];

    private static boolean run(Scanner sc) throws Exception {

        int query_num = sc.nextInt();
        boolean ok = false;

        for (int q = 0; q < query_num; q++) {
            int query = sc.nextInt();

            if (query == CMD_INIT) {
                int N = (int)sc.nextFloat();
                int mWidth = (int)sc.nextFloat();
                int mHeight = (int)sc.nextFloat();
                for (int i = 0; i < N; i++) {
                    mIDs[i] = (int)sc.nextFloat();
                }
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < mWidth; j++)
                        mLengths_tanks[i][j] = (int)sc.nextFloat();
                }
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < mWidth; j++)
                        mUpShapes_tanks[i][j] = (int)sc.nextFloat();
                }
                usersolution.init(N, mWidth, mHeight, mIDs, mLengths_tanks, mUpShapes_tanks);
                ok = true;
            } else if (query == CMD_CHECK) {
                int mLengths[] = new int[3];
                int mUpShapes[] = new int[3];
                int mDownShapes[] = new int[3];
                for (int i = 0; i < 3; i++) {
                    mLengths[i] = sc.nextInt();
                }
                for (int i = 0; i < 3; i++) {
                    mUpShapes[i] = sc.nextInt();
                }
                for (int i = 0; i < 3; i++) {
                    mDownShapes[i] = sc.nextInt();
                }
                int ret = usersolution.checkStructures(mLengths, mUpShapes, mDownShapes);
                int ans = sc.nextInt();
                if (ans != ret) {
                    ok = false;
                    System.out.println("CMD_CHECK " + ans + " but " + ret);
                }
            } else if (query == CMD_ADD) {
                int mLengths[] = new int[3];
                int mUpShapes[] = new int[3];
                int mDownShapes[] = new int[3];
                for (int i = 0; i < 3; i++) {
                    mLengths[i] = (int)sc.nextFloat();
                }
                for (int i = 0; i < 3; i++) {
                    mUpShapes[i] = (int)sc.nextFloat();
                }
                for (int i = 0; i < 3; i++) {
                    mDownShapes[i] = (int)sc.nextFloat();
                }
                int ret = usersolution.addStructures(mLengths, mUpShapes, mDownShapes);
                int ans = (int)sc.nextFloat();
                if (ans != ret) {
                    ok = false;
                    System.out.println("CMD_ADD " + ans + " but " + ret);
                }
            } else if (query == CMD_POUR) {
                int mWater = sc.nextInt();
                Result ret = usersolution.pourIn(mWater);
                int ans_height = 0;
                int ans_used = 0;
                int ans = sc.nextInt();
                if (ans != 0) {
                    ans_height = sc.nextInt();
                    ans_used = sc.nextInt();
                }
                if (ans != 0 && (ans != ret.ID || ans_height != ret.height || ans_used != ret.used)) {
                    ok = false;
                    System.out.println("CMD_POUR " + ans + " but " + ret.ID);
                } else if (ans == 0 && ret.ID != 0) {
                    ok = false;
                    System.out.println("CMD_POUR " + ans + " but " + ret.ID);
                }
            }
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;
        System.setIn(new java.io.FileInputStream("sample_input.txt"));
        Scanner sc = new Scanner(System.in);

        T = sc.nextInt();
        MARK = sc.nextInt();

        for (int tc = 1; tc <= T; tc++) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        sc.close();
    }
}