package DP;

public class Path2DArray {

    public static void main(String[] args) {

        int[][] arr = new  int[5][5];

        for (int i=0;i<arr[0].length;i++)
        {
            arr[0][i] = 1;
            arr[i][0] = 1 ;
        }

        for (int i=1;i<arr.length;i++)
        {
            for (int j=1;j<arr.length;j++)
            {
                arr[i][j] = arr[i-1][j] + arr[i][j-1];

            }
        }


        System.out.println( arr[arr.length-1][arr.length-1]);



    }

}
