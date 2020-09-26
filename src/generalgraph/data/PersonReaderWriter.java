package generalgraph.data;

import generalgraph.Edge;
import generalgraph.Graph;
import generalgraph.Vertex;

import java891011121314.JSONUtil;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PersonReaderWriter {


    static class Person
    {

        String id;
        int height;
        int weight;

         String type = "Person";

        public  String getType() {
            return type;
        }

        public  void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }


    }


    static class Device
    {

        String type = "Device";

        String id ;
        String brand;
        int model;

        public Device(String id, String brand, int model) {
            this.id = id;
            this.brand = brand;
            this.model = model;
        }

        public Device()
        {

        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public int getModel() {
            return model;
        }

        public void setModel(int model) {
            this.model = model;
        }

        final static String[] brands = {"iphone","samsung","google"};

    }


    static class Car
    {
        String type = "Car";

        String id ;
        String brand;

        final static String[] brands = {"ford","acura","tesla"};

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
    }

    static class Show
    {

        String type = "Show";

        String id ;

        String genre;

        int year;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }


        final static String[] genres = { "Thriller" , "Comedy" , "War"};
    }


    public static void carDataGenerator() throws Exception
    {

        File file = new File("/home/manoj/data/epi/generalgraph/car.data");
        Random random = new Random();

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < 10; i++) {
                Car car = new Car();

                car.brand = Car.brands[random.nextInt(3)];
                car.id="C"+i;

                String str = JSONUtil.toJSON(car);

                System.out.println(str);

                bufferedWriter.write(str);
                bufferedWriter.newLine();

            }

            bufferedWriter.flush();
        }
    }






    public static void deviceDataGenerator() throws Exception
    {

        File file = new File("/home/manoj/data/epi/generalgraph/device.data");
        Random random = new Random();

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < 10; i++) {
                Device device = new Device();

                device.brand = Device.brands[random.nextInt(3)];
                device.model=random.nextInt(12);
                device.id="D"+i;

                String str = JSONUtil.toJSON(device);

                System.out.println(str);

                bufferedWriter.write(str);
                bufferedWriter.newLine();

            }

            bufferedWriter.flush();
        }
    }




    public static void personDataReader(Graph graph) throws Exception
    {
        File file = new File("/home/manoj/data/epi/generalgraph/person.data");
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line = bufferedReader.readLine();
            while(line!=null)
            {
                Person person = (Person)JSONUtil.fromJSON(line,Person.class);

                Vertex vertex = new Vertex(person.type,person.id);
                vertex.addAttribute("height",String.valueOf(person.height));
                vertex.addAttribute("weight",String.valueOf(person.weight));

                graph.addVertex(vertex);

                line = bufferedReader.readLine();
            }

        }

    }

    public static void deviceDataReader(Graph graph) throws Exception
    {
        File file = new File("/home/manoj/data/epi/generalgraph/device.data");
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line = bufferedReader.readLine();
            while(line!=null)
            {
                Device device = (Device)JSONUtil.fromJSON(line,Device.class);

                Vertex vertex = new Vertex(device.type,device.id);
                vertex.addAttribute("brand",String.valueOf(device.brand));
                vertex.addAttribute("model",String.valueOf(device.model));

                graph.addVertex(vertex);

                line = bufferedReader.readLine();
            }

        }

    }

    public static void carDataReader(Graph graph) throws Exception
    {
        File file = new File("/home/manoj/data/epi/generalgraph/car.data");
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line = bufferedReader.readLine();
            while(line!=null)
            {
                Car car = (Car)JSONUtil.fromJSON(line,Car.class);

                Vertex vertex = new Vertex(car.type,car.id);
                vertex.addAttribute("brand",String.valueOf(car.brand));

                graph.addVertex(vertex);

                line = bufferedReader.readLine();
            }

        }

    }


    public static void showDataReader(Graph graph) throws Exception
    {
        File file = new File("/home/manoj/data/epi/generalgraph/show.data");
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line = bufferedReader.readLine();
            while(line!=null)
            {
                Show show = (Show)JSONUtil.fromJSON(line,Show.class);

                Vertex vertex = new Vertex(show.type,show.id);
                vertex.addAttribute("genre",String.valueOf(show.genre));

                graph.addVertex(vertex);

                line = bufferedReader.readLine();
            }

        }

    }

    public static void householdReader(Graph graph) throws Exception
    {
        File file = new File("/home/manoj/data/epi/generalgraph/household.data");
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line = bufferedReader.readLine();
            while(line!=null)
            {
                String[] strs = line.split(" ");

                List<Vertex> persons = Arrays.stream(strs).map(str->graph.getVertex(str)).collect(Collectors.toList());

                for(int i=0;i<persons.size();i++)
                {
                    for (int j=0;j<persons.size();j++)
                    {
                        if (i!=j) // no self relationship
                        {
                            Vertex from = persons.get(i);
                            Vertex to = persons.get(j);
                            Edge edge = new Edge("household",from,to);
                            graph.addEdge(edge);
                        }
                    }
                }



                line = bufferedReader.readLine();
            }

        }

    }

   public static void personDeviceOrCarReader(Graph graph,File file) throws Exception
    {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line = bufferedReader.readLine();
            while(line!=null)
            {
                String[] strs = line.split(" ");
                // 0 is person , others are devices . person owns devices , devices are owned by persons . no relationship between devices.
                List<Vertex> vertices = Arrays.stream(strs).map(str->graph.getVertex(str)).collect(Collectors.toList());

                Vertex person = vertices.get(0);

                for(int i=1;i<vertices.size();i++)
                {

                    Vertex from = person;

                    Vertex to = vertices.get(i);

                    Edge edge = new Edge("owns",from,to);
                    graph.addEdge(edge);

                     edge = new Edge("ownedBy",to,from);
                    graph.addEdge(edge);


                }



                line = bufferedReader.readLine();
            }

        }

    }


    public static void personShowReader(Graph graph) throws Exception
    {
        File file = new File("/home/manoj/data/epi/generalgraph/personshow.data");

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line = bufferedReader.readLine();
            while(line!=null)
            {
                String[] strs = line.split(" ");
                // 0 is person , others are devices . person owns devices , devices are owned by persons . no relationship between devices.
                List<Vertex> vertices = Arrays.stream(strs).map(str->graph.getVertex(str)).collect(Collectors.toList());

                Vertex person = vertices.get(0);

                for(int i=1;i<vertices.size();i++)
                {

                    Vertex from = person;

                    Vertex to = vertices.get(i);

                    Edge edge = new Edge("watches",from,to);
                    graph.addEdge(edge);

                    edge = new Edge("watchedBy",to,from);
                    graph.addEdge(edge);


                }



                line = bufferedReader.readLine();
            }

        }

    }




    public static void personDataGenerator() throws Exception
    {

        File file = new File("/home/manoj/data/epi/generalgraph/person.data");
        Random random = new Random();

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < 10; i++) {
                Person person = new Person();

                person.height = random.nextInt(60) + 24;
                person.weight = random.nextInt(300);
                person.id = "P" + i;

                String str = JSONUtil.toJSON(person);

                System.out.println(str);

                bufferedWriter.write(str);
                bufferedWriter.newLine();

            }

            bufferedWriter.flush();
        }
    }

    public static void showDataGenerator() throws Exception
    {

        File file = new File("/home/manoj/data/epi/generalgraph/show.data");
        Random random = new Random();

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < 10; i++) {
                Show show = new Show();

                show.genre = Show.genres[random.nextInt(3)];
                show.id="S" + i;
                show.year = 2000+ random.nextInt(21);


                String str = JSONUtil.toJSON(show);

                System.out.println(str);

                bufferedWriter.write(str);
                bufferedWriter.newLine();

            }

            bufferedWriter.flush();
        }
    }



    public static void main(String[] args) throws Exception {


       // personDataGenerator();

       // deviceDataGenerator();

       // carDataGenerator();

       // showDataGenerator();

    }

}
