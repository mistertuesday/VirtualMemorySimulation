import java.util.Arrays;

/**
 * This class implements a page table, which will map, from its index to a PhysicalAddress object
 * This class has functionality for adding elements, freeing elements, and tracking the page tables hits, free pages allocated, and page fault maps.
 *
 */
public class PageTable {
    //Page table array and trace file name
    private PhysicalAddress[] page_table;
    private String trace_file;

    //Tracking variables for page table hits and pages used
    private long total_hits;
    private long pages_from_free;
    private long pages_from_fault;

    //Constructor -- initializes the page table array, as well as name
    public PageTable(long size, String trace_file) {
        this.page_table = new PhysicalAddress[(int)size];
        this.trace_file = trace_file;
        total_hits = 0;
        pages_from_free = 0;
        pages_from_fault = 0;
    }

    //
    //
    //Necessary getters and setters
    //
    //
    public PhysicalAddress getAddress(long virtual_address) { 
//        total_hits++;
        return page_table[(int)virtual_address];
    }

    public void setAddress(long virtual_address, PhysicalAddress physical_address) {
        page_table[(int)virtual_address] = physical_address;
    }

    public void clearAddress(long virtual_address) {
        page_table[(int)virtual_address] = null;
    }

    public void incrementPagesFree(){
        this.pages_from_free++;
    }
    
    public long getFree() {
    	return this.pages_from_free;
    }

    public void incrementPagesFault() {
        this.pages_from_fault++;
    }
    
    public long getFault() {
    	return this.pages_from_fault;
    }

    public void incrementHits() {
        this.total_hits++;
    }
    
    public long getHits() {
    	return this.total_hits;
    }
    public String toString() {
        return   pages_from_free +" total pages used " + pages_from_fault + " faults "  + total_hits + " total hits ";
    }
}
