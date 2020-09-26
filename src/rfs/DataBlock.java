package rfs;

import java.util.ArrayList;
import java.util.List;

// variable length of data block contents .
public class DataBlock {

    String content;
    int length;

    private DataBlock(String content)
    {
        this.content = content;
        length = content.length();
    }

    public static List<DataBlock>  getBlocks(List<String> contents)
    {

        List<DataBlock> blocks = new ArrayList<>();


        for (int i=0;i<contents.size();i++)
        {
            String s = contents.get(i);

            blocks.add(new DataBlock(s));

        }

        return blocks;

    }


}
