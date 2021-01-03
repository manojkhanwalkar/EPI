package aero;

import java.util.HashMap;
import java.util.Map;

public class Index {

    static final long InvalidLocation = -1 ;



    static class IndexTuple {

        long location;
        boolean marker = false;

        @Override
        public String toString() {
            return "IndexTuple{" +
                    "location=" + location +
                    ", marker=" + marker +
                    '}';
        }

        public IndexTuple(long location) {
            this.location = location;
        }

        public IndexTuple(long location, boolean marker) {
            this.location = location;
            this.marker = marker;
        }

        public long getLocation() {
            return location;
        }

        public void setLocation(long location) {
            this.location = location;
        }

        public boolean isMarker() {
            return marker;
        }

        public void setMarker(boolean marker) {
            this.marker = marker;
        }
    }


    public Map<String,IndexTuple>  index = new HashMap<>();


    public IndexTuple put(String key, long location)
    {
        var tuple = new IndexTuple(location);
        index.put(key,tuple);

        return tuple;
    }

    public long get(String key)
    {
        IndexTuple tuple = index.get(key);
        if (tuple!=null && !tuple.marker)
            return tuple.location;
        else
            return Index.InvalidLocation;

    }

    public IndexTuple delete(String key)
    {
        IndexTuple tuple = index.get(key);
        if (tuple!=null)
        {
            tuple.marker=true;
        }

        return tuple;

    }


    //TODO - persist and recover the index to a file . async persistence.
}
