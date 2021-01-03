package ej.mr;

public class SampleMapper implements Mapper<String,Integer> {

    @Override
    public void map(String input, collector<String, Integer> collector) {

        if (input==null || input.length()==0)
            return ;
        String s =input.substring(0,1);

        Integer count = 1;

        collector.collect(new pair<>(s,count));


    }
}
