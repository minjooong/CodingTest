import java.util.*;

public class Parcel {

    static class Box
    {
        int k, h, w, c, r;

        Box(int k, int h, int w, int c, int r)
        {
            this.k = k; this.h = h; this.w = w; this.c = c; this.r = r;
        }
    }

    static int N;
    static int M;
    static int[][] space;
    static HashMap<Integer, Box> boxes = new HashMap<>();
    static List<Box> boxlist;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        N = sc.nextInt();
        M = sc.nextInt();

        space = new int[N][N];

        for (int i = 0; i < M; i++)
        {
            int k = sc.nextInt();
            int h = sc.nextInt();
            int w = sc.nextInt();
            int c = sc.nextInt() - 1;

            // 1. 박스 떨어뜨리기
            Box newBox = new Box(k, h, w, c, 0);
            boxes.put(k, newBox);

            for (int nx = 0; nx < h; nx++)
            {
                for (int ny = c; ny < c + w; ny++)
                {
                    space[nx][ny] = k;
                }
            }

            MoveDown(newBox);
        }


        boxlist = new ArrayList<>(boxes.values());
        while (!boxes.isEmpty())
        {
            // Log();
            // 2. 좌측 하차
            boxlist = new ArrayList<>(boxes.values());
            boxlist.sort((b1, b2) -> b1.k - b2.k);
            for (Box box : boxlist)
            {
                if (MoveLeft(box)) break;
            }
            // 2-1. 박스 떨어뜨리기
            boxlist = new ArrayList<>(boxes.values());
            boxlist.sort((b1, b2) -> b2.r - b1.r);
            for (Box box : boxlist)
            {
                MoveDown(box);
            }


            // 3. 우측 하차
            boxlist = new ArrayList<>(boxes.values());
            boxlist.sort((b1, b2) -> b1.k - b2.k);
            for (Box box : boxlist)
            {
                if (MoveRight(box)) break;
            }
            // 3-1. 박스 떨어뜨리기
            boxlist = new ArrayList<>(boxes.values());
            boxlist.sort((b1, b2) -> b2.r - b1.r);
            for (Box box : boxlist)
            {
                MoveDown(box);
            }
        }
        
    }

    static void Log()
    {

        for (int nx = 0; nx < N; nx++)
        {
            for (int ny = 0; ny < N; ny++)
            {
                System.out.print(space[nx][ny] + "\t");
            }
            System.out.println();
        }

    }

    static boolean MoveRight(Box box)
    {
        int ny = box.c + box.w;

        while(true)
        {
            if (ny >= N)
            {
                System.out.println(box.k);

                for (int x = box.r; x < box.r + box.h; x++)
                {
                    for (int y = box.c; y < box.c + box.w; y++)
                    {
                        space[x][y] = 0;
                    }
                }

                boxes.remove(box.k);
                M--;
                return true;
            }

            for (int nx = box.r; nx < box.r + box.h; nx++)
            {
                if (space[nx][ny] != 0) return false;
            }
            
            ny++;
        }
    }



    static boolean MoveLeft(Box box)
    {
        int ny = box.c - 1;

        while(true)
        {
            if (ny < 0)
            {
                System.out.println(box.k);

                for (int x = box.r; x < box.r + box.h; x++)
                {
                    for (int y = box.c; y < box.c + box.w; y++)
                    {
                        space[x][y] = 0;
                    }
                }

                boxes.remove(box.k);
                M--;
                return true;
            }

            for (int nx = box.r; nx < box.r + box.h; nx++)
            {
                if (space[nx][ny] != 0) return false;
            }
            
            ny--;
        }
    }

    static void MoveDown(Box box)
    {
        int nx = box.r + box.h;

        while (true)
        {
            if (nx >= N) return;

            for (int ny = box.c; ny < box.c + box.w; ny++)
            {
                if (space[nx][ny] != 0) return;
            }

            for (int ny = box.c; ny < box.c + box.w; ny++)
            {
                space[box.r][ny] = 0;
                space[nx][ny] = box.k;
            }
            box.r++;
            nx++;
        }
    }
}
