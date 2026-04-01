import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
    private static final int CMD_INIT = 100;
    private static final int CMD_JOIN = 200;
    private static final int CMD_TOP5 = 300;

    private static UserSolution usersolution = new UserSolution();

    public static class RESULT {
        int cnt;
        int[] IDs = new int[5];

        RESULT() {
            cnt = -1;
        }
    }

    private static boolean run(BufferedReader br) throws Exception {
        int Q;
        int N, mTime, mID, mX, mY, mLength;
        int cnt, ans;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(st.nextToken());

        boolean okay = false;

        for (int q = 0; q < Q; ++q) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case CMD_INIT:
                    N = Integer.parseInt(st.nextToken());
                    usersolution.init(N);
                    okay = true;
                    break;
                case CMD_JOIN:
                    mTime = Integer.parseInt(st.nextToken());
                    mID = Integer.parseInt(st.nextToken());
                    mX = Integer.parseInt(st.nextToken());
                    mY = Integer.parseInt(st.nextToken());
                    mLength = Integer.parseInt(st.nextToken());
                    usersolution.join(mTime, mID, mX, mY, mLength);
                    break;
                case CMD_TOP5:
                    mTime = Integer.parseInt(st.nextToken());
                    RESULT ret = usersolution.top5(mTime);
                    cnt = Integer.parseInt(st.nextToken());
                    if (cnt != ret.cnt)
                        okay = false;
                    for (int i = 0; i < cnt; ++i) {
                        ans = Integer.parseInt(st.nextToken());
                        if (ans != ret.IDs[i])
                            okay = false;
                    }
                    break;
                default:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception {
        System.setIn(new java.io.FileInputStream("sample_input-2.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int TC = Integer.parseInt(st.nextToken());
        int MARK = Integer.parseInt(st.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        br.close();
    }
}