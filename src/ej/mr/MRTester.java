package ej.mr;

public class MRTester {


    public static void main(String[] args) throws Exception {

   /*     {

            MRJob job = new MRJob("First", "ej.mr.SampleMapper1", "ej.mr.SampleReducer", "/home/manoj/data/words1.txt");

            MRFramework framework = new MRFramework();

            framework.submit(job);

        }*/

    {

        String[] inputFiles = {"/home/manoj/data/chfgvap/chf0","/home/manoj/data/chfgvap/gvap1"};

        MRJob job = new MRJob("First" , "ej.mr.CHFGVAPMapper" , "ej.mr.CHFGVAPReducer" , inputFiles);

        MRFramework framework = new MRFramework();

        framework.submit(job);

    }



    }

}
