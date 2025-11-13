import java.util.Arrays;

public class PageTable {
    //Page table array and trace file name
    private PhysicalAddress[] page_table;
    private String trace_file;

    //Tracking variables for page table hits and pages used
    private int total_hits;
    private int pages_from_free;
    private int pages_from_fault;


    public PageTable(int size, String trace_file) {
        this.page_table = new PhysicalAddress[size];
        this.trace_file = trace_file;
    }

    public PhysicalAddress getAddress(int virtual_address) { 
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


    public String toString() {
        return trace_file;
    }
}
