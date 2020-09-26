package ej.mr;

public class MRJob {

    String jobName;

    String mapperName;

    String reducerName;

    String[] inputFiles;


    public MRJob(String jobName, String mapperName, String reducerName, String... inputFiles) {
        this.jobName = jobName;
        this.mapperName = mapperName;
        this.reducerName = reducerName;
        this.inputFiles = inputFiles;
    }


}
