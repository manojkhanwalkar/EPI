package multiprocessor.sets;

public abstract class GrainedList<T> implements WFSet<T> {

    Node1<T> head = new Node1();
    Node1<T> tail = new Node1();


    public GrainedList()
    {
        head.next = tail;
        head.key = Integer.MIN_VALUE;
        tail.key=Integer.MAX_VALUE;
    }


    public void print()
    {


        Node1<T> curr = head;

        int count=0;

        while(curr!=null)
        {
            count++;

            System.out.print(curr.key) ;
            System.out.print(" ");

            curr = curr.next;
        }

        System.out.println();

        System.out.println(count);



        // set.stream().sorted().forEach(System.out::println);
    }

}
