package ej.mr;

public class CHFGVAPMapper implements Mapper<String,String> {

    @Override
    public void map(String input, collector<String, String> collector) {

        if (input==null || input.length()==0)
            return ;

        String[] strs = input.split(" ");

        String remaining = "";

        for (int i=1;i<strs.length;i++)
        {
            String trim = strs[i].trim();
            if (trim.equals(""))
                continue;
            remaining += strs[i].trim() + " ";
        }


     //   System.out.println(strs[0] + " "  + remaining);
        collector.collect(new pair<>(strs[0],remaining));


    }
}
