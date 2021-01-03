package route;

import java.util.*;

public class Node {

    int name;

    public Node(int name) {
        this.name = name;

        Hop self = new Hop(name,name,0);

        routes.add(self);
    }

    List<Edge> edges = new ArrayList<>();

    public void add(Edge edge)
    {
        edges.add(edge);
    }


    Set<Hop> routes = new HashSet<>();

    public void addRoutes(Set<Hop> other)
    {
        // enables any weight updates in future by removing the older route

        other.stream().forEach(or->{
            routes.remove(or);
            routes.add(or);
        });

    }

    public void calculateRoutes()
    {
        // find the edges , and for each edge create a hop with weight 1 .
        // for all the nodes it has edges to send the hops it has computed.

        Set<Hop> hops = new HashSet<>();
        edges.stream().forEach(edge->{

            Hop hop = new Hop(edge.start.name, edge.end.name);
            hops.add(hop);

        });

        addRoutes(hops);
        edges.stream().forEach(edge->edge.end.addRoutes(routes));

    }


    static class RouteHopTuple
    {
        Hop current;
        List<Integer> route = new ArrayList<>();

        @Override
        public String toString() {
            return "RouteHopTuple{" +
                    "route=" + route +
                    '}';
        }
    }
    public Optional<RouteHopTuple> printRoute(int end)
    {
        Queue<RouteHopTuple> queue = new ArrayDeque<>();

        Set<Integer> seen = new HashSet<>();

        int currName = name;


            routes.stream().filter(h -> h.start == currName).map(h -> {
                RouteHopTuple routeHopTuple = new RouteHopTuple();
                routeHopTuple.current = h;
                routeHopTuple.route.add(h.start);
                return routeHopTuple;
            }).forEach(queue::add);


            while(!queue.isEmpty())
            {
                var rht = queue.remove();

                 if (seen.contains(rht.current.start))
                 {
                     continue;
                 }
                 else
                 {
                   //  seen.add(rht.current.start);
                 }
                int start = rht.current.end;
                if (start==end)
                {
                    rht.route.add(start);
                    return Optional.of(rht);
                }

                routes.stream().filter(h -> h.start == start).map(h -> {
                    RouteHopTuple routeHopTuple = new RouteHopTuple();
                    routeHopTuple.current = h;
                    routeHopTuple.route.addAll(rht.route);
                    routeHopTuple.route.add(h.start);
                    return routeHopTuple;
                }).forEach(queue::add);




            }

            return Optional.empty();


    }

    @Override
    public String toString() {
        return "Node{" +
                "name=" + name +
                ", edges=" + edges +
                ", routes=" + routes +
                '}';
    }
}
