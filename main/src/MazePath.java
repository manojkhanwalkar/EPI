import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class MazePath {
    public static final int BLACK=0;
    public static final int WHITE=1;





    public static void main(String[] args) {

        test1();
        test2();
        test3();
    }

    public static void test1()
    {
        int[][] maze = new int[4][4];
        maze[2][2] = WHITE;
        maze[2][1] = WHITE;
        maze[1][1] = WHITE;
        maze[2][1] = WHITE;
        maze[2][2] = WHITE;
        maze[2][3] = WHITE;
        maze[3][3] = WHITE;

      findPath(maze);



    }

    public static void test2()
    {
        int[][] maze = new int[4][4];
        maze[0][0] = WHITE;
        maze[1][0] = WHITE;
        maze[3][0] = WHITE;
        maze[3][1] = WHITE;
        maze[3][2] = WHITE;
        maze[3][3] = WHITE;


        findPath(maze);


    }

    public static void test3()
    {
        int[][] maze = new int[4][4];
        maze[0][0] = WHITE;
        maze[1][0] = WHITE;

        maze[2][0] = WHITE;
        maze[3][0] = WHITE;
        maze[3][1] = WHITE;
        maze[3][2] = WHITE;
        maze[3][3] = WHITE;

        findPath(maze);



    }


    public static void findPath(int[][] maze)
    {
        List<Coordinate> path = new ArrayList<>();
        Coordinate start = new Coordinate(0,0);
        Coordinate end = new Coordinate(3,3);

        findPath(path,maze,start,end);

        System.out.println(path);


    }



    private static boolean findPath(List<Coordinate> path, int[][] maze, Coordinate start, Coordinate end) {

        if (!isValid(start,maze))
            return false;

        if (maze[start.x][start.y]==BLACK)
            return false;

        path.add(start);

        if (start.equals(end))
            return true;



        maze[start.x][start.y] = BLACK ;  // visited the current node

        if (findPath(path,maze,new Coordinate(start.x+1,start.y),end))
            return true;
        if (findPath(path,maze,new Coordinate(start.x,start.y+1),end))
            return true;
        if (findPath(path,maze,new Coordinate(start.x-1,start.y),end))
            return true;
        if (findPath(path,maze,new Coordinate(start.x,start.y-1),end))
            return true;

        path.remove(start);

        return false;


    }

    private static boolean isValid(Coordinate curr,int[][] maze)
    {
        return (curr.x>=0 && curr.x < maze[0].length && curr.y>=0 && curr.y < maze.length);

    }


}



