package tree;

import java.util.*;

public class BST {


    public static BinaryNode<Integer> search(BinaryNode<Integer> root , Integer key)
    {

            if (key==root.data)
                return root;
            else if (key<root.data)
            {
                if (root.left!=null)
                    return search(root.left,key);

            }
            else
            {
                if (root.right!=null)
                    return search(root.right,key);
            }


            return null; // key not found in the tree
    }


   public static void print(BinaryNode<Integer> current)
   {
       // print left , self , right

       if (current==null)
           return;

       print(current.left);
       System.out.println(current.data);
       print(current.right);


   }


    public static BinaryNode<Integer> build(int... elements)
    {

        BinaryNode<Integer> root = new BinaryNode<>(elements[0]);


        for (int i=1;i<elements.length;i++)
        {
            BinaryNode<Integer> curr = root;
            while(true) {
                if (elements[i] < curr.data) {
                    if (curr.left==null)
                    {
                        curr.left = new BSTNode<>(elements[i]);
                        break;
                    }
                    else
                    {
                        curr = curr.left;
                    }
                }
                else
                {
                    if (curr.right==null)
                    {
                        curr.right = new BSTNode<>(elements[i]);
                        break;
                    }
                    else
                    {
                        curr = curr.right;
                    }
                }
            }
        }

        return root;
    }




    public static boolean isBinaryTree(BinaryNode<Integer> root)
    {
        return isBinaryTree(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static boolean isBinaryTree(BinaryNode<Integer> root, int low , int high) {
        if (!(root.data >= low) || !(root.data <= high))
            return false;
        else {
            boolean left = true, right = true;
            if (root.left != null) {
                left = isBinaryTree(root.left, low, root.data);
            }
            if (root.right != null) {
                right = isBinaryTree(root.right, root.data, high);
            }

            return (right && left);

        }
    }


    public static void test2()
    {
        BinaryNode<Integer> fifteen = new BinaryNode<>(15);
        BinaryNode<Integer> twelve = new BinaryNode<>(12);
        BinaryNode<Integer> twenty = new BinaryNode<>(20);
        BinaryNode<Integer> nineteen = new BinaryNode<>(19);
        BinaryNode<Integer> twentyone = new BinaryNode<>(21);


     /*   fifteen.left = twelve;
        fifteen.right=twenty;
        twenty.right = twentyone;
        twenty.left = nineteen;*/
/*
     fifteen.left = twelve;
     fifteen.right= twenty;
     fifteen.right = twentyone;
     twelve.right= nineteen;
*/

     /*   fifteen.right= twelve;
        fifteen.left = twenty;
        twelve.right=nineteen;
        twenty.right = twentyone;

        boolean result = isBinaryTree(fifteen);

        System.out.println(result);*/


    }

    public static void test4()
    {
        int[] elements = { 43,23,37,29,31,41,47,53};

        BSTNode<Integer> root = buildFromPreOrder(elements);

        boolean result = isBinaryTree(root);

        System.out.println(result);

    }



    private static BSTNode<Integer> buildFromPreOrder(int[] elements) {

        BSTNode<Integer> root = new BSTNode<>(elements[0]);
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;

        build(min,max,root,elements,1);


        return root;

    }

    public static List<BinaryNode<Integer>>  inorder(BinaryNode<Integer> root)
    {

        List<BinaryNode<Integer>> values = new ArrayList<>();

         inorder(root,values);

         return values;
    }

    private static void inorder(BinaryNode<Integer> root,List<BinaryNode<Integer>> values)
    {
        // left , self , right

        if (root==null)
            return;

        inorder(root.left,values);

        values.add(root);

        inorder(root.right,values);
    }


    public static List<BinaryNode<Integer>>  preorder(BinaryNode<Integer> root)
    {

        List<BinaryNode<Integer>> values = new ArrayList<>();

        preorder(root,values);

        return values;
    }

    private static void preorder(BinaryNode<Integer> root,List<BinaryNode<Integer>> values)
    {
        // self , left , right

        if (root==null)
            return;

        values.add(root);

        preorder(root.left,values);



        preorder(root.right,values);
    }


    public static List<BinaryNode<Integer>> inorderViaStack(BinaryNode<Integer> root)
    {

        List<BinaryNode<Integer>> values = new ArrayList<>();
        Stack<BinaryNode<Integer>> stack = new Stack<>();



        BinaryNode<Integer> curr = root;

        while(!stack.isEmpty() || curr!=null)
        {
            if (curr!=null) {
                stack.push(curr);

                curr = curr.left;
            }
            else
            {
                curr = stack.pop();
                values.add(curr);
                curr = curr.right;
            }

        }

        return values;
    }


    public static List<BinaryNode<Integer>> preorderViaStack(BinaryNode<Integer> root)
    {

        // self , left , right

        List<BinaryNode<Integer>> values = new ArrayList<>();
        Stack<BinaryNode<Integer>> stack = new Stack<>();


        stack.push(root);

        while(!stack.isEmpty())
        {


            BinaryNode<Integer> curr = stack.pop();

            values.add(curr);

            if (curr.right!=null)
            {
                stack.push(curr.right);
            }

            if (curr.left!=null)
            {
                stack.push(curr.left);
            }


        }

        return values;
    }



    public static List<BinaryNode<Integer>>  postorder(BinaryNode<Integer> root)
    {

        List<BinaryNode<Integer>> values = new ArrayList<>();

        postorder(root,values);

        return values;
    }

    private static void postorder(BinaryNode<Integer> root,List<BinaryNode<Integer>> values)
    {
        // self , left , right

        if (root==null)
            return;



        postorder(root.left,values);



        postorder(root.right,values);

        values.add(root);
    }




    private static void build(int min, int max, BSTNode<Integer> root, int[] elements, int index) {

        if (index>=elements.length) // processing is complete
            return;
        int val = elements[index];

        BSTNode<Integer> curr = new BSTNode<>(val);



        if (val < root.data)
        {
            min = root.data;
            if (val > min && val < max)
            {
                root.left = curr;
                build(min,max,curr,elements,index+1);
            }
        }
        else
        {
            max = root.data;
            if (val>min && val < max)
            {
                root.right= curr;
                build(min,max,curr,elements,index+1);
            }
        }
    }


    public static void test3()
    {
        BSTNode<Integer> v19 = new BSTNode<>(19);
        BSTNode<Integer> v7 = new BSTNode<>(7);
        BSTNode<Integer> v43 = new BSTNode<>(43);
        BSTNode<Integer> v3 = new BSTNode<>(3);
        BSTNode<Integer> v11 = new BSTNode<>(11);
        BSTNode<Integer> v2 = new BSTNode<>(2);
        BSTNode<Integer> v5 = new BSTNode<>(5);
        BSTNode<Integer> v17 = new BSTNode<>(17);
        BSTNode<Integer> v13 = new BSTNode<>(13);
        BSTNode<Integer> v23 = new BSTNode<>(23);
        BSTNode<Integer> v47 = new BSTNode<>(47);
        BSTNode<Integer> v37 = new BSTNode<>(37);
        BSTNode<Integer> v53 = new BSTNode<>(53);
        BSTNode<Integer> v29 = new BSTNode<>(29);
        BSTNode<Integer> v41 = new BSTNode<>(41);
        BSTNode<Integer> v31 = new BSTNode<>(31);


        v19.left=v7;
        v19.right=v43;

        v7.left=v3;
        v7.right=v11;

        v3.left = v2;
        v3.right=v5;

        v11.right = v17;
        v17.left=v13;

        v43.left=v23;
        v43.right=v47;

        v23.right=v37;
        v37.left=v29;
        v37.right=v41;

        v29.right=v31;

        v47.right=v53;


        System.out.println(preorder(v19));
        System.out.println(preorderViaStack(v19));

      /*  System.out.println(inorder(v19));

        System.out.println(inorderViaStack(v19));*/


     /*   boolean result = isBinaryTree(v19);

        System.out.println(result);

        int key = firstKeyGreaterThan(23,v19);

        System.out.println(key);*/

    // System.out.println(maxKNumbers(5,v19));

/*       System.out.println(LCA(v13,v53,v19));

        populateParent(v19,null);

       System.out.println(LCAUsingParent(v13,v53));


        System.out.println(LCA1(v19,v13,v53).lca);*/

     /* System.out.println(inorder(v19));

      System.out.println(preorder(v19));

        System.out.println(postorder(v19));*/

    // System.out.println(calculateTreeHeight(v19));

    }


    static class Status {
        int numNodes;
        BinaryNode<Integer> lca;
    }
    public static Status LCA1(BinaryNode<Integer> root, BinaryNode<Integer> node1, BinaryNode<Integer> node2)
    {
        if (root==null)
            return null;
        Status leftStatus = LCA1(root.left,node1,node2);
        Status rightStatus = LCA1(root.right,node1,node2);

        if (leftStatus!=null && leftStatus.numNodes==2)
        {
            return leftStatus;
        }

        if (rightStatus!=null && rightStatus.numNodes==2)
        {
            return rightStatus;
        }

        int currVal =0;
        if (root.data.equals(node1.data))
            currVal++;
        if (root.data.equals(node2.data))
            currVal++;

        if (leftStatus!=null)
            currVal+=leftStatus.numNodes;
        if (rightStatus!=null)
            currVal+=rightStatus.numNodes;

        Status status = new Status();
        status.numNodes=currVal;
        if (currVal==2)
        {
            status.lca = root;
        }

        return status;
    }

    public static int calculateTreeHeight(BinaryNode<Integer> root)
    {
        if (root==null)
            return 0;


        int height=0;
        if (root.left==null && root.right==null)
            return 0;

        int left = calculateTreeHeight(root.left);
        int right = calculateTreeHeight(root.right);


        height = 1 + (left>right ? left: right);

        return height;

    }

    public static void populateParent(BinaryNode<Integer> node, BinaryNode<Integer> parent)
    {
        if (node==null)
            return;

        node.parent=parent;
        populateParent(node.left,node);
        populateParent(node.right,node);
    }


    public static BinaryNode<Integer> LCA(BinaryNode<Integer> node1, BinaryNode<Integer> node2 , BinaryNode<Integer> root)
    {
        // assume node1 data is smaller than node 2 data .

        if (node1.data <= root.data && node2.data>= root.data )
            return root;

        if (node1.data<=root.data && node2.data <= root.data)
            return LCA(node1,node2,root.left);
        else
            return LCA(node1,node2,root.right);
    }

    public static BinaryNode<Integer> LCAUsingParent(BinaryNode<Integer> node1, BinaryNode<Integer> node2) {

        Set<BinaryNode<Integer>> ancestors = new HashSet<>();

        BinaryNode<Integer> curr = node1;
        while (curr != null) {
            ancestors.add(curr);
            curr = curr.parent;
        }

        curr = node2;
        while (curr != null) {
            if (ancestors.contains(curr))
                return curr;

            curr = curr.parent;
        }


        return null;

    }

    public static List<Integer> maxKNumbers(int k , BinaryNode<Integer> node)
    {
        List<Integer> values = new ArrayList<>();

        maxKNumbers(k,node,values);

        return values;
    }

    private static void maxKNumbers(int k, BinaryNode<Integer> node, List<Integer> values)
    {

        // right , self , left .

        if (node.right!=null)
            maxKNumbers(k,node.right,values);

        if (values.size() < k)
        {
            values.add(node.data);
        }
        else
        {
            return;   // k numbers found , no more processing required.
        }

        if (node.left!=null)
        {
            maxKNumbers(k,node.left,values);
        }

    }


    public static int firstKeyGreaterThan(int key, BinaryNode<Integer> node)
    {
        int max = Integer.MAX_VALUE;

        return firstKeyGreaterThan(key,max,node);
    }

    private static int firstKeyGreaterThan(int key, int max, BinaryNode<Integer> node)
    {


        while(true) {

            if (node==null)
                return max;
            if (key < node.data) {
                max = node.data;
                node = node.left;

            }
            else
            {
                node = node.right;
            }

        }


    }






    public static void test1() {
            int[] arr = { 10, 15, 11, 20 , 9, 30 , 25 , 27};
        BinaryNode<Integer> root = build(arr);
            print(root);

            System.out.println(search(root,27));


        System.out.println(search(root,57));


    }


    private static int BinarySum(BinaryNode<Integer> root, int partialSum)
    {
        if (root==null)
            return 0;


        partialSum = 2*partialSum + root.data;

        if (root.left==null&& root.right==null)
            return partialSum;

        return BinarySum(root.left,partialSum) + BinarySum(root.right,partialSum);
    }


    public static void test6()
    {
        BinaryNode<Integer> root = BinaryNode.getBinaryValueTree1();

        printSumAndPathAtNodes(root,null);
    }

    static class SumPath
    {
        String path;
        int sum;

        public SumPath(int value, SumPath oldPath)
        {
            path = (oldPath==null?" " : oldPath.path) + "  " + value;
            sum = value+(oldPath==null?0 : oldPath.sum);
        }


        @Override
        public String toString() {
            return "SumPath{" +
                    "path='" + path + '\'' +
                    ", sum=" + sum +
                    '}';
        }
    }

    private static void printSumAndPathAtNodes( BinaryNode<Integer> root , SumPath oldpath) {
        if (root == null)
            return;

        SumPath path = new SumPath(root.data, oldpath);

        // left , self , right

        printSumAndPathAtNodes(root.left,path);

        System.out.println(path);

        printSumAndPathAtNodes(root.right,path);

    }


    public static void test5()
    {
        BinaryNode<Integer> root = BinaryNode.getBinaryValueTree();

      //  print(root);

       System.out.println( BinarySum(root,0));
    }

        public static void main(String[] args) {

        //test1();

          //  test2();

        //    test3();


     //       test4();

          //  test5();

//            test6();


//            test7();

        //    System.out.println(getLeaves(BinaryNode.getBinaryValueTree1()));

           // test8() ;

          //  test9();

           /* BinaryNode<Integer> root = BinaryNode.getBinaryValueTree1();
            System.out.println(findCover(root));*/

           test10();

        }


        public static void test10()
        {
            BinaryNode<Integer> root = BinaryNode.getBalancedBinaryTree();
            populateParent(root,null);
            populateRightSibling(root);

            printSelfAndRightSibling(root);
        }

        public static void printSelfAndRightSibling(BinaryNode<Integer> root)
        {
            Queue<BinaryNode<Integer>> queue = new ArrayDeque<>();

            queue.add(root);

            while(!queue.isEmpty())
            {
                BinaryNode<Integer> current = queue.remove();

                System.out.println(current.data + " " + (current.sibling!=null ? current.sibling.data:"null"));

                if (current.left!=null)
                {
                    queue.add(current.left);
                }

                if (current.right!=null)
                {
                    queue.add(current.right);
                }
            }
        }

        public static void populateRightSibling(BinaryNode<Integer> root)
        {
            if (root==null)
                return;
            if (root.parent!=null) {

                if (root == root.parent.left) {
                    root.sibling = root.parent.right;
                } else {
                    if (root.parent.sibling != null) {
                        root.sibling = root.parent.sibling.left;
                    }
                }

            }

            populateRightSibling(root.left);
            populateRightSibling(root.right);
        }

    private static boolean isLeaf(BinaryNode<Integer> node)
    {
        if (node.left==null && node.right==null)
            return true;
        else
            return false;
    }

    public static Set<BinaryNode<Integer>>  findCover(BinaryNode<Integer> root)
    {

        Set<BinaryNode<Integer>> set = new HashSet<>();
        // left , leaf , right

        BinaryNode<Integer> curr = root;

        while(!isLeaf(curr))
        {
            set.add(curr);
            curr = (curr.left!=null)? curr.left : curr.right;
        }
        set.add(curr);

        set.addAll(getLeaves(root));

        curr=root;

        while(!isLeaf(curr))
        {
            set.add(curr);
            curr = (curr.right!=null)? curr.right:curr.left;
        }

        set.add(curr);

        return set;

    }

    private static BinaryNode<Integer> getNode(int value, BinaryNode<Integer> root)
    {
        if (root==null)
            return null;
        if (root.data==value)
            return root;

        BinaryNode<Integer> curr = getNode(value,root.left);
        if (curr!=null)
            return curr;

        return getNode(value,root.right);

    }

    public static void test9()
    {

        BinaryNode<Integer> root = BinaryNode.getBinaryValueTree1();
        populateParent(root,null);


        BinaryNode<Integer> current = getNode(314,root);

        System.out.println(current);

        System.out.println(findSuccessor(root, current));


    }



    private static BinaryNode<Integer> findSuccessor(BinaryNode<Integer> root, BinaryNode<Integer> node)
    {
            if (node.right!=null)
            {
                node = node.right;
                while(true)
                {
                    if (node.left!=null)
                        node = node.left;
                    else
                        break;
                }

                return node;
            }

            while(node!=null)
            {
                BinaryNode<Integer> parent = node.parent;
                if (node.parent.left==node)
                    return node.parent;
                else
                {
                    node = node.parent;
                }
            }

            return null;
    }


    public static void test8()
    {
        BinaryNode<Integer> root = BinaryNode.getBinaryValueTree1();
       computeSubTreeSize(root);

       findKthNode(9,root);

      // System.out.println(size);
    }

    private static void findKthNode(int num, BinaryNode<Integer> root)
    {
        if (num > root.subtreeSize)
        {
            System.out.println("Number greater than tree size");
            return ;
        }



        // is number in left subtree
        if (root.left!=null && num <= root.left.subtreeSize)
        {
             findKthNode(num,root.left);
             return;

        }

        // is it the current node
        if (root.left!=null)
        {
            num = num-root.left.subtreeSize;
        }

        num = num -1 ; // subtract self


        if (num==0)
        {
            System.out.println("Kth node is " + root);
            return;
        }

        if (root.right!=null)
        {
            findKthNode(num,root.right);
            return;
        }


    }

    public static int computeSubTreeSize(BinaryNode<Integer> root)
    {
        if (root==null)
            return 0;

        root.subtreeSize= 1+ computeSubTreeSize(root.left) + computeSubTreeSize(root.right);

        return root.subtreeSize;

    }


    public static List<BinaryNode<Integer>> getLeaves(BinaryNode<Integer> root)
    {
       // BinaryNode<Integer> root = BinaryNode.getBinaryValueTree1();


        List<BinaryNode<Integer>> leaves = new ArrayList<>();

        getLeaves(root,leaves);

        return leaves;
    }

    private static void getLeaves( BinaryNode<Integer> root , List<BinaryNode<Integer>> leaves ) {

        if (root==null)
            return;

        if (root.left == null && root.right == null) {
            leaves.add(root);
            return;
        }

        getLeaves(root.left, leaves);
        getLeaves(root.right,leaves);
    }

    static class Balanced
    {

        @Override
        public String toString() {
            return "Balanced{" +
                    "balanced=" + balanced +
                    ", height=" + height +
                    '}';
        }

        boolean balanced=false;
        int height;
    }

    private static Balanced isBalanced(BinaryNode<Integer> root)
    {
        Balanced balanced = new Balanced();
        if (root.left==null && root.right==null) // leaf
        {
            balanced.balanced=true;
            balanced.height=1;
            return balanced;
        }

        if (root.right==null || root.left==null)
        {
            balanced.balanced=false;
            return balanced;
        }

        Balanced leftBalanced ;

            leftBalanced = isBalanced(root.left);
            if (!leftBalanced.balanced)
            {
                balanced.balanced=false;
                return balanced;
            }


        Balanced rightBalanced ;

            rightBalanced=isBalanced(root.right);
            if (!rightBalanced.balanced)
            {
                balanced.balanced=false;
                return balanced;
            }


            if (rightBalanced.height==leftBalanced.height)
            {
                balanced.balanced=true;
                balanced.height = rightBalanced.height+1;
                return balanced;
            }
            else
            {
                balanced.balanced=false;
                return balanced;
            }



    }

    public static void test7() {
        BinaryNode<Integer> root = BinaryNode.getBinaryValueTree2();

        System.out.println(isBalanced(root));

    }




}
