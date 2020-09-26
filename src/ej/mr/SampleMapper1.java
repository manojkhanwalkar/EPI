package ej.mr;

public class SampleMapper1 implements Mapper<String,Integer> {

    @Override
    public void map(String input, collector<String, Integer> collector) {

        if (input==null || input.length()==0)
            return ;
        String s =(input.length()>=5) ? input.substring(0,5) : input;

        Integer count = 1;

        collector.collect(new pair<>(s,count));


    }
}
