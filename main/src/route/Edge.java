package route;

import java.util.Objects;

public class Edge {

    Node start;

    Node end ;

    int weight=1;

    public Edge(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "start=" + start.name +
                ", end=" + end.name +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return start.equals(edge.start) &&
                end.equals(edge.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}

