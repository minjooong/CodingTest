import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
	private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private final static int WIDTH = 8;
    
    private final static int CMD_INIT = 100;
    private final static int CMD_TAKETURN = 200;

    private static int tiles[][] = new int[10000][WIDTH];

    private static boolean run() throws Exception {
    	StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");
    	
    	boolean okay = false;
    	int Q = Integer.parseInt(stdin.nextToken());

    	for (int q = 0; q < Q; ++q)
    	{
    		stdin = new StringTokenizer(br.readLine(), " ");
    		int cmd = Integer.parseInt(stdin.nextToken());
    		switch (cmd)
    		{
    		case CMD_INIT:
    			int N = Integer.parseInt(stdin.nextToken());
    			for(int y = 0;y < N;y++)
    			{
    				stdin = new StringTokenizer(br.readLine(), " ");
    				for(int x = 0;x < WIDTH;x++)
    				{
    					tiles[y][x] = Integer.parseInt(stdin.nextToken());
    				}
    			}
    			userSolution.init(N, tiles);
    			okay = true;
    			break;
    		case CMD_TAKETURN:
    			int[] user_ans = userSolution.takeTurn();
    			for (int i = 0; i < 5;i++)
    			{
    				int correct_ans =  Integer.parseInt(stdin.nextToken());
    				if(user_ans[i] != correct_ans)
    				{
    					okay = false;
    				}
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
        int T, MARK;

        System.setIn(new java.io.FileInputStream("sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}