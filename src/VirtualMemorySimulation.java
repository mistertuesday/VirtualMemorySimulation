import java.util.ArrayList;
import java.lang.Math;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
public class VirtualMemorySimulation {
    
    //KB, MB, B for easy calculations
    static long BYTE = 8;
    static long KB = 1024;
    static long MB = 1048576;

    //Price for cost calculations, per KB
    static double PRICE_PER_KB = 0.07;

    public static void main(String[] args) throws FileNotFoundException {

        //Declare variables to hold each token passed by command line
        long cache_size = 0;
        long block_size = 0;
        long associativity = 0;
        String replacement_policy = new String();
        long physical_memory = 0;
        long used_memory = 0 ;
        long instructions = 0 ;
        ArrayList<String> files = new ArrayList<>();
        
        //Declare calculated variables for Cache
        long total_blocks = 0;
        long tag_size = 0;
        long index_size = 0;
        long total_rows = 0;
        long overhead_size = 0;
        long imp_mem_size = 0;
        double cost = 0;

        //Declare calcluated variables for Physical Memory
        long num_physical_pages = 0;
        long num_system_pages = 0;
        long pte_size = 0;
        long total_ram = 0;
        long num_of_files = 0;

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
                    cache_size = Long.parseLong(args[i + 1]);
                    break;
                case "-b":
                case "–b":
                case "—b":
                    block_size = Long.parseLong(args[i + 1]);
                    break;
                case "-a":
                case "–a":
                case "—a":
                    associativity = Long.parseLong(args[i + 1]);
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
                    physical_memory = Long.parseLong(args[i + 1]);
                    break;
                case "-u":
                case "–u":
                case "—u":
                    used_memory = Long.parseLong(args[i + 1]);
                    break;
                case "-n":
                case "–n":
                case "—n":
                    instructions = Long.parseLong(args[i+1]);
                    break;
            }
        }
        PageTable pageTest = new PageTable(physical_memory,files.get(0));
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
        
        //MILESTONE 2
        //Create vram object!
        VirtualMemory ram_object = new VirtualMemory((num_physical_pages - num_system_pages),files);
        //Create one list of tuples for each trace file, store each list in another list.
        ArrayList<ArrayList<Tuple>> trace_file_inputs = new ArrayList<ArrayList<Tuple>>();
        ArrayList<Cache> caches = new ArrayList<Cache>();
        //Parse each trace file and add it to our list of tuple sets.
        long instruction_bytes = 0;
        long src_dst_bytes = 0;
        for(String file_name: files) {
        
            ArrayList<Tuple> outputs = new ArrayList<>();
        	File f = new File(file_name);
        	Scanner s = new Scanner(f);
        	String line;
        	while (s.hasNextLine()) {
        		//First line, EIP
        		line = s.nextLine();
        		//If the current line is blank, then we need the next one.
        		//But if it's the final line, we can break the loop to avoid nosuchelement exception
        		if (line.compareTo("") == 0) {
        			if (s.hasNextLine() == true) {
        				line = s.nextLine();
        			}
        			else {
        				break;
        			}
        		}
        		//These instruction length lines are in two digit form always, making it super weird.
        		long iLength;
        		iLength = Long.parseLong(line.substring(5,7));
        		long sAddress = Long.decode("0x"+line.substring(10,18));
        		outputs.add(new Tuple(sAddress, iLength));
                        instruction_bytes += iLength;
        		//Third line, dest and source
        		//Note to self, assignment document says ASSUME ALL VALID DATA ACCESSES ARE 4 BYTES!!!
        		line = s.nextLine();
        		long dstM;
        		if (line.charAt(17) != '-') {
        			dstM = Long.decode("0x"+line.substring(6,14));
        			outputs.add(new Tuple(dstM, 4));
                                src_dst_bytes += 4;
        		}
        		long srcM;
        		if (line.charAt(46) != '-') {
        			srcM = Long.decode("0x"+line.substring(33,41));
        			outputs.add(new Tuple(srcM, 4));
                                src_dst_bytes += 4;
        		}
        	}
        	//No more lines, bye scanner!
        	s.close();
            trace_file_inputs.add(outputs);
        }

        //Number of times memory is accessed
        long mapped_virt_pages = 0;
        long total_cache_accesses = 0;
        Cache cash = new Cache(associativity, index_size, tag_size, getPower(block_size));
        long hits = 0;
        long comp = 0;
        long conf = 0;
        //Run simulation through virtual memory
        for(int x = 0; x < trace_file_inputs.size(); x++) {
        	//Load up one of the trace file instruction sets
            ArrayList<Tuple> ints_to_feed = trace_file_inputs.get(x);
            //Set the access file and queue for the vram object
            ram_object.setAccessFile(x);
            //Run through each of the instructions.
            for(Tuple int_to_feed: ints_to_feed) {
            	//Get the first mem address
                long first_address = int_to_feed.getX();
                //Offset by the byte count to get the second one
                long second_address = int_to_feed.getX()+int_to_feed.getY()-1;
                //Access the memory at the first address
                ram_object.accessMemory(first_address);
                cash.access(first_address);
                mapped_virt_pages++;
                total_cache_accesses++;
                //If the page numbers for the first and second address don't line up...
                //that means we've gone forwards a page and need to run another access.
                if(getIndex(first_address, index_size, block_size) != getIndex(second_address, index_size, block_size)) {
                    //System.out.printf("THEEEEEEEEE: %d %d\n",first_address, second_address );
                    cash.access(second_address);
                    total_cache_accesses++;
                }
            }
            hits += cash.get_hits();
            comp += cash.get_comp_misses();
            conf += cash.get_conf_misses();
            cash.reset();
        }
        
        //
        //Print out memory simulation results
        //
        
        //TODO - Fix the atrocious formatting on this I'm bad at that ty
        System.out.println("");
        System.out.printf("***** VIRTUAL MEMORY SIMULATION RESULTS *****\n\n");
        System.out.printf("%-30s %d\n", "Physical Pages Used By SYSTEM:", num_system_pages);
        System.out.printf("%-30s %d\n\n", "Pages Available to User:", num_physical_pages - num_system_pages); 
        System.out.printf("%-30s %d\n", "Virtual Pages Mapped:", mapped_virt_pages);
        //Section 2
        //s2[0] = page hits
        //s2[1] = pages from free
        //s2[2] = page faults
        String[] s2 = ram_object.outputs1();
        System.out.printf("%15s \n", "------------------------------");
        System.out.printf("%-30s \n", "Page Table Hits: " + s2[0]);
        //System.out.println("");
        System.out.printf("%-30s \n", "Pages From Free: " + s2[1]);
        //System.out.println("");
        System.out.printf("%-30s \n\n\n", "Page Faults: " + s2[2]);
        //System.out.println("");
        System.out.printf("%-30s \n", "Page Table Usage Per Process:"); 
        System.out.printf("%15s \n\n", "------------------------------");
        //Usage per process values
        long[] upp = ram_object.outputs2();
        int count = 0;
        for (String fn : files) {
        	//TODO - NEED TO ROUND THIS TO A NICE CLEAN CRISP 2 DECIMAL PLACES
        	double percentage = (upp[count] / Math.pow(2, 19)) * 100;
        	System.out.print("[" + count + "] " + fn + ":" + "\n");
        	System.out.printf("\t%s %d (%.2f%%)\n", "Used Page Table Entries:", upp[count], percentage);
        	//TODO - CALC AND PRINT THE WASTED PAGES FOR EACH TRACE FILE
            System.out.printf("\t%s %d bytes\n\n", "Page Table Wasted: ", (long)((num_physical_pages - num_system_pages) * pte_size - ((upp[count] *pte_size)/8.0)));
            count++;
        }

        System.out.printf("***** CACHE SIMULATION RESULTS *****\n\n");
        System.out.printf("%-30s %-10d (%d addresses)\n","Total Cache Accesses:", total_cache_accesses, mapped_virt_pages); 
       // System.out.printf("Hits %d\n Compulsory Misses: %d\n Conflict Misses: %d\n", hits, comp, conf);
        System.out.printf("%-30s %d\n", "--- Instruction Bytes:", instruction_bytes);
        System.out.printf("%-30s %d\n", "--- SrcDst Bytes:", src_dst_bytes);
        System.out.printf("%-30s %d\n", "Cache Hits:", hits);
        System.out.printf("%-30s %d\n", "Cache Misses:", comp+conf);
        System.out.printf("%-30s %d\n", "--- Compulsory Misses:", comp);
        System.out.printf("%-30s %d\n", "--- Conflict Misses:", conf);
        System.out.printf("***** ***** CACHE HIT & MISS RATE *****\n\n");
        //Calculate hit rate
        double hitrate = ((double)hits / total_cache_accesses) * 100;
        System.out.printf("%-30s %.4f%%\n", "Hit Rate:", hitrate);
        System.out.printf("%-30s %f\n", "Miss Rate:", -1.0);
        System.out.printf("%-30s %f Cycles/Instruction (%d)\n", "CPI:", -1.0, -1);
        System.out.printf("%-30s %f KB / %f KB = %f%% Waste: $%f/chip\n", "Unused Cache Space:", -1.0, -1.0, -1.0, -1.0);
        System.out.printf("%-30s %d / %d\n", "Unused Cache Blocks:", -1, -1);
    }

    //SHIFT METHOD FOR CHECKING PAGE CHECKS
    //
    private static long getPage(long virtual_address) {
        return virtual_address >> 12;
    }
    
    //SHIFT METHOD for testing cache accesses
    private static long getIndex(long virtual_address, long index_size, long block_size) {
        return (virtual_address >> getPower(block_size)) & ~(0x7FFFFFF<<getPower(index_size));
    }
    //
    //TRACE FILE PARSING - Returns a list of tuple objects corresponding to addresses and byte counts from a singular trace file.
    private static ArrayList<Tuple> trace(String filename) throws FileNotFoundException {
    	ArrayList<Tuple> outputs = new ArrayList<>();
    	File f = new File(filename);
    	Scanner s = new Scanner(f);
    	String line;
    	while (s.hasNextLine()) {
    		//First line, EIP
    		line = s.nextLine();
    		//If the current line is blank, then we need the next one.
    		//But if it's the final line, we can break the loop to avoid nosuchelement exception
    		if (line.compareTo("") == 0) {
    			if (s.hasNextLine() == true) {
    				line = s.nextLine();
    			}
    			else {
    				break;
    			}
    		}
    		//These instruction length lines are in two digit form always, making it super weird.
    		long iLength;
    		iLength = Long.parseLong(line.substring(5,7));
    		long sAddress = Long.decode("0x"+line.substring(10,18));
    		outputs.add(new Tuple(sAddress, iLength));
    		//Third line, dest and source
    		//Note to self, assignment document says ASSUME ALL VALID DATA ACCESSES ARE 4 BYTES!!!
    		line = s.nextLine();
    		long dstM;
    		if (line.charAt(17) != '-') {
    			dstM = Long.decode("0x"+line.substring(6,14));
    			outputs.add(new Tuple(dstM, 4));
    		}
    		long srcM;
    		if (line.charAt(46) != '-') {
    			srcM = Long.decode("0x"+line.substring(33,41));
    			outputs.add(new Tuple(srcM, 4));
    		}
    	}
    	//No more lines, bye scanner!
    	s.close();
    	return outputs;
    }

    //CALCULATION METHODS
    //
    //
    //CACHE CALC METHODS
    //
    //
    //Method for calculating total number of blocks, given cache sizse and block size
    private static long calcBlocks(long cache_size, long block_size) {
        return (cache_size*KB)/block_size;
    }

    //Method for calculating index bits required for cache
    private static long calcIndex(long cache_size, long block_size, long associativity) {
        return getPower(cache_size*KB) - (getPower(block_size) + getPower(associativity));
    }   
    
    //Method for calculating tag size required for cache
    private static long calcTagSize(long physical_memory, long index_size, long block_size) {
        return getPower(physical_memory*MB) - (index_size + getPower(block_size));
    }

    //Method for calculating total number of rows
    private static long calcTotalRows(long index_size) {
        return (long)Math.pow(2, index_size);
    }

    //Method for calculating overhead for cache
    private static long calcOverhead(long total_blocks, long tag_size) {
        return ((tag_size+1)*total_blocks)/BYTE;
    }
    
    //Method for calculating total memory required for cache
    private static long calcImpMemory(long overhead_size, long cache_size) {
        return overhead_size + cache_size*KB;
    }

    //Method for calculating cost of cache
    private static double calcCost(long imp_mem_size) {
        return (imp_mem_size/KB) * PRICE_PER_KB;
    }

    //PHYSICAL MEMORY CALC METHODS
    //
    //Method for calculating number of physical pages
    private static long calcPhysicalPages(long physical_memory) {
        return (physical_memory*MB)/(4*KB);
    }

    //Method for calculating number of pages for system
    private static long calcSystemPages(long num_physical_pages, long used_memory) {
        return (long)(((double)used_memory/100)*num_physical_pages);
    }
    
    //Method for calculating PTE size
    private static long calcPTESize(long num_physical_pages) {
        return getPower(num_physical_pages) + 1;
    }

    //Method for calculating RAM needed
    private static long calcTotalRam(long cache_size, long num_of_files, long pte_size) {
        return (cache_size*KB*num_of_files*pte_size)/BYTE;
    }

    //Helper method -- takes base and returns the exponent, for powers of 2
    private static long getPower(long base) {
        return (long)(Math.log(base)/Math.log(2));
    }
}

