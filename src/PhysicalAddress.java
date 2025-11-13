import java.util.*;

//PHYSICAL ADDRESS CLASS
//
//This class implements comparable, so it can be used in a priority queue -- this will be useful for determining which physical page to 
//select when allocating pages 
//
//
public class PhysicalAddress implements Comparable<PhysicalAddress> {
    private int last_access;
    private int address;
    static int CURR_ADDRESS = 0;
    private int access_process;
    private int mapped_address;

    //Constructor
    public PhysicalAddress() {
        last_access = 1;
        mapped_address = 0;
        address = CURR_ADDRESS;
        CURR_ADDRESS+=1;
        access_process = -1;
    }

    //Method for setting the last virtual memory address, and cycle that the address was allocated, as well as the 
    //process that owns the physical page
    public void setLastAccess(int new_access, int access_process, int mapped_address)
    {
        last_access = new_access;
        this.access_process = access_process;
        this.mapped_address = mapped_address;
    }
    
    public int getMappedAddress() {
        return mapped_address;
    }

    public int getAccessProcess() {
        return access_process;
    }

    public int getLastAccess() {
        return last_access;
    }

    //Compare to will compare the last access of the processes, so the last accessed memory will get reallocated first
    @Override
    public int compareTo(PhysicalAddress address_two) {
        return last_access - address_two.getLastAccess();
    }
    public String toString() {
        return "HI";
    }
}

