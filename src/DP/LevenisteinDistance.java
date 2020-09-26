package DP;

public class LevenisteinDistance {

    public static void main(String[] args) {

        System.out.println(editDistance("carthorse" , "orchestra"));

    }

    public static int editDistance(String s1,String s2)
    {
       int[][] chars = new int[s1.length()+1][s2.length()+1];

       for (int i=0;i<chars[0].length;i++)
       {
           chars[0][i] = i;
       }

       for (int i=0;i<chars.length;i++)
       {
           chars[i][0] = i;
       }

       for (int i=1;i<chars[0].length;i++)
       {
           for (int j=1;j<chars.length;j++)
           {
               int min = Math.min(Math.min(chars[i][j-1],chars[i-1][j]),chars[i-1][j-1]);
               if (s1.charAt(i-1)!=s2.charAt(j-1))
                   min++;
               chars[i][j] = min;
           }
       }

       return chars[s1.length()][s2.length()];



    }

}
