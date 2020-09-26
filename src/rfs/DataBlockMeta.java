package rfs;

import java891011121314.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DataBlockMeta {


    long dataBlockLocation = -1; // not yet assigned a block

    int lengthDataBlock = -1;

    public long getDataBlockLocation() {
        return dataBlockLocation;
    }

    public void setDataBlockLocation(long dataBlockLocation) {
        this.dataBlockLocation = dataBlockLocation;
    }

    public int getLengthDataBlock() {
        return lengthDataBlock;
    }

    public void setLengthDataBlock(int lengthDataBlock) {
        this.lengthDataBlock = lengthDataBlock;
    }
}
