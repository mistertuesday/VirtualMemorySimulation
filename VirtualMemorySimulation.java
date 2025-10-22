import java.util.ArrayList;
public class VirtualMemorySimulation {
    public static void main(String[] args) {
        int cache_size=0;
        int block_size;
        int associativity;
        String replacement_policy;
        int physical_memory;
        int used_memory;
        int instructions;
        ArrayList<String> files = new ArrayList<>();
        for(int i = 0; i < args.length; i = i + 2)
        {
            switch(args[i]) {
                case "-f":
                    files.add(args[i + 1]);
                    break;
                case "–s":
                    cache_size = Integer.parseInt(args[i + 1]);
                    break;
                case "–b":
                    block_size = Integer.parseInt(args[i + 1]);
                    break;
                case "–a":
                    associativity = Integer.parseInt(args[i + 1]);
                    break;
                case "–r":
                    replacement_policy = args[i + 1];
                    break;
                case "–p":
                    physical_memory = Integer.parseInt(args[i + 1]);
                    break;
                case "-u":
                    used_memory = Integer.parseInt(args[i + 1]);
                    break;
                case "-n":
                    instructions = Integer.parseInt(args[i+1]);
                    break;
            }
        }
    }
}

