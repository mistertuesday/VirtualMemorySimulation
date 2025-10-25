import java.util.ArrayList;
import java.lang.Math;

public class VirtualMemorySimulation {
    
    //KB, MB, B for easy calculations
    static int BYTE = 8;
    static int KB = 1024;
    static int MB = 1048576;

    //Price for cost calculations, per KB
    static double PRICE_PER_KB = 0.07;

    public static void main(String[] args) {


        //Declare variables to hold each token passed by command line
        int cache_size = 0;
        int block_size = 0;
        int associativity = 0;
        String replacement_policy = new String();
        int physical_memory = 0;
        int used_memory = 0 ;
        int instructions = 0 ;
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
        int num_system_pages = 0;
        int pte_size = 0;
        int total_ram = 0;
        int num_of_files = 0;

        //Parse tokens
        for(int i = 0; i < args.length; i = i + 2) {
            switch(args[i]) {
                case "-f":
                case "–f":
                case "—f":
                    files.add(args[i + 1]);
                    break;
                case "-s":
                case "–s":
                case "—s":
                    cache_size = Integer.parseInt(args[i + 1]);
                    break;
                case "-b":
                case "–b":
                case "—b":
                    block_size = Integer.parseInt(args[i + 1]);
                    break;
                case "-a":
                case "–a":
                case "—a":
                    associativity = Integer.parseInt(args[i + 1]);
                    break;
                case "-r":
                case "–r":
                case "—r":
                    if(args[i+1].equals("rr"))
                        replacement_policy = "Round Robin";
                    else
                        replacement_policy = "Random";
                    break;
                case "-p":
                case "–p":
                case "—p":
                    physical_memory = Integer.parseInt(args[i + 1]);
                    break;
                case "-u":
                case "–u":
                case "—u":
                    used_memory = Integer.parseInt(args[i + 1]);
                    break;
                case "-n":
                case "–n":
                case "—n":
                    instructions = Integer.parseInt(args[i+1]);
                    break;
            }
        }

        //Calculate Cache Values
        total_blocks = calcBlocks(cache_size, block_size);
        index_size = calcIndex(cache_size, block_size, associativity);
        tag_size = calcTagSize(physical_memory, index_size, block_size);
        total_rows = calcTotalRows(index_size);        
        overhead_size = calcOverhead(total_blocks, tag_size);
        imp_mem_size = calcImpMemory(overhead_size, cache_size);
        cost = calcCost(imp_mem_size);

        //Calculate Physical Memory Values
        num_of_files = files.size();
        num_physical_pages = calcPhysicalPages(physical_memory);
        num_system_pages = calcSystemPages(num_physical_pages, used_memory);
        pte_size = calcPTESize(num_physical_pages);
        total_ram = calcTotalRam(cache_size, num_of_files, pte_size);

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

        //Print Cache calculated values
        System.out.println("\n***** Cache Calculated Values *****\n");
        System.out.printf("%-30s %d\n", "Total # Blocks:", total_blocks); 
        System.out.printf("%-30s %d bits\n", "Tag Size:", tag_size);
        System.out.printf("%-30s %d bits\n", "Index Size:", index_size);
        System.out.printf("%-30s %d\n", "Total # Rows:", total_rows);
        System.out.printf("%-30s %d bytes\n", "Overhead Size:", overhead_size); 
        System.out.printf("%-30s %.2f KB (%d bytes)\n", "Implementation Memory Size:",
                (double)imp_mem_size/KB, imp_mem_size);
        System.out.printf("%-30s $%.2f @ $%.2f per KB\n", "Cost:", cost, PRICE_PER_KB); 

        //Print Physical Memory calculated values
        System.out.println("\n***** Physical Memory Calculated Values *****\n");
        System.out.printf("%-30s %d\n", "Number of Physical Pages:", num_physical_pages);
        System.out.printf("%-30s %d\n", "Number of Pages for System:", num_system_pages);
        System.out.printf("%-30s %d bits\n", "Size of Page Table Entry:", pte_size);
        System.out.printf("%-30s %d bytes\n", "Total RAM for Page Table(s):", total_ram);

    }




    //CALCULATION METHODS
    //
    //
    //CACHE CALC METHODS
    //
    //
    //Method for calculating total number of blocks, given cache sizse and block size
    private static int calcBlocks(int cache_size, int block_size) {
        return (cache_size*KB)/block_size;
    }

    //Method for calculating index bits required for cache
    private static int calcIndex(int cache_size, int block_size, int associativity) {
        return getPower(cache_size*KB) - (getPower(block_size) + getPower(associativity));
    }   
    
    //Method for calculating tag size required for cache
    private static int calcTagSize(int physical_memory, int index_size, int block_size) {
        return getPower(physical_memory*MB) - (index_size + getPower(block_size));
    }

    //Method for calculating total number of rows
    private static int calcTotalRows(int index_size) {
        return (int)Math.pow(2, index_size);
    }

    //Method for calculating overhead for cache
    private static int calcOverhead(int total_blocks, int tag_size) {
        return ((tag_size+1)*total_blocks)/BYTE;
    }
    
    //Method for calculating total memory required for cache
    private static int calcImpMemory(int overhead_size, int cache_size) {
        return overhead_size + cache_size*KB;
    }

    //Method for calculating cost of cache
    private static double calcCost(int imp_mem_size) {
        return (imp_mem_size/KB) * PRICE_PER_KB;
    }

    //PHYSICAL MEMORY CALC METHODS
    //
    //Method for calculating number of physical pages
    private static int calcPhysicalPages(int physical_memory) {
        return (physical_memory*MB)/(4*KB);
    }

    //Method for calculating number of pages for system
    private static int calcSystemPages(int num_physical_pages, int used_memory) {
        return (int)(((double)used_memory/100)*num_physical_pages);
    }
    
    //Method for calculating PTE size
    private static int calcPTESize(int num_physical_pages) {
        return getPower(num_physical_pages) + 1;
    }

    //Method for calculating RAM needed
    private static int calcTotalRam(int cache_size, int num_of_files, int pte_size) {
        return (cache_size*KB*num_of_files*pte_size)/BYTE;
    }

    //Helper method -- takes base and returns the exponent, for powers of 2
    private static int getPower(int base) {
        return (int)(Math.log(base)/Math.log(2));
    }
}

