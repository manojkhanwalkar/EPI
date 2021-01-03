package ej.mr;

import java.util.ArrayList;
import java.util.List;

public class CHFGVAPReducer implements Reducer<String,String> {


    @Override
    public void reduce(String s, List<String> values, collector<String, String> collector) {


        String line = values.get(0);
        if (hasAlphabet(line))
        {
            String[] str = line.split(" ");
            if (str.length>2) {
                String fname = str[str.length - 2];
                String lname = str[str.length - 1];

                List<String> pans = new ArrayList<>();
                for (int i=1;i<str.length-2;i+=2)
                {
                    if (str[i].trim().equals(""))
                        continue;
                    pans.add(str[i]);
                }

                for (int j=0;j<pans.size();j++)
                {
                    collector.collect(new pair(s, fname+" " + lname+" " + pans.get(j)));
                }
            }

        }




    }

    public static boolean hasAlphabet(String str)
    {
        if (str == null || str.equals("")) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (((ch >= 'A' && ch <= 'Z'))
                    || ((ch >= 'a' && ch <= 'z'))) {
                return true;
            }
        }
        return false;
    }


}
