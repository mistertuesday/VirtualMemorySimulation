import java.util.ArrayList;
import java.lang.Math;

public class VirtualMemorySimulation {
    //KB, MB for easy calculations
    static int Byte = 8;
    static int KB = 1024;
    static int MB = 1048576;

    public static void main(String[] args) {


        //Declare variables to hold each token passed by command line
        int cache_size = 0;
        int block_size = 0;
        int associativity = 0;
        String replacement_policy = new String();
        int physical_memory = 0;
        int used_memory= 0 ;
        int instructions= 0 ;
        ArrayList<String> files = new ArrayList<>();
        
        //Declare calculated variables for Cache
        int total_blocks = 0;
        int tag_size = 0;
        int index_size = 0;
        int total_rows = 0;
        int overhead_size = 0;
        int imp_mem_size = 0;
        double cost = 0;

        //Declare calcluated variables for Physical Memory
        int num_physical_pages = 0;
        int number_pages = 0;
        int pte_size = 0;
        int total_ram = 0;

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
                    if(args[i+1].equals("rr"))
                        replacement_policy = "Round Robin";
                    else
                        replacement_policy = "Random";
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

        //Calculate Cache Values
        total_blocks = calcBlocks(cache_size, block_size);
        tag_size = calcTagSize(cache_size, block_size, associativity);
        

        //Print header
        System.out.println("Cache Simulator - CS 3853 – Team #15\n");
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

        //Print Cache Calculated Values
        System.out.println("\n***** Cache Calculated Values *****\n");
        System.out.printf("%-30s %d\n", "Total # Blocks:", total_blocks); 
    }

    private static int calcBlocks(int cache_size, int block_size) {
        return (cache_size*KB)/block_size;
    }
    private static int calcTagSize(int cache_size, int block_size, int associativity)
    {
        return 0;
    }

}

