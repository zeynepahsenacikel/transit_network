import java.util.*;

public class LaunchPlan {

    String name;
    List<Operation> ops;

    public LaunchPlan(String n, List<Operation> o) {
        name = n;
        ops = o;
    }

    /**
     * Computes the earliest possible start times for all operations.
     * You should consider dependency constraints between operations.
     */
    public int[] earliest() {
        // TODO: Implement topological sorting + scheduling

        //use Kahn's algorithm for topological sorting to find earliest start times
        int n = ops.size();
        int maxCode =-1; //newwwwwww

        //map code -> idx in ops list
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int code = ops.get(i).code;
            map.put(code, i);
            if (code > maxCode) maxCode = code;
        }

        //build adjacency list (prereq -> dependent)
        List<List<Integer>> adjacency = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjacency.add(new ArrayList<>());
        }

        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            for (int pre : ops.get(i).prereq) {
                if (map.containsKey(pre)) { // newwwwwwww
                    int preIdx = map.get(pre);
                    adjacency.get(preIdx).add(i);
                    inDegree[i]++;
                }
            }
        }

        //earliest start times
        int[] start = new int[n];

        //kahns bfs topo sort
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while(!queue.isEmpty()) {
            int k = queue.poll();
            int finish = start[k] + ops.get(k).time;
            for (int l : adjacency.get(k)) {
                if (finish > start[l]) {
                    start[l] = finish;
                }
                inDegree[l]--;
                if (inDegree[l] == 0) {
                    queue.add(l);
                }
            }
        }

        // bu blok yeni
        int[] result = new int[maxCode + 1];
        for (int i = 0; i < n; i++) {
            result[ops.get(i).code] = start[i];
        }

        return result;
    }

    /**
     * Computes total time required to complete all operations.
     */
    public int total(int[] schedule) {
        // TODO: Compute maximum finish time

        int max = 0;
        int n = ops.size();
        for (int i = 0; i < n; i++) {
            int a = schedule[i] + ops.get(i).time;
            if (a > max) {
                max = a;
            }
        }
        
        return max;
    }

    /**
     * Helper function to print separator line
     */
    public static void printLine(int n) {
        for (int i = 0; i < n; i++) System.out.print("-");
        System.out.println();
    }

    /**
     * Prints the launch plan timeline in required format.
     */
    public void print() {

        int[] schedule = earliest();

        int width = 65;

        printLine(width);
        System.out.println("Launch Plan: " + name);
        printLine(width);

        // Header
        System.out.printf("%-8s%-35s%-8s%-8s\n", "Code", "Operation", "Begin", "Finish");

        printLine(width);

        // TODO:
        // Print each operation with:
        // code, label, start time, finish time
        for (Operation o : ops) {
            int start = schedule[o.code];
            int finish = start + o.time;
            System.out.printf("%-8d%-35s%-8d%-8d\n", o.code, o.label, start, finish);
        }

        printLine(width);

        // TODO:
        // Print total duration in format:
        // Launch-ready in X hour(s).

        //print the total calculated duration in the expected format
        System.out.println("Launch-ready in " + total(schedule) + " hour(s).");

        printLine(width);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LaunchPlan)) return false;

        LaunchPlan other = (LaunchPlan) o;

        return name.equals(other.name) && ops.equals(other.ops);
    }
}