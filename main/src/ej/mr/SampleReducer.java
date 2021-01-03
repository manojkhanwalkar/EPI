package ej.mr;

import java.util.List;

public class SampleReducer implements Reducer<String,Integer> {


    @Override
    public void reduce(String s, List<Integer> values, collector<String, Integer> collector) {


        collector.collect(new pair(s,values.size()));

    }
}
