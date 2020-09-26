import java.util.*;


public class ColorFlip {
    public static final int BLACK=0;
    public static final int WHITE=1;


    public static void main(String[] args) {

        //test1();
      // test2();
        test3();
    }

    public static int[][] cloneMaze(int[][] maze)
    {

        int[][] newMaze = new int[maze.length][maze[0].length];
        for (int i=0;i<maze.length;i++)
        {
            for (int j=0;j<maze[0].length;j++)
            {
                newMaze[i][j] = maze[i][j];
            }
        }

        return newMaze;

    }

    public static int[][] compareMaze(int[][] maze1, int[][] maze2)
    {

        int[][] diff = new int[maze1.length][maze1[0].length];
        for (int i=0;i<maze1.length;i++)
        {
            for (int j=0;j<maze1[0].length;j++)
            {
                if (maze1[i][j] != maze2[i][j])
                    diff[i][j] = 1;
            }
        }

        return diff;

    }

    public static void print(int[][] maze)
    {
        for (int i=0;i<maze.length;i++)
        {
            for (int j=0;j<maze[0].length;j++)
            {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();

    }


    public static void test1()
    {
        int[][] maze = new int[4][4];
        maze[2][2] = WHITE;
        maze[2][1] = WHITE;

        print(maze);

        flip(maze);

        print(maze);




    }

    public static void test2()
    {
        int[][] maze = new int[4][4];
        maze[3][0] = WHITE;
        maze[0][2] = WHITE;
        maze[1][2] = WHITE;
        maze[2][2] = WHITE;
        maze[3][2] = WHITE;
        maze[0][3] = WHITE;


        print(maze);

        flip(maze);

        print(maze);



    }

    public static void test3()
    {
        int[][] maze = new int[4][4];
        maze[0][0] = WHITE;
        maze[1][0] = WHITE;

        maze[2][0] = WHITE;
        maze[3][0] = WHITE;
        maze[0][3] = WHITE;
        maze[1][3] = WHITE;
        maze[3][3] = WHITE;
        maze[2][2] = WHITE;

        print(maze);

        flip(maze);

        print(maze);



    }


    public static void flip(int[][] maze)
    {
       Queue<Coordinate> queue = new ArrayDeque<>();

        Coordinate init = new Coordinate(2,2);

        int color = maze[init.x][init.y];

        queue.add(init);

        while(!queue.isEmpty())
        {
            Coordinate current = queue.remove();
            if (isValid(current,maze) && maze[current.x][current.y]==color) {
                maze[current.x][current.y] = color == 1 ? 0 : 1;

                queue.add(new Coordinate(current.x+1,current.y));
                queue.add(new Coordinate(current.x,current.y+1));
                queue.add(new Coordinate(current.x-1,current.y));
                queue.add(new Coordinate(current.x,current.y-1));



            }

        }





    }





    private static boolean isValid(Coordinate curr,int[][] maze)
    {
        return (curr.x>=0 && curr.x < maze[0].length && curr.y>=0 && curr.y < maze.length);

    }


}



