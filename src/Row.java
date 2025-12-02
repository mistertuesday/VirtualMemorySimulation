import java.lang.Math;

public class Row {
    static private long policy = 0;  //Replacement policy - 0 is random, 1 is RR
    private long valid;              //Number of valid memory addresses in cache
    private long[] tags;             //Array of tags

    public Row(long ass) {
        valid = 0;
        tags = new long[(int)ass];
    }

    public long access(long tag) {
        for(int i = 0; i < valid; i++) {
            if (tags[i] == tag) {
                return -1;
            }
        }
        if (valid == 0) {
            tags[0] = tag;
            valid ++;
            return 0;
        }
        
       if (valid < tags.length){
            tags[(int)valid] = tag;
            valid++;
            System.out.printf("CONFLICT Tag %d conflicts with %d\n",tag, tags[(int)valid-2]);
            return valid;
        }
        if(policy == 0) {
            tags[(int)(Math.random() *valid)] = tag;
//            System.out.printf("FFFFF %d", valid);
        }
        return valid;
    }
}
