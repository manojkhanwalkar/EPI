package generalgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Queries {

    Graph graph;

    public Queries(Graph graph) {
        this.graph = graph;
    }

    public List<String> allPersonsWhoOwnaPhoneBrad(String srcVertexType, String edgeType , String targetVertexType , String brand)
    {

        List<String> personNames = new ArrayList<>();
        List<Vertex> persons = graph.getVertices(srcVertexType);

        persons.stream().forEach(person->{


            List<Edge> edges = person.edgeTypes.get(edgeType);

            if (edges!=null ) {
                edges.stream().forEach(edge -> {

                    if (edge.to.type.equalsIgnoreCase(targetVertexType) && edge.to.attributes.get("brand").equalsIgnoreCase(brand)) {
                        personNames.add(person.id);

                    }

                });

            }

        });

        return personNames;

    }


    // persons not owning a car

    public List<String> allPersonsNotowningACar()
    {

        Set<String> personNamesWithCar = new HashSet<>();
        List<Vertex> persons = graph.getVertices("Person");


        persons.stream().forEach(person->{


            List<Edge> edges = person.edgeTypes.get("owns");

            if (edges!=null ) {
                edges.stream().forEach(edge -> {

                    if (edge.to.type.equalsIgnoreCase("car")) {
                        personNamesWithCar.add(person.id);

                    }

                });

            }



        });

        return persons.stream().filter(p->


                {return !personNamesWithCar.contains(p.id);})


                .map(p->p.id).collect(Collectors.toList());

    }



    // persons owning more than one car

    public List<String> allPersonsOwningMoreThanOneCar()
    {

        List<String> personNamesWithCar = new ArrayList<>();

        List<Vertex> persons = graph.getVertices("Person");


        persons.stream().forEach(person->{


            List<Edge> edges = person.edgeTypes.get("owns");

            if (edges!=null ) {

                long count = edges.stream().filter((edge->edge.to.type.equalsIgnoreCase("car"))).count();
                if (count>1)
                {
                    personNamesWithCar.add(person.id);
                }

            }



        });

        return personNamesWithCar;

    }



    // persons owning a particular car brand .

    // persons owning more than one car

    public List<String> allPersonsOwningABrand(String brandName)
    {

        List<String> personNamesWithCar = new ArrayList<>();

        List<Vertex> persons = graph.getVertices("Person");


        persons.stream().forEach(person->{


            List<Edge> edges = person.edgeTypes.get("owns");

            if (edges!=null ) {

                long count = edges.stream().filter((edge->edge.to.type.equalsIgnoreCase("car")))
                        .filter(edge->edge.to.attributes.get("brand").equalsIgnoreCase(brandName)).count();
                if (count>0)
                {
                    personNamesWithCar.add(person.id);
                }

            }



        });

        return personNamesWithCar;

    }



    // persons owning a car and a phone
    public List<String> allPersonsOwningCarAndPhone(String carBrand, String phoneBrand)
    {

        List<String> personNamesWithCarAndPhone = new ArrayList<>();

        List<Vertex> persons = graph.getVertices("Person");


        persons.stream().forEach(person->{


            List<Edge> edges = person.edgeTypes.get("owns");

            long totalCount=0;

            if (edges!=null ) {

                long count1 = edges.stream().filter((edge->edge.to.type.equalsIgnoreCase("car")))
                        .filter(edge->edge.to.attributes.get("brand").equalsIgnoreCase(carBrand)).count();

                long count2 = edges.stream().filter((edge->edge.to.type.equalsIgnoreCase("device")))
                        .filter(edge->edge.to.attributes.get("brand").equalsIgnoreCase(phoneBrand)).count();

                totalCount = count1+ count2;

            }

            if (totalCount>1)
                personNamesWithCarAndPhone.add(person.id);



        });

        return personNamesWithCarAndPhone;

    }

    //total fords in a population

    // persons owning a car and a phone
    public long totalCarsInPopulation(String carBrand)
    {

        List<Vertex> persons = graph.getVertices("Person");

         return persons.stream().flatMap(person->{

             List<Edge> edges = person.edgeTypes.get("owns");
             if (edges!=null)
                 return edges.stream();
             else
                 return new ArrayList<Edge>().stream();
         }).filter((edge->edge.to.type.equalsIgnoreCase("car")))
                 .filter(edge->edge.to.attributes.get("brand").equalsIgnoreCase(carBrand)).count();


    }

    // how many households are there ?
    public long numberOfHouseholds()
    {
        Set<Vertex> processedPersons = new HashSet<>();
        List<Vertex> persons = graph.getVertices("Person");

        return persons.stream().filter(p->!processedPersons.contains(p)).map(person->{

            processedPersons.add(person);
            List<Edge> edges = person.edgeTypes.get("household");
            if (edges!=null)
                 edges.stream().forEach(edge->processedPersons.add(edge.to));
           return person;

        }).count();
    }



}
