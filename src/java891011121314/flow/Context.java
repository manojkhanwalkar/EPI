package java891011121314.flow;

public class Context {

    public enum Type { Order,Execution}

    public enum Operation { Add, Modify, Delete}

    Type type;
    Operation operation;

    Object object;


    @Override
    public String toString() {
        return "Context{" +
                "type=" + type +
                ", operation=" + operation +
                ", object=" + object +
                '}';
    }


}
