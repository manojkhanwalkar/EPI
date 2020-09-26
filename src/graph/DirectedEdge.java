package graph;

public class DirectedEdge implements Comparable<DirectedEdge>, Edge {
    int v, w, weight;

    public DirectedEdge(int v, int w, int weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public DirectedEdge(String v, String w, String weight) {
        this.v = Integer.valueOf(v);
        this.w = Integer.valueOf(w);
        this.weight = Integer.valueOf(weight);
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(DirectedEdge directedEdge) {
        return Integer.compare(this.weight, directedEdge.weight);
    }
}
