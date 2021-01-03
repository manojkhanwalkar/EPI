package graph;

import java.util.List;

public interface Graph {

     int getVertices();

     default List<Edge>[] getAdjacent()
     {
          throw new UnsupportedOperationException();
     }

     Graph reverse();

     Graph negateWeight();




}
