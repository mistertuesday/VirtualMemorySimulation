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
        populatePhysicalQueue();
        initializePageTables();
    }

    //Used by the constructor
    //Will populate the physical_queue, which contains the available physical addresses
    private void populatePhysicalQueue() {
        System.out.printf("Size: %d\n", physical_size);
        for(int i = 0; i < physical_size; i++) {
            PhysicalAddress p_addy = new PhysicalAddress();
            System.out.printf("new: %d\n", p_addy.getPhysicalAddress());
            physical_queue.add(p_addy);
        }
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
    }

    private PageTable getCurrentTable() {
        return page_tables.get(access_file);
    }


    public void accessMemory(int full_virtual_address)
    {
        System.out.printf("%d\n", full_virtual_address);
        int virtual_address = full_virtual_address >> 12;
        PhysicalAddress temp = getCurrentTable().getAddress(virtual_address);
        if (temp == null) {
            temp = physical_queue.peek();
            if(temp.getAccessProcess() == -1){
                getCurrentTable().incrementPagesFree();
            }
            else {
                getCurrentTable().incrementPagesFault();
                clean(temp);
            }
            getCurrentTable().setAddress(virtual_address, temp);
        }
        physical_queue.remove(temp);
        temp.setLastAccess(access_time, access_file, virtual_address);
        physical_queue.add(temp);
        ++access_time;
    }

    private void clean(PhysicalAddress temp) {
        //System.out.printf("Replacing %d\n", temp.getPhysicalAddress());
        page_tables.get(temp.getAccessProcess()).clearAddress(temp.getMappedAddress());
    }

    public void print() {
        System.out.printf("There are %s \n", getCurrentTable());
    }
}
