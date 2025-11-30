import java.util.HashMap;
import java.lang.Math;

public class Cache {

    //Cache properties
    private int ass; //Associativity, I just don't like having to type it all out all the time
    private int index_size;
    private int tag_size;
    private int offset_bytes;

    //Hashmap for storing addresses to access
    private HashMap<Integer, Row> cache_mappings;
    
    //Metadata for cache
    private int cache_hits;
    private int cache_comp_misses;
    private int cache_conf_misses;
    

    public Cache(int ass, int index_size, int tag_size, int offset_bytes) {
        //Initialize passed values
        this.ass = ass;
        this.index_size = index_size;
        this.tag_size = tag_size;
        this.offset_bytes = offset_bytes;

        //Initialize HashMap
        cache_mappings = new HashMap<Integer, Row>();

        //Initialize metadata
        cache_hits = 0;
        cache_comp_misses = 0;
        cache_conf_misses = 0;
    }

    public void access(int address) {
        //Temp values -- the tag to be accessed, and the index to be accessed
        int tag = address >>>(index_size + offset_bytes);
        int index = (address >>> offset_bytes) & ~((int)0x7FFFFFFF<<index_size);
        System.out.printf("Address : %d\tTag: %d\t Index: %d\n", address, tag, index);
        //Result of cache access. If -1, tag was found. If 0, compulsory miss. If not 0, conflict miss
        int success = 0;
        if(!cache_mappings.containsKey(index)) {
            Row temp = new Row(ass);
            cache_mappings.put(index,temp);
            cache_comp_misses++;
        }
        success = cache_mappings.get(index).access(tag);
        if(success == -1) cache_hits++;
        if(success > 0) cache_conf_misses++;
    }

    public int get_hits() {
        return cache_hits;
    }
    
    public int get_comp_misses() {
        return cache_comp_misses;
    }

    public int get_conf_misses() {
        return cache_conf_misses;
    }
}

