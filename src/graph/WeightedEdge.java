package graph;

public class WeightedEdge implements Comparable<WeightedEdge> , Edge {

    int v;
    int w;
    int weight;

    @Override
    public int compareTo(WeightedEdge edge) {
        return Integer.compare(this.weight,edge.weight);
    }

    public WeightedEdge(int v, int w, int weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public WeightedEdge(String v, String w, String weight) {
        this.v = Integer.valueOf(v);
        this.w = Integer.valueOf(w);
        this.weight = Integer.valueOf(weight);
    }

    public int from()
    {
        return v;
    }

    public int to()
    {
        return w;
    }


    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "WeightedEdge{" +
                "v=" + v +
                ", w=" + w +
                ", weight=" + weight +
                '}';
    }
}
