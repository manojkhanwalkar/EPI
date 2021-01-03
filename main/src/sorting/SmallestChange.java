package sorting;

public class SmallestChange {

    public static void main(String[] args) {

        int[] arr = { 1,1,1,1,1,5,10,25};

        int V = arr[0];

        for (int i=1;i<arr.length;i++)
        {
            if (arr[i] > V+1)
                break;
            else
                V=V+arr[i];
        }

        System.out.println(V+1);

    }
}
