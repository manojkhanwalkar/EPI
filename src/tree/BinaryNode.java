package tree;

public class BinaryNode<T> extends TreeNode<T> {



    BinaryNode<T> left;
    BinaryNode<T> right;

    BinaryNode<T> parent;
    BinaryNode<T> sibling;


    int subtreeSize;


    public BinaryNode(T data)
    {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BinaryNode{" +
                "data=" + data +
                '}';
    }


    public static BinaryNode<Integer> getBalancedBinaryTree()
    {



        BinaryNode<Integer> A = new BinaryNode<>(1);
        BinaryNode<Integer> B = new BinaryNode<>(2);
        BinaryNode<Integer> C = new BinaryNode<>(3);
        BinaryNode<Integer> D = new BinaryNode<>(4);
        BinaryNode<Integer> E = new BinaryNode<>(5);
        BinaryNode<Integer> F = new BinaryNode<>(6);
        BinaryNode<Integer> G = new BinaryNode<>(7);
        BinaryNode<Integer> H = new BinaryNode<>(8);
        BinaryNode<Integer> I = new BinaryNode<>(9);
        BinaryNode<Integer> J = new BinaryNode<>(10);
        BinaryNode<Integer> K = new BinaryNode<>(11);
        BinaryNode<Integer> L = new BinaryNode<>(12);
        BinaryNode<Integer> M = new BinaryNode<>(13);
        BinaryNode<Integer> N = new BinaryNode<>(14);
        BinaryNode<Integer> O = new BinaryNode<>(15);
     //   BinaryNode<Integer> P = new BinaryNode<>(16);

        A.left = B;
        B.left=C;
        B.right=F;
        C.left=D;
        C.right=E;

        F.left=G;
        F.right=H;
        A.right=I;
        I.left=J;
        I.right=M;

        J.left=K;
        J.right=L;
        M.left=N;
        M.right=O;

        return A;

    }


    public static BinaryNode<Integer> getBinaryValueTree1()
    {



        BinaryNode<Integer> A = new BinaryNode<>(314);
        BinaryNode<Integer> B = new BinaryNode<>(6);
        BinaryNode<Integer> C = new BinaryNode<>(271);
        BinaryNode<Integer> D = new BinaryNode<>(28);
        BinaryNode<Integer> E = new BinaryNode<>(0);
        BinaryNode<Integer> F = new BinaryNode<>(561);
        BinaryNode<Integer> G = new BinaryNode<>(3);
        BinaryNode<Integer> H = new BinaryNode<>(17);
        BinaryNode<Integer> I = new BinaryNode<>(6);
        BinaryNode<Integer> J = new BinaryNode<>(2);
        BinaryNode<Integer> K = new BinaryNode<>(1);
        BinaryNode<Integer> L = new BinaryNode<>(401);
        BinaryNode<Integer> M = new BinaryNode<>(641);
        BinaryNode<Integer> N = new BinaryNode<>(257);
        BinaryNode<Integer> O = new BinaryNode<>(271);
        BinaryNode<Integer> P = new BinaryNode<>(28);

        A.left = B;
        B.left=C;
        B.right=F;
        C.left=D;
        C.right=E;
        F.right=G;
        G.left=H;
        A.right=I;
        I.left=J;
        I.right=O;
        J.right=K;
        K.left=L;
        K.right=N;
        L.right=M;
        O.right=P;

        return A;

    }



    public static BinaryNode<Integer> getBinaryValueTree2()
    {



        BinaryNode<Integer> A = new BinaryNode<>(314);
        BinaryNode<Integer> B = new BinaryNode<>(6);
        BinaryNode<Integer> C = new BinaryNode<>(271);
        BinaryNode<Integer> D = new BinaryNode<>(28);
        BinaryNode<Integer> E = new BinaryNode<>(0);
        BinaryNode<Integer> F = new BinaryNode<>(561);
        BinaryNode<Integer> G = new BinaryNode<>(3);
        BinaryNode<Integer> H = new BinaryNode<>(17);
        BinaryNode<Integer> I = new BinaryNode<>(6);
        BinaryNode<Integer> J = new BinaryNode<>(2);
        BinaryNode<Integer> K = new BinaryNode<>(1);
        BinaryNode<Integer> L = new BinaryNode<>(401);
        BinaryNode<Integer> M = new BinaryNode<>(641);
        BinaryNode<Integer> N = new BinaryNode<>(257);
        BinaryNode<Integer> O = new BinaryNode<>(271);
        BinaryNode<Integer> P = new BinaryNode<>(28);

        A.left = B;
        A.right=I;


        B.left=C;
        B.right=F;


        I.left=J;
        I.right=O;

        C.left=D;
        C.right=E;
     /*   F.right=G;
        G.left=H;


        J.right=K;
        K.left=L;
        K.right=N;
        L.right=M;
        O.right=P;*/

        return A;

    }





    public static BinaryNode<Integer> getBinaryValueTree()
    {
        final int ONE = 1 ;
        final int ZERO = 0;


        BinaryNode<Integer> A = new BinaryNode<>(1);
        BinaryNode<Integer> B = new BinaryNode<>(0);
        BinaryNode<Integer> C = new BinaryNode<>(0);
        BinaryNode<Integer> D = new BinaryNode<>(0);
        BinaryNode<Integer> E = new BinaryNode<>(1);
        BinaryNode<Integer> F = new BinaryNode<>(1);
        BinaryNode<Integer> G = new BinaryNode<>(1);
        BinaryNode<Integer> H = new BinaryNode<>(0);
        BinaryNode<Integer> I = new BinaryNode<>(1);
        BinaryNode<Integer> J = new BinaryNode<>(0);
        BinaryNode<Integer> K = new BinaryNode<>(0);
        BinaryNode<Integer> L = new BinaryNode<>(1);
        BinaryNode<Integer> M = new BinaryNode<>(1);
        BinaryNode<Integer> N = new BinaryNode<>(0);
        BinaryNode<Integer> O = new BinaryNode<>(0);
        BinaryNode<Integer> P = new BinaryNode<>(0);

        A.left = B;
        B.left=C;
        B.right=F;
        C.left=D;
        C.right=E;
        F.right=G;
        G.left=H;
       A.right=I;
        I.left=J;
        I.right=O;
        J.right=K;
        K.left=L;
        K.right=N;
        L.right=M;
        O.right=P;

        return A;

    }
}
