import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_CHECK = 400;
    private final static int CMD_COUNT = 500;

    private final static UserSolution usersolution = new UserSolution();

    private static boolean run(BufferedReader br) throws Exception {
        int q = Integer.parseInt(br.readLine());

        int l, r, mId, mX, mY;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());
            switch (cmd) {
                case CMD_INIT:
                    okay = true;
                    l = Integer.parseInt(st.nextToken());
                    r = Integer.parseInt(st.nextToken());
                    usersolution.init(l, r);
                    break;
                case CMD_ADD:
                    mId = Integer.parseInt(st.nextToken());
                    mX = Integer.parseInt(st.nextToken());
                    mY = Integer.parseInt(st.nextToken());
                    ans = Integer.parseInt(st.nextToken());
                    ret = usersolution.add(mId, mX, mY);
                    if (ret != ans)
                        okay = false;
                        // System.out.println("CMD_ADD " + ans + " but " + ret);
                    break;
                case CMD_REMOVE:
                    mId = Integer.parseInt(st.nextToken());
                    ans = Integer.parseInt(st.nextToken());
                    ret = usersolution.remove(mId);
                    if (ret != ans)
                        okay = false;
                        // System.out.println("CMD_REMOVE " + ans + " but " + ret);
                    break;
                case CMD_CHECK:
                    mId = Integer.parseInt(st.nextToken());
                    ans = Integer.parseInt(st.nextToken());
                    ret = usersolution.check(mId);
                    if (ret != ans)
                        okay = false;
                        // System.out.println("CMD_CHECK " + ans + " but " + ret);
                    break;
                case CMD_COUNT:
                    ans = Integer.parseInt(st.nextToken());
                    ret = usersolution.count();
                    if (ret != ans)
                        okay = false;
                        // System.out.println("CMD_COUNT " + ans + " but " + ret);
                    break;
                default:
                    okay = false;
                    break;
            }
        }
        return okay;
    }

    public static void main(String[] args) throws Exception {
        int TC, MARK;

        System.setIn(new java.io.FileInputStream("sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        TC = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        br.close();
    }
}