import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;


public class ContiguousPath {
    public static final int BLACK=0;
    public static final int WHITE=1;


    public static void main(String[] args) {

        //test1();
      test2();
        //test3();
    }




    private static void print(int[][] maze)
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
        maze[1][0] = WHITE;
        maze[1][1] = WHITE;

        print(maze);


        System.out.println(path(maze));



    }

    public static void test2()
    {
        int[][] maze = new int[4][4];
        maze[1][2] = WHITE;
        maze[2][1] = WHITE;
        maze[2][2] = WHITE;
        maze[3][1] = WHITE;


        print(maze);

        System.out.println(path(maze));

        //print(maze);



    }

    public static void test3()
    {
        int[][] maze = new int[4][4];
        maze[1][1] = WHITE;
        maze[1][2] = WHITE;

        maze[2][2] = WHITE;
        maze[3][1] = WHITE;


        print(maze);



        System.out.println(path(maze));



    }


    public static Set<Coordinate> path(int[][] maze)
    {

       Queue<Coordinate> queue = new ArrayDeque<>();

       // find all W in the edges - ie for all x = 0 , x = maze[0].length-1 , y = 0 , y = maze.length

        for (int i=0;i<maze.length;i++)
        {
            if (maze[0][i]==1)
            {
                Coordinate init = new Coordinate(0,i);
                queue.add(init);
                maze[0][i] = BLACK;
            }
            else if (maze[maze[0].length-1][i]==1)
            {
                Coordinate init = new Coordinate(maze[0].length-1,i);
                queue.add(init);
                maze[maze[0].length-1][i]=BLACK;
            }
        }

        for (int i=0;i<maze[0].length;i++)
        {
            if (maze[i][0]==1)
            {
                Coordinate init = new Coordinate(i,0);
                queue.add(init);
                maze[i][0] = BLACK;
            }
            else if (maze[i][maze.length-1]==1)
            {
                Coordinate init = new Coordinate(i,maze.length-1);
                queue.add(init);
                maze[i][maze.length-1]=BLACK;
            }
        }


        Set<Coordinate> paths = new HashSet<>();
        while(!queue.isEmpty())
        {
            Coordinate current = queue.remove();
            paths.add(current);

            Coordinate next = new Coordinate(current.x+1,current.y);
            processNext(queue,next,maze);

            next = new Coordinate(current.x,current.y+1);
            processNext(queue,next,maze);

            next = new Coordinate(current.x-1,current.y);
            processNext(queue,next,maze);

            next = new Coordinate(current.x,current.y-1);
            processNext(queue,next,maze);


        }

        return paths;



    }

    private static void processNext(Queue<Coordinate> queue, Coordinate next, int[][] maze)
    {
        if (isValid(next,maze) && maze[next.x][next.y]==1)
        {
            queue.add(next);
            maze[next.x][next.y] = BLACK;
        }

    }





    private static boolean isValid(Coordinate curr,int[][] maze)
    {
        return (curr.x>=0 && curr.x < maze[0].length && curr.y>=0 && curr.y < maze.length);

    }


}



