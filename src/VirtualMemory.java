import java.util.PriorityQueue;
import java.util.ArrayList;
public class VirtualMemory {
    static int TABLE_SIZE = 524288;                         //page table size
    private PriorityQueue<PhysicalAddress> physical_queue;  //priority queue with physicaladdresses
    private ArrayList<PageTable> page_tables;               //arraylist with each trace file's page table
    private int access;                                     //current access #

    //int to track which file is currently being processed
    private int access_file;
    private int physical_size;
    private ArrayList<String> trace_files;
    
    //Constructor
    public VirtualMemory(int physical_size, ArrayList<String> trace_files) {
        access = 0;
        this.physical_size = physical_size;
        this.trace_files = trace_files;
        populatePhysicalQueue();
        initializePageTables();
    }

    private void populatePhysicalQueue() {
        for(int i = 0; i < physical_size; i++) {
            physical_queue.add(new PhysicalAddress());
        }
    }

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


    public void accessMemory(int virtual_address)
    {
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
        temp.setLastAccess(access, access_file, virtual_address);
        physical_queue.add(temp);
    }

    private void clean(PhysicalAddress temp) {
        page_tables.get(temp.getAccessProcess()).clearAddress(temp.getMappedAddress());
    }
}
