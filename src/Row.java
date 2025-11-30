import java.lang.Math;

public class Row {
    static private int policy = 0;  //Replacement policy - 0 is random, 1 is RR
    private int valid;              //Number of valid memory addresses in cache
    private int[] tags;             //Array of tags

    public Row(int ass) {
        valid = 0;
        tags = new int[ass];
    }

    public int access(int tag) {
        if (valid == 0) {
            tags[0] = tag;
            valid ++;
            return 0;
        }
        for(int i = 0; i < valid; i++) {
            if (tags[i] == tag) {
                return -1;
            }
        }
       if (valid < tags.length){
            tags[valid] = tag;
            valid++;
            return valid;
        }
        if(policy == 0) {
            tags[(int)(Math.random() *valid)] = tag;
//            System.out.printf("FFFFF %d", valid);
        }
        return valid;
    }
}
