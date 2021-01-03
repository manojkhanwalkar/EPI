package generalgraph;

import java.io.File;

import static generalgraph.data.PersonReaderWriter.*;

public class GraphTester {


    public static void main(String[] args) throws Exception  {



        Graph graph = new Graph();

        personDataReader(graph);

        //householdReader(graph);

      //  deviceDataReader(graph);

      //  carDataReader(graph);

        showDataReader(graph);

        File file = new File("/home/manoj/data/epi/generalgraph/persondevice.data");

       // personDeviceOrCarReader(graph,file);

         file = new File("/home/manoj/data/epi/generalgraph/personcar.data");

      //  personDeviceOrCarReader(graph,file);

        personShowReader(graph);


       // System.out.println(graph);


        Queries queries = new Queries(graph);

     /*   System.out.println(queries.allPersonsWhoOwnaPhoneBrad("Person","owns", "device","iphone"));

        System.out.println(queries.allPersonsWhoOwnaPhoneBrad("Person","owns", "device","samsung"));

        System.out.println(queries.allPersonsWhoOwnaPhoneBrad("Person","owns", "device","noogle"));*/

/*
        System.out.println(queries.allPersonsNotowningACar());
        System.out.println(queries.allPersonsOwningMoreThanOneCar());
        System.out.println(queries.allPersonsOwningABrand("chevy"));*/


       // System.out.println(queries.allPersonsOwningCarAndPhone("ford" , "iphone"));

       // System.out.println(queries.totalCarsInPopulation("acura"));

       // System.out.println(queries.numberOfHouseholds());

        Projections projections = new Projections(graph);

       // System.out.println(projections.personProjection());

        System.out.println(projections.showProjection());

    }
}
