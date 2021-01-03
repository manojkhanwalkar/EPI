package tree;

public class BSTNode<T> extends BinaryNode<T> {




    public BSTNode(T data)
    {
        super(data);
    }

    @Override
    public String toString() {
        return "BSTNode{" +
                "data=" + data +
                '}';
    }
}
