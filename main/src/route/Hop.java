package route;

import java.util.Objects;

public class Hop {

    int start;

    int end ;

    int weight;

    public Hop(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public Hop(int start, int end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hop hop = (Hop) o;
        return start == hop.start &&
                end == hop.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "Hop{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}

