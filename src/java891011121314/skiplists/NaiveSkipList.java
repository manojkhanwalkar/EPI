package java891011121314.skiplists;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class NaiveSkipList {

    static class Node
    {
        Node left;
        Node right;
        Node top;
        Node bottom;

        int key ;


        public Node(int key)
        {
            this.key = key;

        }
    }

    Node head  ;
    Node tail;

    int maxHeight;

    public NaiveSkipList(int min , int max, int maxHeight)
    {

        this.maxHeight = maxHeight;
        initList(min,max);

    }


    private void initList(int min, int max)
    {
        head = new Node(min);
        tail = new Node(max);
        head.right = tail;

        Node first = head;
        Node second = tail;
        for (int i=1;i<maxHeight;i++)
        {
            Node tmp1 = new Node(min);
            Node tmp2  = new Node(max);

            first.bottom=tmp1;
            second.bottom=tmp2;
            tmp1.right=tmp2;

            first = tmp1;
            second = tmp2;

        }
    }

    Random random = new Random();

    public void add(int key)
    {
        int height = 1+random.nextInt(maxHeight-1);
        Stack<Node> nodes = new Stack<>();
        Node curr = head;
        nodes.push(curr);
        while(true) {
            while (curr.right.key <= key) {
                curr = curr.right;
                //nodes.add(curr);
            }

            if (curr.key==key)
                return;

            if (curr.bottom!=null) {
                nodes.add(curr);
                curr = curr.bottom;
            }
            else {
                while (curr.right.key<=key)
                {
                    curr = curr.right;
                }

                break;
            }

        }

        if (curr.key==key) // dont add as it exists
        {
            return;
        }

        Node node = new Node(key);

        Node next=curr.right;

        curr.right=node;
        node.left=curr;
        node.right=next;
        next.left=node;

       // nodes.pop();

        while(height!=0)
        {
            Node node1 = new Node(key);
            Node tmp = nodes.pop();
            node1.right = tmp.right;
            tmp.right=node1;
            node.top = node1;
            node1.bottom = node;
            node1.left = tmp;
            node1.right.left=node1;
            height--;

            node = node1;
        }

    }

    private Node find(int key)
    {
        Node curr = head;
        while(true) {
            while (curr.right.key <= key) {
                curr = curr.right;
                //nodes.add(curr);
            }

            if (curr.key==key)
                return curr;

            if (curr.bottom!=null) {
                curr = curr.bottom;
            }
            else {
                while (curr.right.key<=key)
                {
                    curr = curr.right;
                }

                break;
            }

        }

        if (curr.key==key) // dont add as it exists
        {
            return curr;
        }


        return null;

    }

   public boolean contains(int key)
    {

        if (find(key)!=null)
            return true;
        else
            return false;
    }

   public boolean remove(int  key)
    {
        Node curr = find(key);
        if (curr==null)
            return false;

        while(curr!=null)
        {
            Node prev = curr.left;

            prev.right = curr.right;
            curr.right.left = prev;

            curr = curr.bottom;
        }

        return true;
    }


    public void print()
    {
        Node curr = head;
        while(curr!=null)
        {
            Node tmp = curr;
            while(tmp!=null)
            {
                System.out.print(tmp.key+   " ") ;
                tmp = tmp.right;
            }
            System.out.println();
            curr = curr.bottom;
        }


    }


    public static void main(String[] args) {

        NaiveSkipList  list = new NaiveSkipList(Integer.MIN_VALUE, Integer.MAX_VALUE,10);

        for (int i=0;i<10;i++)
        {
            list.add(i);
            list.add(30-i);
        }


        list.print();


    /*    System.out.println("\n\n");

        for (int i=0;i<10;i++)
        {
            list.add(i);
            list.add(30-i);
        }


        list.print();
*/

  /*      for (int i=0;i<30;i++)
        {
            System.out.println(i + " contains " + list.contains(i));
        }*/


        for (int i=0;i<=30;i++)
        {
            System.out.println(i + " removed " + list.remove(i));
        }



        list.print();

    }



}
