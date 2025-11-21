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
    private int total_hits;
    private int pages_from_free;
    private int pages_from_fault;

    //Constructor -- initializes the page table array, as well as name
    public PageTable(int size, String trace_file) {
        this.page_table = new PhysicalAddress[size];
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
    public PhysicalAddress getAddress(int virtual_address) { 
        total_hits++;
        return page_table[virtual_address];
    }

    public void setAddress(int virtual_address, PhysicalAddress physical_address) {
        page_table[virtual_address] = physical_address;
    }

    public void clearAddress(int virtual_address) {
        page_table[virtual_address] = null;
    }

    public void incrementPagesFree(){
        this.pages_from_free++;
    }

    public void incrementPagesFault() {
        this.pages_from_fault++;
    }

    public void incrementHits() {
        this.total_hits++;
    }
    public int getHits() {
    	return this.total_hits;
    }
    public String toString() {
        return   pages_from_free +" total pages used " + pages_from_fault + " faults "  + total_hits + " total hits ";
    }
}
