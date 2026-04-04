import java.util.*;

// This is the upgraded TileGraph from Lab 4 with weighted edges and directivity selection

class WeightedEdge {
    
    private Tile myTile;
    private int penalty;

    WeightedEdge(Tile thisTile, int pen)
    {
        myTile = thisTile;
        penalty = pen;
    }

    public Tile getTile() { return myTile; }
    public int getPenalty() { return penalty; }

    public boolean hasTile(Tile thisTile)
    {
        return myTile.equals(thisTile);  // shallow comparison by reference only
    }

    public boolean isEqual(WeightedEdge thisWEdge)
    {
        boolean result = myTile.isEqual(thisWEdge.getTile());
        result &= (penalty == thisWEdge.getPenalty());
        return result;
    }
}

public class TileGraph {
        
    private Map<Tile, LinkedList<WeightedEdge>> adjList;

    public TileGraph()
    {
        adjList = new HashMap<>();
    }

    public TileGraph(TileMap thisMap)
    {
        adjList = new HashMap<>();
        buildGraph(thisMap);
    }

    public void addVertex(Tile thisTile)
    {
        adjList.putIfAbsent(thisTile, new LinkedList<WeightedEdge>());
    }

    public void addEdge(Tile src, Tile dst, int penalty)
    {   
        // Implement the weighted edge insertion method. 

        // check if src exists
        if (!adjList.containsKey(src)) {
            return;
        }
    
        LinkedList <WeightedEdge> edgeList = adjList.get(src);
        WeightedEdge newEdge = new WeightedEdge(dst, penalty);

        //check if edge already exists
        for(WeightedEdge edge : edgeList) {
            if (edge.hasTile(dst)) {
                return; //edge exists, dont add
            }
        }

        //add new edge to end of list
        edgeList.addLast(newEdge);
        
    }

    // returns the path with the smallest number of edges
    private LinkedList<Tile> UnweightedShortestPath(Tile start, Tile end)
    {
        // HIDDEN - Completed in Lab 4 as findShortestPath()
        // You must copy and modify your code from lab 3 to make Game Mode 0 functional.

        LinkedList<Tile> path = new LinkedList<Tile>();  // front end to start

        if (start == null || end == null) {
            return path;
        }

        if (start.isEqual(end)) {
            path.add(start);
            return path;
        }

        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();
        Map<Tile, Tile> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);
        parent.put(start, null);

        boolean found = false;

        while (!queue.isEmpty() && !found) {
            Tile current = queue.poll();

            LinkedList <WeightedEdge> neighbors =adjList.get(current);
            if (neighbors != null) {
                // process order: right then down
                List<Tile> rightNeighbors = new ArrayList<>();
                List<Tile> downNeighbors = new ArrayList<>();

                for (WeightedEdge edge : neighbors) {
                    Tile neighbor = edge.getTile();
                    if (neighbor.getX() > current.getX()) {
                        rightNeighbors.add(neighbor);
                    }
                    else if (neighbor.getY() > current.getY()) {
                        downNeighbors.add(neighbor);
                    }
                }

                Collections.sort(rightNeighbors, (t1, t2) -> Integer.compare(t1.getX(), t2.getX()));
                Collections.sort(downNeighbors, (t1, t2) -> Integer.compare(t1.getY(), t2.getY()));

                List<Tile> orderedNeighbors = new ArrayList<>();
                orderedNeighbors.addAll(rightNeighbors);
                orderedNeighbors.addAll(downNeighbors);

                for (Tile neighbor : orderedNeighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parent.put(neighbor, current);

                        if (neighbor.isEqual(end)) {
                            found = true;
                            break;
                        }
                        
                        queue.add(neighbor);

                    }
                }
            }
        }

        if (found) {
            Tile current = end;
            while (current != null) {
                path.add(current);
                current = parent.get(current);
            }
        }

        return path;
    }

    // dijkastra for path with lowest penalties
    private LinkedList<Tile> DijkstraShortestPath(Tile start, Tile end)
    {
        // Just like UnweightedShortestPath(), you must return the shortest path from *end* to *start*

        LinkedList<Tile> shortestPath = new LinkedList<Tile>();

        if (start ==  null || end == null) {
            return shortestPath;
        }

        //distance map
        Map<Tile, Integer> dist = new HashMap<>();
        Map<Tile, Tile> prev = new HashMap<>();
        Set<Tile> settled = new HashSet<>();

        // priority Q of (dist, tile)
        PriorityQueue<Map.Entry<Integer, Tile>> pq = new PriorityQueue<>((a,b) -> Integer.compare(a.getKey(), b.getKey()));

        //initialise distances to inf and source to 0
        for (Tile vertex : adjList.keySet()) {
            dist.put(vertex, Integer.MAX_VALUE);
        }
        dist.put(start, 0);

        pq.add(new AbstractMap.SimpleEntry<>(0, start));

        while (!pq.isEmpty()) {
            Map.Entry<Integer, Tile> entry = pq.poll(); //retrieves and removes head
            Tile current = entry.getValue();
            int currentDist = entry.getKey();

            //if reach end, break
            if (current.isEqual(end)) {
                break;
            }

            //skip if already visited
            if (settled.contains(current)) {
                continue;
            }
            settled.add(current);

            //check neighbors
            LinkedList<WeightedEdge> neighbors = adjList.get(current);
            if (neighbors != null) {
                for (WeightedEdge edge : neighbors) {
                    Tile neighbor = edge.getTile();
                    int weight = edge.getPenalty();

                    //only if weight is non-negative --> djikstras req
                    if (weight < 0) {
                        continue;
                    }

                    //not visited yet
                    if (!settled.contains(neighbor)) {
                        int newDist = currentDist + weight;
                        //if better path, relax it
                        if (newDist < dist.get(neighbor)) {
                            dist.put(neighbor, newDist);
                            prev.put(neighbor,current);
                            pq.add(new AbstractMap.SimpleEntry<>(newDist, neighbor));
                        }
                    }

                }
            }

        }

        // reverse path order --> from end to start
        if (prev.containsKey(end) || start.isEqual(end)) {
            Tile current = end;
            while (current != null) {
                shortestPath.add(current);
                current = prev.get(current);
            }
        }

        return shortestPath;

    }

    // Bellman-Ford Lowest Penalty Path - returns null if negative weight cycles are detected
    private LinkedList<Tile> BellmanShortestPath(Tile start, Tile end)
    {
        // // remove these lines when implementing this algorithm
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // shortestPath.add(new Tile(0,0,'?',-10));
        // // remove these lines when implementing this algorithm


        // Just like UnweightedShortestPath(), you must return the shortest path from *end* to *start*
        // In addition, if a negative-weighted cycle is detected, you must return *null* for the shortestPath
        // You should also print out a warning message for visual confirmation

        LinkedList<Tile> shortestPath = new LinkedList<Tile>();

        if (start == null || end == null){
            return shortestPath;
        }

        // get all vertices
        Set<Tile> vertices = adjList.keySet();
        int V = vertices.size();

        // maps
        Map<Tile, Integer> dist = new HashMap<>();
        Map<Tile, Tile> prev = new HashMap<>();
        
        // initialise distances to inf and initial to 0
        for (Tile vertex : vertices) {
            dist.put(vertex, Integer.MAX_VALUE);
        }
        dist.put(start, 0);
        prev.put(start, null);

        // all edges into a list
        List<WeightedEdgeList> allEdges = new ArrayList<>();
        for (Map.Entry<Tile, LinkedList<WeightedEdge>> entry : adjList.entrySet()) {
            Tile src = entry.getKey();
            for (WeightedEdge edge : entry.getValue()) {
                allEdges.add(new WeightedEdgeList(src, edge.getTile(), edge.getPenalty()));
            }
        }

        // relax edges V-1 times
        for (int  i =0 ; i < V-1 ; i++){
            boolean updated = false;
            for (WeightedEdgeList edge : allEdges){
                Tile u = edge.src;
                Tile v = edge.dst;
                int weight = edge.weight;

                if (dist.get(u) != Integer.MAX_VALUE && dist.get(u) + weight < dist.get(v)) {
                    dist.put(v , dist.get(u) + weight);
                    prev.put(v,u);
                    updated = true;
                }
            }

            if (!updated) {
                break;
            }
        }

        // check for -ve weight cycles
        for (WeightedEdgeList edge : allEdges){
            Tile u = edge.src;
            Tile v = edge.dst;
            int weight = edge.weight;

            if (dist.get(u) != Integer.MAX_VALUE && dist.get(u) + weight < dist.get(v)) {
                System.out.println("Negative weight cycle exists! D:");
                return null;
            }
        }
        
        return shortestPath;
    }

    // Helper class for bellmanford edges
    private static class WeightedEdgeList {
        Tile src;
        Tile dst;
        int weight;
        
        WeightedEdgeList(Tile s, Tile d, int w) {
            src = s;
            dst = d;
            weight = w;
        }
    }

    private LinkedList<Tile> DAGShortestPath(Tile start, Tile end)
    {
        // Just like UnweightedShortestPath(), you must return the shortest path from *end* to *start*

        LinkedList<Tile> shortestPath = new LinkedList<Tile>();

        if (start == null || end == null) {
            return shortestPath;
        }

        // get topological order
        LinkedList<Tile> topOrder = topologicalSort();
        if (topOrder == null || topOrder.isEmpty()) {
            return shortestPath;
        }

        //initialise map and set all to inf and src to 0
        Map<Tile, Integer> dist = new HashMap<>();
        Map<Tile, Tile> prev = new HashMap<>();

        for (Tile vertex : adjList.keySet()) {
            dist.put(vertex, Integer.MAX_VALUE);
        }
        dist.put(start, 0);
        prev.put(start, null);

        // vertices in top. order
        boolean startReached = false;
        for (Tile u : topOrder) {
            if (u.isEqual(start)) {
                startReached = true;
            }

            if (!startReached && !u.isEqual(start)) {
                continue;
            }

            if (dist.get(u) != Integer.MAX_VALUE) {
                LinkedList<WeightedEdge> neighbors = adjList.get(u);
                if (neighbors != null) {
                    for (WeightedEdge edge : neighbors) {
                        Tile v = edge.getTile();
                        int weight = edge.getPenalty();

                        if (dist.get(u) + weight < dist.get(v)) {
                            dist.put(v, dist.get(u) + weight);
                            prev.put(v,u);
                        }
                    }
                }
            }
        }

        // path --> end to start
        if (prev.containsKey(end) || start.isEqual(end)) {
            Tile current = end;
            while (current != null) {
                shortestPath.add(current);
                current = prev.get(current);
            }
        }
        
        return shortestPath;
    }
    
    // Kahn's Topological Sorting
    private LinkedList<Tile> topologicalSort()
    {
        LinkedList<Tile> sortedList = new LinkedList<Tile>();

        // in degree of each vertex and init all to 0
        Map<Tile, Integer> inDegree = new HashMap<>();
        for (Tile vertex : adjList.keySet()) {
            inDegree.put(vertex, 0);
        }

        // compute all indegrees
        for (LinkedList<WeightedEdge> edges : adjList.values()) {
            for (WeightedEdge edge : edges) {
                Tile dst = edge.getTile();
                inDegree.put(dst, inDegree.get(dst) + 1);
            }
        }

        // Q for v's w/ indegree = 0
        Queue<Tile> queue = new LinkedList<>();
        for (Map.Entry<Tile, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        //process Q
        while (!queue.isEmpty()) {
            Tile u = queue.poll();
            sortedList.add(u);

            LinkedList<WeightedEdge> neighbors = adjList.get(u);
            if (neighbors != null) {
                for (WeightedEdge edge : neighbors) {
                    Tile v = edge.getTile();
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        queue.add(v);
                    }
                }
            }
        }

        //if not all v's processed, check for cycles
        if (sortedList.size() != adjList.size()) {
            return new LinkedList<Tile>();
        }


        return sortedList;
    }

    public LinkedList<Tile> findShortestPath(Tile start, Tile end, int complexity)
    {
        switch(complexity)
        {
            default:
            case 0:
                return UnweightedShortestPath(start, end);  // Lab 4 --> plain
             
            case 1:
                return DAGShortestPath(start, end);    // DAG --> penalty only
                
            case 2:
                return DijkstraShortestPath(start, end);    // Dijkstra --> penalty only

            case 3:
                return BellmanShortestPath(start, end);    // Bellman --> penalty + reward
        }
    }

    public void printGraph()
    {
        Set<Tile> keySet = adjList.keySet();
        Collection<LinkedList<WeightedEdge>> valueLists = adjList.values();      

        Iterator<Tile> keySetIter = keySet.iterator();  // so to iterate through map
        Iterator<LinkedList<WeightedEdge>> valueListsIter = valueLists.iterator();  // so to iterate through map
        int size = keySet.size();
        
        for(int i = 0; i < size; i++)
        {
            keySetIter.next().printTileCoord();
            System.out.printf(" >>\t");
            valueListsIter.next().forEach(e -> {e.getTile().printTileCoord(); System.out.printf(" : ");});
            System.out.println();
        }
    }

    private void buildGraph(TileMap mapRef)
    {
        // HIDDEN - Completed in Lab 3

        // You must upgrade your lab 4 code so that the following weights are scanned into the edge of the graph
        // Score Penalty ('x') gives a weight of +5
        // Score Reward ('$') gives a weight of -2
    }



    // Test Bench Below
    // Test Bench Below
    // Test Bench Below

    private static boolean totalPassed = true;
    private static int totalTestCount = 0;
    private static int totalPassCount = 0;

    public static void main(String args[])
    {        
        testAddWeightedEdge1();
        testAddWeightedEdge2();
        testAddWeightedEdgeCustom();

        testTopologicalSort1();
        testTopologicalSort2();
        testTopologicalSortCustom();

        testShortestPathDAG1();
        testShortestPathDAG2();
        testShortestPathDAGCustom();

        testShortestPathDijkastra1();
        testShortestPathDijkastra2();
        testShortestPathDijkastraCustom();

        testShortestPathBellmanFord1();
        testShortestPathBellmanFord2();
        testNegativeCycleBellmanFord1();
        testNegativeCycleBellmanFord2();
        testShortestPathBellmanFordCustom();

        System.out.println("================================");
        System.out.printf("Test Score: %d / %d\n", 
                          totalPassCount, 
                          totalTestCount);
        if(totalPassed)  
        {
            System.out.println("All Tests Passed.");
            System.exit(0);
        }
        else
        {   
            System.out.println("Tests Failed.");
            System.exit(-1);
        }        
    }


    // Add Weighted Edges (Code Upgrade from Lab 3)
    // Add Weighted Edges (Code Upgrade from Lab 3)
    // Add Weighted Edges (Code Upgrade from Lab 3)

    private static void testAddWeightedEdge1()
    {
        // Setup
        System.out.println("============testAddWeightedEdge1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { 
            new Tile(0, 0, 'I', -5), 
            new Tile(4, 0, 'I', -5),
            new Tile(0, 4, 'I', -5),
            new Tile(5, 5, 'I', -5),
            new Tile(5, 10, 'I', -5)
        };

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 4), //0
            new WeightedEdge(tileArray[2], 2), //0
            new WeightedEdge(tileArray[4], 8), //1
            new WeightedEdge(tileArray[3], 7), //1
            new WeightedEdge(tileArray[3], 2), //2
            new WeightedEdge(tileArray[4], 1)  //3
        };

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);
            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[5].getTile(), wEdge[5].getPenalty());

        
        // Action
        LinkedList<WeightedEdge> tempList;              
        boolean tempResult;
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[0].printTileCoord();
        System.out.println();

        tempList = testGraph.adjList.get(tileArray[0]);                
        passed &= assertEquals(2, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[2]) || 
                         testWEdge.isEqual(wEdge[3]) || 
                         testWEdge.isEqual(wEdge[4]) ||
                         testWEdge.isEqual(wEdge[5]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[0]) || 
                         testWEdge.isEqual(wEdge[1]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[0]))
                passed &= assertEquals(wEdge[0].getPenalty(), testWEdge.getPenalty());
            else if(testWEdge.isEqual(wEdge[1]))
                passed &= assertEquals(wEdge[1].getPenalty(), testWEdge.getPenalty());
        }        

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[1].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[1]);              
        passed &= assertEquals(2, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) || 
                         testWEdge.isEqual(wEdge[1]) || 
                         testWEdge.isEqual(wEdge[4]) || 
                         testWEdge.isEqual(wEdge[5]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[2]) ||
                         testWEdge.isEqual(wEdge[3]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[2]))
                passed &= assertEquals(wEdge[2].getPenalty(), testWEdge.getPenalty());
            else if(testWEdge.isEqual(wEdge[3]))
                passed &= assertEquals(wEdge[3].getPenalty(), testWEdge.getPenalty());
        }

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[2].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[2]);              
        passed &= assertEquals(1, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) || 
                         testWEdge.isEqual(wEdge[1]) || 
                         testWEdge.isEqual(wEdge[2]) || 
                         testWEdge.isEqual(wEdge[3]) || 
                         testWEdge.isEqual(wEdge[5]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[4]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[4]))
                passed &= assertEquals(wEdge[4].getPenalty(), testWEdge.getPenalty());
        }

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[3].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[3]);                      
        passed &= assertEquals(1, tempList.size());
        
        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) || 
                         testWEdge.isEqual(wEdge[1]) || 
                         testWEdge.isEqual(wEdge[2]) || 
                         testWEdge.isEqual(wEdge[3]) ||
                         testWEdge.isEqual(wEdge[4]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[5]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[5]))
                passed &= assertEquals(wEdge[5].getPenalty(), testWEdge.getPenalty());
        }

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[4].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[4]);
        passed &= assertEquals(true, tempList.isEmpty());

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void testAddWeightedEdge2()
    {
        // Setup
        System.out.println("============testAddWeightedEdge2=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(0, 4, 'I', -5),
                              new Tile(4, 0, 'I', -5),
                              new Tile(4, 8, 'I', -5),                              
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5),
                              new Tile(10, 16, 'I', -5), 
                              new Tile(10, 23, 'I', -5)};

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 4), //0
            new WeightedEdge(tileArray[2], 2), //0
            new WeightedEdge(tileArray[3], 8), //0
            new WeightedEdge(tileArray[4], 3), //1
            new WeightedEdge(tileArray[5], 1), //1            
            new WeightedEdge(tileArray[3], 4), //2
            new WeightedEdge(tileArray[4], 2), //2
            new WeightedEdge(tileArray[6], 6), //3
            new WeightedEdge(tileArray[5], 5), //4
            new WeightedEdge(tileArray[6], 1), //5            
            new WeightedEdge(tileArray[7], 3), //5
            new WeightedEdge(tileArray[7], 1)  //6
        };

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[5].getTile(), wEdge[5].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[6].getTile(), wEdge[6].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[7].getTile(), wEdge[7].getPenalty());
        testGraph.addEdge(tileArray[4], wEdge[8].getTile(), wEdge[8].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[9].getTile(), wEdge[9].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[10].getTile(), wEdge[10].getPenalty());
        testGraph.addEdge(tileArray[6], wEdge[11].getTile(), wEdge[11].getPenalty());

        
        // Action
        LinkedList<WeightedEdge> tempList;              
        boolean tempResult;
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[0].printTileCoord();
        System.out.println();

        tempList = testGraph.adjList.get(tileArray[0]);                
        passed &= assertEquals(3, tempList.size());      
        
        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[3]) || 
                         testWEdge.isEqual(wEdge[4]) || 
                         testWEdge.isEqual(wEdge[5]) || 
                         testWEdge.isEqual(wEdge[6]) ||
                         testWEdge.isEqual(wEdge[7]) || 
                         testWEdge.isEqual(wEdge[8]) ||
                         testWEdge.isEqual(wEdge[9]) || 
                         testWEdge.isEqual(wEdge[10]) ||
                         testWEdge.isEqual(wEdge[11]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[0]) || 
                         testWEdge.isEqual(wEdge[1]) || 
                         testWEdge.isEqual(wEdge[2]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[0]))
                passed &= assertEquals(wEdge[0].getPenalty(), testWEdge.getPenalty());
            else if(testWEdge.isEqual(wEdge[1]))
                passed &= assertEquals(wEdge[1].getPenalty(), testWEdge.getPenalty());
            else if(testWEdge.isEqual(wEdge[2]))
                passed &= assertEquals(wEdge[2].getPenalty(), testWEdge.getPenalty());
        }        

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[1].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[1]);              
        passed &= assertEquals(2, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) ||  
                         testWEdge.isEqual(wEdge[1]) ||  
                         testWEdge.isEqual(wEdge[2]) || 
                         testWEdge.isEqual(wEdge[5]) || 
                         testWEdge.isEqual(wEdge[6]) ||
                         testWEdge.isEqual(wEdge[7]) || 
                         testWEdge.isEqual(wEdge[8]) ||
                         testWEdge.isEqual(wEdge[9]) ||
                         testWEdge.isEqual(wEdge[10]) ||
                         testWEdge.isEqual(wEdge[11]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[3]) ||
                         testWEdge.isEqual(wEdge[4]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[3]))
                passed &= assertEquals(wEdge[3].getPenalty(), testWEdge.getPenalty());
            else if(testWEdge.isEqual(wEdge[4]))
                passed &= assertEquals(wEdge[4].getPenalty(), testWEdge.getPenalty());
        } 

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[2].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[2]);              
        passed &= assertEquals(2, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) ||  
                         testWEdge.isEqual(wEdge[1]) ||  
                         testWEdge.isEqual(wEdge[2]) || 
                         testWEdge.isEqual(wEdge[3]) ||
                         testWEdge.isEqual(wEdge[4]) ||
                         testWEdge.isEqual(wEdge[7]) || 
                         testWEdge.isEqual(wEdge[8]) ||
                         testWEdge.isEqual(wEdge[9]) ||
                         testWEdge.isEqual(wEdge[10]) ||
                         testWEdge.isEqual(wEdge[11]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[5]) || 
                         testWEdge.isEqual(wEdge[6]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[5]))
                passed &= assertEquals(wEdge[5].getPenalty(), testWEdge.getPenalty());
            else if(testWEdge.isEqual(wEdge[6]))
                passed &= assertEquals(wEdge[6].getPenalty(), testWEdge.getPenalty());
        }

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[3].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[3]);              
        passed &= assertEquals(1, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) ||  
                         testWEdge.isEqual(wEdge[1]) ||  
                         testWEdge.isEqual(wEdge[2]) ||
                         testWEdge.isEqual(wEdge[3]) ||                          
                         testWEdge.isEqual(wEdge[4]) || 
                         testWEdge.isEqual(wEdge[5]) ||
                         testWEdge.isEqual(wEdge[6]) || 
                         testWEdge.isEqual(wEdge[8]) ||
                         testWEdge.isEqual(wEdge[9]) ||
                         testWEdge.isEqual(wEdge[10]) ||
                         testWEdge.isEqual(wEdge[11]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[7]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[7]))
                passed &= assertEquals(wEdge[7].getPenalty(), testWEdge.getPenalty());
            
        } 

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[4].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[4]);                      
        passed &= assertEquals(1, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) ||  
                         testWEdge.isEqual(wEdge[1]) ||  
                         testWEdge.isEqual(wEdge[2]) ||
                         testWEdge.isEqual(wEdge[3]) ||                          
                         testWEdge.isEqual(wEdge[4]) || 
                         testWEdge.isEqual(wEdge[5]) ||
                         testWEdge.isEqual(wEdge[6]) || 
                         testWEdge.isEqual(wEdge[7]) ||
                         testWEdge.isEqual(wEdge[9]) ||
                         testWEdge.isEqual(wEdge[10]) ||
                         testWEdge.isEqual(wEdge[11]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[8]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[8]))
                passed &= assertEquals(wEdge[8].getPenalty(), testWEdge.getPenalty());
            
        } 

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[5].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[5]);                      
        passed &= assertEquals(2, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) ||  
                         testWEdge.isEqual(wEdge[1]) ||  
                         testWEdge.isEqual(wEdge[2]) || 
                         testWEdge.isEqual(wEdge[3]) ||
                         testWEdge.isEqual(wEdge[4]) ||
                         testWEdge.isEqual(wEdge[5]) || 
                         testWEdge.isEqual(wEdge[6]) ||
                         testWEdge.isEqual(wEdge[7]) || 
                         testWEdge.isEqual(wEdge[8]) ||
                         testWEdge.isEqual(wEdge[11]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[9]) ||
                         testWEdge.isEqual(wEdge[10]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[9]))
                passed &= assertEquals(wEdge[9].getPenalty(), testWEdge.getPenalty());
            else if(testWEdge.isEqual(wEdge[10]))
                passed &= assertEquals(wEdge[10].getPenalty(), testWEdge.getPenalty());
        }

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[6].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[6]);                      
        passed &= assertEquals(1, tempList.size());

        for(WeightedEdge testWEdge : tempList)
        {
            tempResult = testWEdge.isEqual(wEdge[0]) ||  
                         testWEdge.isEqual(wEdge[1]) ||  
                         testWEdge.isEqual(wEdge[2]) ||
                         testWEdge.isEqual(wEdge[3]) ||                          
                         testWEdge.isEqual(wEdge[4]) || 
                         testWEdge.isEqual(wEdge[5]) ||
                         testWEdge.isEqual(wEdge[6]) || 
                         testWEdge.isEqual(wEdge[7]) ||
                         testWEdge.isEqual(wEdge[8]) ||
                         testWEdge.isEqual(wEdge[9]) ||
                         testWEdge.isEqual(wEdge[10]);
            
            passed &= assertEquals(false, tempResult);

            tempResult = testWEdge.isEqual(wEdge[11]);

            passed &= assertEquals(true, tempResult);

            if(testWEdge.isEqual(wEdge[11]))
                passed &= assertEquals(wEdge[11].getPenalty(), testWEdge.getPenalty());
            
        } 

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[7].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[7]);
        passed &= assertEquals(true, tempList.isEmpty());

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testAddWeightedEdgeCustom()
    {
        // Setup
        System.out.println("============testAddWeightedEdgeCustom=============");
        boolean passed = true;
        totalTestCount++;

        // Add your own custom test here
        // Design another case to test your edge insertion with minimally 8
        // vertices and 12 edges

        // WARNING!! remove these lines when adding test case here
        System.out.println("Did you add the Custom Test Case?");
        passed &= false;
        // WARNING!! remove these lines when adding test case here

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    

    // Topological Sort
    // Topological Sort
    // Topological Sort

    private static void testTopologicalSort1()
    {
        // Setup
        System.out.println("============testTopologicalSort1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { 
            new Tile(0, 0, 'I', -5), 
            new Tile(4, 0, 'I', -5),
            new Tile(0, 4, 'I', -5),
            new Tile(5, 5, 'I', -5),
            new Tile(5, 10, 'I', -5)
        };

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 4), //0
            new WeightedEdge(tileArray[2], 2), //0
            new WeightedEdge(tileArray[4], 8), //1
            new WeightedEdge(tileArray[3], 7), //1
            new WeightedEdge(tileArray[3], 2), //2
            new WeightedEdge(tileArray[4], 1)  //3
        };

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);
            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[5].getTile(), wEdge[5].getPenalty());

        
        // Action
        LinkedList<Tile> sorted = testGraph.topologicalSort();

        if(sorted != null)
        {
            // Valid solutions:
            // 1: 0,1,2,3,4
            // 2: 0,2,1,3,4
            boolean solution1 = sorted.get(0).equals(tileArray[0]) &&
                            ((sorted.get(1).equals(tileArray[1]) && sorted.get(2).equals(tileArray[2])) ||
                                (sorted.get(1).equals(tileArray[2]) && sorted.get(2).equals(tileArray[1]))) &&
                            sorted.get(3).equals(tileArray[3]) &&
                            sorted.get(4).equals(tileArray[4]);
            
            passed &= solution1;
        }
        else
        {
            passed = false;
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void testTopologicalSort2()
    {
        // Setup
        System.out.println("============testTopologicalSort2=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                            new Tile(0, 4, 'I', -5),
                            new Tile(4, 0, 'I', -5),
                            new Tile(4, 8, 'I', -5),                              
                            new Tile(5, 5, 'I', -5),
                            new Tile(5, 10, 'I', -5),
                            new Tile(10, 16, 'I', -5), 
                            new Tile(10, 23, 'I', -5)};

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 4), //0
            new WeightedEdge(tileArray[2], 2), //0
            new WeightedEdge(tileArray[3], 8), //0
            new WeightedEdge(tileArray[4], 3), //1
            new WeightedEdge(tileArray[5], 1), //1            
            new WeightedEdge(tileArray[3], 4), //2
            new WeightedEdge(tileArray[4], 2), //2
            new WeightedEdge(tileArray[6], 6), //3
            new WeightedEdge(tileArray[5], 5), //4
            new WeightedEdge(tileArray[6], 1), //5            
            new WeightedEdge(tileArray[7], 3), //5
            new WeightedEdge(tileArray[7], 1)  //6
        };

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[5].getTile(), wEdge[5].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[6].getTile(), wEdge[6].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[7].getTile(), wEdge[7].getPenalty());
        testGraph.addEdge(tileArray[4], wEdge[8].getTile(), wEdge[8].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[9].getTile(), wEdge[9].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[10].getTile(), wEdge[10].getPenalty());
        testGraph.addEdge(tileArray[6], wEdge[11].getTile(), wEdge[11].getPenalty());

        
        // Action
        LinkedList<Tile> sorted = testGraph.topologicalSort();

        if(sorted != null)
        {
            // Check if the solution is one of the valid topological sorts
            boolean validSolution = false;
            
            if (sorted.get(0).equals(tileArray[0])) {

                
                int[] indexMap = new int[tileArray.length];
                for (int i = 0; i < sorted.size(); i++) {
                    for (int j = 0; j < tileArray.length; j++) {
                        if (sorted.get(i).equals(tileArray[j])) {
                            indexMap[j] = i;
                            break;
                        }
                    }
                }
                
                // Check all dependencies
                validSolution = true;

                validSolution &= indexMap[1] > indexMap[0];

                validSolution &= indexMap[2] > indexMap[0];

                validSolution &= indexMap[3] > indexMap[0];
                validSolution &= indexMap[3] > indexMap[2];

                validSolution &= indexMap[4] > indexMap[1];
                validSolution &= indexMap[4] > indexMap[2];

                validSolution &= indexMap[5] > indexMap[1];

                validSolution &= indexMap[6] > indexMap[3];
                validSolution &= indexMap[6] > indexMap[5];

                validSolution &= indexMap[7] > indexMap[5];
                validSolution &= indexMap[7] > indexMap[6];
            }
            
            passed &= validSolution;
        }
        else
        {
            passed = false;
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testTopologicalSortCustom()
    {
        // Setup
        System.out.println("============testTopologicalSortCustom=============");
        boolean passed = true;
        totalTestCount++;

        // Add your own custom test here
        // Design another case to test your edge insertion with minimally 8
        // vertices and 12 edges

        // WARNING!! remove these lines when adding test case here
        System.out.println("Did you add the Custom Test Case?");
        passed &= false;
        // WARNING!! remove these lines when adding test case here

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    

    // Shortest Path for Directed Acyclic Graph using Topological Sort
    // Shortest Path for Directed Acyclic Graph using Topological Sort
    // Shortest Path for Directed Acyclic Graph using Topological Sort

    private static void testShortestPathDAG1()
    {
        // Setup
        System.out.println("============testShortestPathDAG1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { 
            new Tile(0, 0, 'I', -5), 
            new Tile(4, 0, 'I', -5),
            new Tile(0, 4, 'I', -5),
            new Tile(5, 5, 'I', -5),
            new Tile(5, 10, 'I', -5)
        };

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 4), //0
            new WeightedEdge(tileArray[2], 2), //0
            new WeightedEdge(tileArray[4], 8), //1
            new WeightedEdge(tileArray[3], 7), //1
            new WeightedEdge(tileArray[3], 2), //2
            new WeightedEdge(tileArray[4], 1)  //3
        };

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);
            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[5].getTile(), wEdge[5].getPenalty());

        
        // Action
        LinkedList<Tile> path = testGraph.DAGShortestPath(tileArray[0], tileArray[4]);
        
        if(path != null)
        {
            passed &= assertEquals(tileArray[4], path.get(0));
            passed &= assertEquals(tileArray[3], path.get(1));
            passed &= assertEquals(tileArray[2], path.get(2));
            passed &= assertEquals(tileArray[0], path.get(3));
        }
        else
        {
            passed = false;
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testShortestPathDAG2()
    {
        // Setup
        System.out.println("============testShortestPathDAG2=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(0, 4, 'I', -5),
                              new Tile(4, 0, 'I', -5),
                              new Tile(4, 8, 'I', -5),                              
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5),
                              new Tile(10, 16, 'I', -5), 
                              new Tile(10, 23, 'I', -5)};

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 4), //0
            new WeightedEdge(tileArray[2], 2), //0
            new WeightedEdge(tileArray[3], 8), //0
            new WeightedEdge(tileArray[4], 3), //1
            new WeightedEdge(tileArray[5], 1), //1            
            new WeightedEdge(tileArray[3], 4), //2
            new WeightedEdge(tileArray[4], 2), //2
            new WeightedEdge(tileArray[6], 6), //3
            new WeightedEdge(tileArray[5], 5), //4
            new WeightedEdge(tileArray[6], 1), //5            
            new WeightedEdge(tileArray[7], 3), //5
            new WeightedEdge(tileArray[7], 1)  //6
        };

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[5].getTile(), wEdge[5].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[6].getTile(), wEdge[6].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[7].getTile(), wEdge[7].getPenalty());
        testGraph.addEdge(tileArray[4], wEdge[8].getTile(), wEdge[8].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[9].getTile(), wEdge[9].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[10].getTile(), wEdge[10].getPenalty());
        testGraph.addEdge(tileArray[6], wEdge[11].getTile(), wEdge[11].getPenalty());

        
        // Action
        LinkedList<Tile> path = testGraph.DAGShortestPath(tileArray[0], tileArray[7]);

        if(path != null)
        {
            passed &= assertEquals(tileArray[7], path.get(0));
            passed &= assertEquals(tileArray[6], path.get(1));
            passed &= assertEquals(tileArray[5], path.get(2));
            passed &= assertEquals(tileArray[1], path.get(3));
            passed &= assertEquals(tileArray[0], path.get(4));        
        }
        else
        {
            passed = false;
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testShortestPathDAGCustom()
    {
        // Setup
        System.out.println("============testShortestPathDAGCustom=============");
        boolean passed = true;
        totalTestCount++;

        // Add your own custom test here
        // Design another case to test your edge insertion with minimally 8
        // vertices and 12 edges

        // WARNING!! remove these lines when adding test case here
        System.out.println("Did you add the Custom Test Case?");
        passed &= false;
        // WARNING!! remove these lines when adding test case here

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    

    // Shortest Path using Dijkastra Algorithm on Positive-Weighted Directed Graph
    // Shortest Path using Dijkastra Algorithm on Positive-Weighted Directed Graph
    // Shortest Path using Dijkastra Algorithm on Positive-Weighted Directed Graph

    private static void testShortestPathDijkastra1()
    {
        // Setup
        System.out.println("============testShortestPathDijkastra1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { 
            new Tile(0, 0, 'I', -5), 
            new Tile(4, 0, 'I', -5),
            new Tile(0, 4, 'I', -5),
            new Tile(5, 5, 'I', -5),
            new Tile(5, 10, 'I', -5)
        };

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 4), //0
            new WeightedEdge(tileArray[2], 6), //0
            new WeightedEdge(tileArray[4], 5), //1
            new WeightedEdge(tileArray[3], 1), //1
            new WeightedEdge(tileArray[3], 10), //2
            new WeightedEdge(tileArray[4], 7)  //3
        };

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);
            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[5].getTile(), wEdge[5].getPenalty());

        
        // Action
        LinkedList<Tile> path = testGraph.DijkstraShortestPath(tileArray[0], tileArray[4]);
        
        if(path != null)
        {
            passed &= assertEquals(tileArray[4], path.get(0));
            passed &= assertEquals(tileArray[1], path.get(1));
            passed &= assertEquals(tileArray[0], path.get(2));
        }
        else
        {
            passed = false;
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testShortestPathDijkastra2()
    {
        // Setup
        System.out.println("============testShortestPathDijkastra2=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(0, 4, 'I', -5),
                              new Tile(4, 0, 'I', -5),
                              new Tile(4, 8, 'I', -5),                              
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5),
                              new Tile(10, 16, 'I', -5), 
                              new Tile(10, 23, 'I', -5)};

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 5), //0
            new WeightedEdge(tileArray[2], 4), //0
            new WeightedEdge(tileArray[3], 2), //0
            new WeightedEdge(tileArray[4], 7), //1
            new WeightedEdge(tileArray[5], 11), //1            
            new WeightedEdge(tileArray[3], 3), //2
            new WeightedEdge(tileArray[4], 5), //2
            new WeightedEdge(tileArray[6], 1), //3
            new WeightedEdge(tileArray[5], 2), //4
            new WeightedEdge(tileArray[6], 7), //5            
            new WeightedEdge(tileArray[7], 6), //5
            new WeightedEdge(tileArray[7], 10)  //6
        };

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[5].getTile(), wEdge[5].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[6].getTile(), wEdge[6].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[7].getTile(), wEdge[7].getPenalty());
        testGraph.addEdge(tileArray[4], wEdge[8].getTile(), wEdge[8].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[9].getTile(), wEdge[9].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[10].getTile(), wEdge[10].getPenalty());
        testGraph.addEdge(tileArray[6], wEdge[11].getTile(), wEdge[11].getPenalty());

        
        // Action
        LinkedList<Tile> path = testGraph.DijkstraShortestPath(tileArray[0], tileArray[7]);
        
        if(path != null)
        {
            passed &= assertEquals(tileArray[7], path.get(0));
            passed &= assertEquals(tileArray[6], path.get(1));
            passed &= assertEquals(tileArray[3], path.get(2));
            passed &= assertEquals(tileArray[0], path.get(3));   
        }
        else
        {
            passed = false;
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testShortestPathDijkastraCustom()
    {
        // Setup
        System.out.println("============testShortestPathDijkastraCustom=============");
        boolean passed = true;
        totalTestCount++;

        // Add your own custom test here
        // Design another case to test your edge insertion with minimally 8
        // vertices and 12 edges

        // WARNING!! remove these lines when adding test case here
        System.out.println("Did you add the Custom Test Case?");
        passed &= false;
        // WARNING!! remove these lines when adding test case here

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    

    // Shortest Path using Bellman-Ford Algorithm on General-Weighted Graph
    // Shortest Path using Bellman-Ford Algorithm on General-Weighted Graph
    // Shortest Path using Bellman-Ford Algorithm on General-Weighted Graph

    private static void testShortestPathBellmanFord1()
    {
        // Setup
        System.out.println("============testShortestPathBellmanFord1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { 
            new Tile(0, 0, 'I', -5), 
            new Tile(4, 0, 'I', -5),
            new Tile(0, 4, 'I', -5),
            new Tile(5, 5, 'I', -5),
            new Tile(5, 10, 'I', -5)
        };

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 2), //0
            new WeightedEdge(tileArray[2], 4), //0
            new WeightedEdge(tileArray[4], -3), //1
            new WeightedEdge(tileArray[3], -4), //1
            new WeightedEdge(tileArray[3], 3), //2
            new WeightedEdge(tileArray[4], -1)  //3
        };

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);
            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[5].getTile(), wEdge[5].getPenalty());

        
        // Action
        LinkedList<Tile> path = testGraph.BellmanShortestPath(tileArray[0], tileArray[4]);
        
        if(path != null)
        {
            passed &= assertEquals(tileArray[4], path.get(0));
            passed &= assertEquals(tileArray[3], path.get(1));
            passed &= assertEquals(tileArray[1], path.get(2));
            passed &= assertEquals(tileArray[0], path.get(3));
        }
        else
        {
            passed = false;
        }   

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testShortestPathBellmanFord2()
    {
        // Setup
        System.out.println("============testShortestPathBellmanFord2=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(0, 4, 'I', -5),
                              new Tile(4, 0, 'I', -5),
                              new Tile(4, 8, 'I', -5),                              
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5),
                              new Tile(10, 16, 'I', -5), 
                              new Tile(10, 23, 'I', -5)};

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 5), //0
            new WeightedEdge(tileArray[2], 4), //0
            new WeightedEdge(tileArray[3], 2), //0
            new WeightedEdge(tileArray[4], -3), //1
            new WeightedEdge(tileArray[5], 11), //1            
            new WeightedEdge(tileArray[3], -2), //2
            new WeightedEdge(tileArray[4], 5), //2
            new WeightedEdge(tileArray[6], 1), //3
            new WeightedEdge(tileArray[5], -4), //4
            new WeightedEdge(tileArray[6], 7), //5            
            new WeightedEdge(tileArray[7], -1), //5
            new WeightedEdge(tileArray[7], 10)  //6
        };

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[5].getTile(), wEdge[5].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[6].getTile(), wEdge[6].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[7].getTile(), wEdge[7].getPenalty());
        testGraph.addEdge(tileArray[4], wEdge[8].getTile(), wEdge[8].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[9].getTile(), wEdge[9].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[10].getTile(), wEdge[10].getPenalty());
        testGraph.addEdge(tileArray[6], wEdge[11].getTile(), wEdge[11].getPenalty());

        
        // Action
        LinkedList<Tile> path = testGraph.BellmanShortestPath(tileArray[0], tileArray[7]);

        if(path != null)
        {
            passed &= assertEquals(tileArray[7], path.get(0));
            passed &= assertEquals(tileArray[5], path.get(1));
            passed &= assertEquals(tileArray[4], path.get(2));
            passed &= assertEquals(tileArray[1], path.get(3));   
            passed &= assertEquals(tileArray[0], path.get(4));   
        }
        else
        {
            passed = false;
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void testNegativeCycleBellmanFord1()
    {
        // Setup
        System.out.println("============testNegativeCycleBellmanFord1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { 
            new Tile(0, 0, 'I', -5), 
            new Tile(4, 0, 'I', -5),
            new Tile(0, 4, 'I', -5),
            new Tile(5, 5, 'I', -5),
            new Tile(5, 10, 'I', -5)
        };

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 2), //0
            new WeightedEdge(tileArray[2], 4), //0
            new WeightedEdge(tileArray[4], -3), //1
            new WeightedEdge(tileArray[3], -4), //1
            new WeightedEdge(tileArray[3], 3), //2
            new WeightedEdge(tileArray[4], -1),  //3
            new WeightedEdge(tileArray[1], -4)  //4
        };

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);
            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[5].getTile(), wEdge[5].getPenalty());
        testGraph.addEdge(tileArray[4], wEdge[6].getTile(), wEdge[6].getPenalty());

        
        // Action
        LinkedList<Tile> path = testGraph.BellmanShortestPath(tileArray[0], tileArray[4]);        
        passed &= assertEquals(true, path == null);
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void testNegativeCycleBellmanFord2()
    {
        // Setup
        System.out.println("============testNegativeCycleBellmanFord2=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(0, 4, 'I', -5),
                              new Tile(4, 0, 'I', -5),
                              new Tile(4, 8, 'I', -5),                              
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5),
                              new Tile(10, 16, 'I', -5), 
                              new Tile(10, 23, 'I', -5)};

        WeightedEdge wEdge[] = {
            new WeightedEdge(tileArray[1], 5), //0
            new WeightedEdge(tileArray[2], 4), //0
            new WeightedEdge(tileArray[3], 2), //0
            new WeightedEdge(tileArray[4], -3), //1
            new WeightedEdge(tileArray[5], 11), //1            
            new WeightedEdge(tileArray[3], -2), //2
            new WeightedEdge(tileArray[4], 5), //2
            new WeightedEdge(tileArray[6], 1), //3
            new WeightedEdge(tileArray[5], -4), //4
            new WeightedEdge(tileArray[6], 7), //5            
            new WeightedEdge(tileArray[1], 2), //5  
            new WeightedEdge(tileArray[7], -1), //5
            new WeightedEdge(tileArray[7], 10)  //6
        };

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);            

        testGraph.addEdge(tileArray[0], wEdge[0].getTile(), wEdge[0].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[1].getTile(), wEdge[1].getPenalty());
        testGraph.addEdge(tileArray[0], wEdge[2].getTile(), wEdge[2].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[3].getTile(), wEdge[3].getPenalty());
        testGraph.addEdge(tileArray[1], wEdge[4].getTile(), wEdge[4].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[5].getTile(), wEdge[5].getPenalty());
        testGraph.addEdge(tileArray[2], wEdge[6].getTile(), wEdge[6].getPenalty());
        testGraph.addEdge(tileArray[3], wEdge[7].getTile(), wEdge[7].getPenalty());
        testGraph.addEdge(tileArray[4], wEdge[8].getTile(), wEdge[8].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[9].getTile(), wEdge[9].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[10].getTile(), wEdge[10].getPenalty());
        testGraph.addEdge(tileArray[5], wEdge[11].getTile(), wEdge[11].getPenalty());
        testGraph.addEdge(tileArray[6], wEdge[12].getTile(), wEdge[12].getPenalty());

        
        // Action
        LinkedList<Tile> path = testGraph.BellmanShortestPath(tileArray[0], tileArray[7]);
        passed &= assertEquals(true, path == null);
        

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testShortestPathBellmanFordCustom()
    {
        // Setup
        System.out.println("============testShortestPathBellmanFordCustom=============");
        boolean passed = true;
        totalTestCount++;

        // Add your own custom test here
        // Design another case to test your edge insertion with minimally 8
        // vertices and 12 edges

        // WARNING!! remove these lines when adding test case here
        System.out.println("Did you add the Custom Test Case?");
        passed &= false;
        // WARNING!! remove these lines when adding test case here

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    

    


    ////// ASSERTIONS //////
    ////// ASSERTIONS //////
    ////// ASSERTIONS //////

    private static boolean assertEquals(Tile expected, Tile actual)
    {
        if(!expected.isEqual(actual))
        {
            System.out.println("\tAssert Failed!");
            System.out.printf("\tExpected:");
            expected.printTile();
            expected.printTileCoord();
            System.out.printf("\tActual:");
            actual.printTile();
            actual.printTileCoord();
            return false;
        }

        return true;
    }

    private static boolean assertEquals(boolean expected, boolean actual)
    {
        if(expected != actual)
        {
            System.out.println("\tAssert Failed!");
            System.out.printf("\tExpected: %b, Actual: %b\n\n", expected, actual);
            return false;
        }

        return true;
    }

    private static boolean assertEquals(int expected, int actual)
    {
        if(expected != actual)
        {
            System.out.println("\tAssert Failed!");
            System.out.printf("\tExpected: %d, Actual: %d\n\n", expected, actual);
            return false;
        }

        return true;
    }
}
