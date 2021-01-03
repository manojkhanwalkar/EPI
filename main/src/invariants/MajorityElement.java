package invariants;

public class MajorityElement {


    public static void main(String[] args) {

        char[] arr = {'b', 'a' , 'c', 'a' , 'a', 'b', 'a','a', 'c', 'a'};

        char maj = arr[0];
        int count = 1;

        for (int i=1;i<arr.length;i++)
        {
            if (maj==arr[i])
            {
                count++;
                continue;
            }
            else
            {
                if (count==0)
                {
                    maj=arr[i];
                }
                else if (count==1)
                {
                    maj=arr[i]; count--;
                }
                else
                    count--;
            }



        }


        System.out.println(maj);


    }
}
