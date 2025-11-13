package Ram;

public class PhysicalAddress {
    int last_access;
    int address;
    static int CURR_ADDRESS = 0;

    public PhysicalAddress() {
        last_access = 1;
        address = CURR_ADDRESS;
        CURR_ADDRESS+=1;

    }
    public void printMe()
    {
        System.out.println("MEEEE");
    }
}

