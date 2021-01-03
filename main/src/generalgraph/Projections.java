package generalgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Projections {

    Graph graph;

    public Projections(Graph graph) {
        this.graph = graph;
    }


    public Graph personProjection()
    {

        Graph personGraph = new Graph();
        List<Vertex> shows = graph.getVertices("show");

        shows.stream().flatMap(s->{

            List<Edge> edges = s.edgeTypes.get("watchedBy");
            if (edges!=null)
                return edges.stream();
            else
                return new ArrayList<Edge>().stream();

        }).map(e->e.to).distinct().forEach(v->personGraph.addVertex(v));

        shows.stream().forEach(s->{


            List<Edge> patrons = s.edgeTypes.get("watchedBy");

            if (patrons==null) return;

            for (int i=0;i<patrons.size();i++)
            {
                for (int j=0;j<patrons.size();j++)
                {
                    if (i!=j)
                    {
                        Vertex from = patrons.get(i).to;
                        Vertex to = patrons.get(j).to;
                        personGraph.addEdge(new Edge("showgroup",from,to));
                    }
                }
            }


        });


        /*
        persons.stream().filter(p->{
            if(p.edgeTypes.get("watches")!=null)
                return true;
            else
                return false;
        }).forEach(p->personGraph.addVertex(p));

        Set<Vertex> processed = new HashSet<>();
        personGraph.vertices.values().stream().forEach(p->{

            List<Edge> shows = p.edgeTypes.get("watches");
        });
*/


        return personGraph;



    }


    /*
     public Graph personProjection()
    {

        Graph personGraph = new Graph();
        List<Vertex> persons = graph.getVertices("person");

        persons.stream().filter(p->{
            if(p.edgeTypes.get("watches")!=null)
                return true;
            else
                return false;
        }).forEach(p->personGraph.addVertex(p));

        Set<Vertex> processed = new HashSet<>();
        personGraph.vertices.values().stream().forEach(p->{

            List<Edge> shows = p.edgeTypes.get("watches");
            shows.stream().forEach(s->{

                if (processed.contains(s.to))
                    return;

                List<Edge> patrons = s.to.edgeTypes.get("watchedBy");

                for (int i=0;i<patrons.size();i++)
                {
                    for (int j=0;j<patrons.size();j++)
                    {
                        if (i!=j)
                        {
                            Vertex from = patrons.get(i).to;
                            Vertex to = patrons.get(j).to;
                            personGraph.addEdge(new Edge("showgroup",from,to));
                        }
                    }
                }

                processed.add(s.to);
            });
        });

        return personGraph;



    }
     */


    public Graph showProjection()
    {
        Graph showGraph = new Graph();
        List<Vertex> persons = graph.getVertices("person");

        persons.stream()
        .filter(p->{if (p.edgeTypes.get("watches")!=null)
            return true;
        else
            return false;
        })
        .flatMap(p-> p.edgeTypes.get("watches").stream())
        .forEach(e->showGraph.addVertex(e.to));

        persons.stream()
                .filter(p->{if (p.edgeTypes.get("watches")!=null)
                    return true;
                else
                    return false;
                })
                .forEach(p->{
                    List<Edge> shows = p.edgeTypes.get("watches");
                    for (int i=0;i<shows.size();i++)
                    {
                        for (int j=0;j<shows.size();j++)
                        {
                            if (i!=j)
                            {
                                showGraph.addEdge(new Edge("audience",shows.get(i).to, shows.get(j).to));
                            }
                        }
                    }
                });

        return showGraph;
    }
}
