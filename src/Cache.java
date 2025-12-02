import java.util.HashMap;
import java.lang.Math;

public class Cache {

    //Cache properties
    private long ass; //Associativity, I just don't like having to type it all out all the time
    private long index_size;
    private long tag_size;
    private long offset_bytes;

    //Hashmap for storing addresses to access
    private HashMap<Long, Row> cache_mappings;
    
    //Metadata for cache
    private long cache_hits;
    private long cache_comp_misses;
    private long cache_conf_misses;
    

    public Cache(long ass, long index_size, long tag_size, long offset_bytes) {
        //Initialize passed values
        this.ass = ass;
        this.index_size = index_size;
        this.tag_size = tag_size;
        this.offset_bytes = offset_bytes;

        //Initialize HashMap
        cache_mappings = new HashMap<Long, Row>();

        //Initialize metadata
        cache_hits = 0;
        cache_comp_misses = 0;
        cache_conf_misses = 0;
    }

    public void access(long address) {
        //Temp values -- the tag to be accessed, and the index to be accessed
        long tag = address >>>(index_size + offset_bytes);
        long index = (address -(tag<<(index_size+offset_bytes)))>>>offset_bytes;
//        System.out.printf("Address : %d\tTag: %d\t Index: %d\n", address, tag, index);
        //Result of cache access. If -1, tag was found. If 0, compulsory miss. If not 0, conflict miss
        long success = 0;
        if(!cache_mappings.containsKey(index)) {
            Row temp = new Row(ass);
            cache_mappings.put(index,temp);
            cache_comp_misses++;
        }
        success = cache_mappings.get(index).access(tag);
        if(success == -1) cache_hits++;
        if(success > 0){
            cache_conf_misses++;
//            System.out.printf("Address : %d\tTag: %d\t Index: %d\n", address, tag, index);
        }
    }

    public long get_hits() {
        return cache_hits;
    }
    
    public long get_comp_misses() {
        return cache_comp_misses;
    }

    public long get_conf_misses() {
        return cache_conf_misses;
    }
}

