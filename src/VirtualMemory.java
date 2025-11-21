import java.util.PriorityQueue;
import java.util.ArrayList;
public class VirtualMemory {
    static int TABLE_SIZE = 524288;                         //page table size
    private PriorityQueue<PhysicalAddress> physical_queue;  //priority queue with physicaladdresses
    private ArrayList<PageTable> page_tables;               //arraylist with each trace file's page table
    private int access_time;                                     //current access #(increments each time memory is accessed 

    private int access_file;                                //int to track which file is currently being processed
    private int physical_size;
    private ArrayList<String> trace_files;
    
    //Constructor
    public VirtualMemory(int physical_size, ArrayList<String> trace_files) {
        access_time = 0;
        this.physical_size = physical_size;
        this.trace_files = trace_files;
        this.physical_queue = new PriorityQueue<PhysicalAddress>();
        this.page_tables = new ArrayList<PageTable>();
        initializePageTables();
    }

    //Used by the constructor
    //Will initialize the page table for each of the trace files
    private void initializePageTables() {
        for (String trace_file: trace_files) {
            page_tables.add(new PageTable(TABLE_SIZE, trace_file));
        }
    }
    
    public void setAccessFile(int new_access_file) {
        this.access_file = new_access_file;
        this.physical_queue = new PriorityQueue<PhysicalAddress>();
    }

    private PageTable getCurrentTable() {
        return page_tables.get(access_file);
    }


    public void accessMemory(int full_virtual_address)
    {
        int virtual_address = full_virtual_address >> 12;
        PhysicalAddress temp = getCurrentTable().getAddress(virtual_address);
        if (temp == null) {
            if (physical_queue.size()<physical_size) {
                temp = new PhysicalAddress();
                getCurrentTable().incrementPagesFree();
            }
            else {
                temp = physical_queue.peek();
                getCurrentTable().incrementPagesFault();
            }
        }
        //TODO - This hit calc just isn't quite right... It's close, but not close enough.
        else {
        	getCurrentTable().incrementHits();
        }
        physical_queue.remove(temp);
        temp.setLastAccess(access_time, access_file, virtual_address);
        physical_queue.add(temp);
        getCurrentTable().setAddress(virtual_address, temp);
        ++access_time;
    }

    private void clean(PhysicalAddress temp) {
        //System.out.printf("Replacing %d\n", temp.getPhysicalAddress());
        page_tables.get(temp.getAccessProcess()).clearAddress(temp.getMappedAddress());
    }

    public String[] outputs1() {
    	int hits = 0;
    	int free = 0;
    	int faults = 0;
    	for (PageTable p : page_tables) {
    		hits = hits + p.getHits();
    		free = free + p.getFree();
    		faults = faults + p.getFault();
    	}
    	String outputs = "" + hits + " " + free + " " + faults;
    	return outputs.split(" ");
    }
    
    public int[] outputs2() {
    	int[] outputs = new int[page_tables.size()];
    	int i = 0;
    	for (PageTable p : page_tables) {
    		outputs[i] = p.getFree() + p.getFault();
    		i++;
    	}
		return outputs;
    }
}
