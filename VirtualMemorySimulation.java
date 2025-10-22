import java.util.ArrayList;
public class VirtualMemorySimulation {
    public static void main(String[] args) {


        //Declare variables to hold each token passed by command line
        int cache_size=0;
        int block_size=0;
        int associativity=0;
        String replacement_policy = new String();
        int physical_memory=0;
        int used_memory=0;
        int instructions=0;
        ArrayList<String> files = new ArrayList<>();

        //Parse tokens
        for(int i = 0; i < args.length; i = i + 2)
        {
            switch(args[i]) {
                case "–f":
                case "-f":
                    files.add(args[i + 1]);
                    break;
                case "–s":
                case "-s":
                    cache_size = Integer.parseInt(args[i + 1]);
                    break;
                case "–b":
                case "-b":
                    block_size = Integer.parseInt(args[i + 1]);
                    break;
                case "–a":
                case "-a":
                    associativity = Integer.parseInt(args[i + 1]);
                    break;
                case "–r":
                case "-r":
                    replacement_policy = args[i + 1];
                    break;
                case "–p":
                case "-p":
                    physical_memory = Integer.parseInt(args[i + 1]);
                    break;
                case "–u":
                case "-u":
                    used_memory = Integer.parseInt(args[i + 1]);
                    break;
                case "–n":
                case "-n":
                    instructions = Integer.parseInt(args[i+1]);
                    break;
            }
        }
        

        //Print header
        System.out.printf("Cache Simulator - CS 3853 – Team #15\n\n");
        System.out.printf("Trace File(s):\n");
        
        //Print list of trace files
        for(String file: files) {
            System.out.printf("\t%s\n",file);
        }

        //Print Input Parameters
        System.out.printf("\n***** Cache Input Parameters *****\n\n");
        System.out.printf("%-30s %d KB\n", "Cache Size:", cache_size);
        System.out.printf("%-30s %d bytes\n", "Block Size:",  block_size);
        System.out.printf("%-30s %d\n", "Associativity:",  associativity);
        System.out.printf("%-30s %s\n", "Replacement Policy:",  replacement_policy);
        System.out.printf("%-30s %d MB\n", "Physical Memory:", physical_memory);
        System.out.printf("%-30s %d%%\n", "Percent Memory Used by System:", used_memory); 
        System.out.printf("%-30s %d\n", "Instructions / Time Slice:", instructions);
        
    }
}

