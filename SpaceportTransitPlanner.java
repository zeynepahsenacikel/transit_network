import java.util.*;

public class SpaceportTransitPlanner {

    Map<Station, Station> prev = new HashMap<>();
    Map<String, Double> cost = new HashMap<>();

    double dist(Station a, Station b) {
        double dx = a.p.x - b.p.x;
        double dy = a.p.y - b.p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    void addUndirected(Station a, Station b, double speed) {
        // TODO: Add bidirectional walking edge

        //calculate walking time in minutes and add edges for both directions
        double t = dist(a, b) / speed; //minutes
        a.edges.add(new Edge(b, t));
        b.edges.add(new Edge(a, t));
    }

    void addDirected(Station a, Station b, double speed) {
        // TODO: Add directed shuttle edge

        //calculate shuttle travel time and add a one-way edge
        double t = dist(a, b) / speed; //minutes
        a.edges.add(new Edge(b, t));
    }

    public List<RouteInstruction> solve(SpaceportTransitNetwork net) {
        List<RouteInstruction> route = new ArrayList<>();

        // TODO:
        // 1. Build graph
        // 2. Add walking edges
        // 3. Add directed shuttle edges
        // 4. Run Dijkstra
        // 5. Reconstruct path

        //clear previous edges, build the complete graph (walking + shuttle), 
        //run Dijkstra's algorithm, and reconstruct the shortest path
        for (ShuttleCorridor c : net.corridors) {
            for (Station s : c.stations) {
                s.edges.clear();
            }
        }

        net.start.edges.clear();
        net.end.edges.clear();

        // Collect all stations
        List<Station> allStations = new ArrayList<>();
        allStations.add(net.start);
        allStations.add(net.end);
        for (ShuttleCorridor corridor : net.corridors) {
            allStations.addAll(corridor.stations);
        }

        //speeds in m/min
        double walkSpeedMpm = net.walkSpeed;
        double shuttleSpeedMpm = net.shuttleSpeed;

        //add walking edges between all pairs of stations
        for (int i = 0; i < allStations.size(); i++) {
            for (int j = i+1; j < allStations.size(); j++) {
                addUndirected(allStations.get(i), allStations.get(j), walkSpeedMpm);
            }
        }

        //add d shuttle e for each corridor
        for (ShuttleCorridor corridor : net.corridors) {
            List<Station> stations = corridor.stations;
            for (int i = 0; i < stations.size() - 1; i++) {
                addDirected((stations.get(i)), stations.get(i+1), shuttleSpeedMpm);
            }
        }

        //dijlstra
        Map<Station, Double> dist = new HashMap<>();
        Map<Station, Station> prevSt = new HashMap<>();
        Map<Station, Double> edgeTimeUsed = new HashMap<>(); // time of edge used to reach station
        Map<Station, Boolean> shuttleEdgeUsed = new HashMap<>();

        for (Station s: allStations) {
            dist.put(s, Double.MAX_VALUE);
        }
        dist.put(net.start, 0.0);

        //priority queue: (cost, station)
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));
        Map<Station, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < allStations.size(); i++) {
            indexMap.put(allStations.get(i), i);
        }

        pq.offer(new double[]{0.0, indexMap.get(net.start)});

        while (!pq.isEmpty()) {
            double[] current = pq.poll();
            double currentCost = current[0];
            int currentIndex = (int) current[1];
            Station currentSt = allStations.get(currentIndex);
            
            if (currentCost > dist.get(currentSt)) {
                continue;
            }

            for (Edge e : currentSt.edges) {
                double newCost = currentCost + e.w;
                if (newCost < dist.get(e.to)) {
                    dist.put(e.to, newCost);
                    prevSt.put(e.to, currentSt);
                    edgeTimeUsed.put(e.to, e.w);
            
                    boolean isShuttle = isShuttleEdge(currentSt, e.to, net);
                    shuttleEdgeUsed.put(e.to, isShuttle);

                    shuttleEdgeUsed.put(e.to, isShuttle);
                    pq.offer(new double[]{newCost, indexMap.get(e.to)});
                }
            }
        }

        //reconstruct path
        List<Station> path = new ArrayList<>();
        Station current = net.end;
        while(current != null) {
            path.add(current);
            current = prevSt.get(current);
        }
        Collections.reverse(path);

        //build route instructions
        for (int i = 0; i < path.size()- 1; i++) {
            Station from = path.get(i);
            Station to = path.get(i + 1);
            double time = edgeTimeUsed.get(to);
            boolean shuttle = shuttleEdgeUsed.get(to);
            route.add(new RouteInstruction((from.name), to.name, time, shuttle));
        }

        return route;
    }

    private boolean isShuttleEdge(Station from, Station to, SpaceportTransitNetwork net) {
        for (ShuttleCorridor sh : net.corridors) {
            List<Station> stations = sh.stations;
            for (int i= 0; i<stations.size() - 1; i++) {
                if (stations.get(i) == from && stations.get(i+1) == to) {
                    return true;
                }
            }
        }
        return false;
    }

    public void print(List<RouteInstruction> r) {
        // TODO: Print route exactly in required format

        //calculate total transit time and print each step of the route clearly
        if (r.isEmpty()) {
            System.out.println("Fastest route takes 0 minute(s).");
            System.out.println("Route Instructions");
            System.out.println("------------------");
            return;
        }

        double totalTime = 0;
        for (RouteInstruction i : r) {
            totalTime += i.t;
        }
        long totalMinutes = Math.round(totalTime);

        System.out.println("Fastest route takes " + totalMinutes + " minute(s).");
        System.out.println("Route Instructions");
        System.out.println("------------------");

        int step = 1;
        for (RouteInstruction i : r) {
            String verb;
            if (i.shuttle) {
                verb = "Take the shuttle";
                System.out.printf(Locale.US,"%d. %s from \"%s\" to \"%s\" for %.2f minutes.%n", step++, verb, i.a, i.b, i.t);
            } else {
                verb = "Walk";
                System.out.printf(Locale.US,"%d. %s from \"%s\" to \"%s\" for %.2f minutes.%n", step++, verb, i.a, i.b, i.t);
            }
        }
    }
}