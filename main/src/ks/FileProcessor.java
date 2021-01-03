package ks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class FileProcessor {


    KSGraph graph ;
    public FileProcessor(KSGraph graph)
    {
        this.graph = graph;
    }

    public void processPersons()
    {
        try {
            File file = new File("/home/manoj/data/epi/ksgraph/person1.data");

            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String str = reader.readLine();
                while (str != null) {

                    String[] persons1 = str.split(" ");
                    String from = persons1[0];
                    String[] tos = persons1[1].split(",");
                    Arrays.stream(tos).forEach(to -> graph.addPersonPersonRelation(from, to));
                    str = reader.readLine();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void processWords()
    {
        try {
            File file = new File("/home/manoj/data/epi/ksgraph/word.data");

            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String str = reader.readLine();
                while (str != null) {

                    String[] persons1 = str.split(" ");
                    String from = persons1[0];
                    String[] words = persons1[1].split(",");
                    Arrays.stream(words).forEach(to -> graph.addPersonWordRelationship(from, to));
                    str = reader.readLine();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
