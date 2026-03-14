package Lab3Model;

import java.util.*;

// This is the Graph of Tiles that we will use to build the Maze in Lab 4
// We will use the Adjacency List implementation.

// This object is under "SINGLETON OWNERSHIP" of the TielMap class
// Singleton = only one instance in the entire runtime.
public class TileGraph {

    private Map<Tile, LinkedList<Tile>> adjList; // a collection of pairs (Tile, LinkedList), where the 1st member is a vertex, the 2nd is the adjacency list of that vertex
    // Read the documentation  specification of java built-in interface Map<K,V> in package java.util of module java.base

    //constructor: instantiates an empty graph 
    public TileGraph()
    {
        adjList = new HashMap<Tile, LinkedList<Tile>>(); //creates an empty graph    
    }

    //constructor: instantiates a graph according to mRef
    public TileGraph(TileMap mRef)
    {
        adjList = new HashMap<>(); //creates an empty graph
        buildGraph(mRef); //populates the graph according to mREf
    }

    // Problem 1: Populate the graph 
    //////////////////////////////////////////////////////
    
    // Method to add thisTile as a new Vertex (with an empty adjacency list), if it is not already in the graph
    private void addVertex(Tile thisTile)
    {
        adjList.putIfAbsent(thisTile, new LinkedList<Tile>()); // adds a new pair to the collection; the vertex is thisTile, its adjacency list is now empty
    }

    // Problem 1-1 - Add a DIRECTED edge from src to dst; 
    // assume that src is already a vertex in the graph 
    // add dst to the adjacency list (i.e., linked list) of src, 
    // only if dst is not already there
    private void addEdge(Tile src, Tile dst)
    {
        LinkedList<Tile> srcList = adjList.get(src); // returns a reference to the linked list paired with src
        // Complete the rest of the code
        // ......
    } 

    // Problem 1-2 - Get the list of vertices adjacent to thisTile (return a reference to the linked list storing the adjacent vertices)
    //               You will need this for depth-first traversal and breadth-first search later
    private LinkedList<Tile> getAdjacentVertices(Tile thisTile)
    {
        
    }

    //Problem 1-3 - Add vertices and edges to the existing empty graph to build the graph corresponding to mRef
    // Vertices are intersections; edges are roads connecting two consecutive intersections from left to right or from up to down
    // Edges are directed: from up to down and from left to right
    // Must use an algorithm similar to BFS (for efficiency) to get the maximum grade
   
    private void buildGraph(TileMap mRef)
    {
        
    }

    // Problem 2 - Depth-First Traversal
    //             Return the list containing all the vertices visited 
    //             in Depth-First Traversal order from the start tile
    ////////////////////////////////////////////////////////////////////
    public LinkedList<Tile> depthFirstTraversal(Tile start)
    {
        
    }

    // Problem 3 - Find the Unweighted Shortest Path from start to end using Breadth-First Search (BFS)
    //             Return the list of all the vertices visited in this shortest path, in reversed order
    ////////////////////////////////////////////////////////////////////////////////////////
    public LinkedList<Tile> findShortestPath(Tile start, Tile end)
    {
        
    }

    //  Method to print the Entire Graph using printTile() and printTileCoord() from Tile class
    //                   In the format of Vertex : List of Neighbouring Vertices
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public void printGraph()
    {
        // Need Documentations on Set<>, Map<>, LinkedList<>, Collection<>, and Iterator<>

        Set<Tile> keySet = adjList.keySet(); // the set of all vertices
        Collection<LinkedList<Tile>> valueLists = adjList.values(); // the collection of all adjacency lists    

        Iterator<Tile> keySetIter = keySet.iterator();  // to iterate through the vertex set
        Iterator<LinkedList<Tile>> valueListsIter = valueLists.iterator();  // to iterate through all adjacency lists
        int size = keySet.size(); //  number of vertices

        //iterates through the vertex set; for each vertex, prints the vertex (i.e., tile coordinates) followed by the adjacent vertices
        for(int i = 0; i < size; i++)
        {
            keySetIter.next().printTileCoord();
            System.out.printf(" >>\t");
            valueListsIter.next().forEach(e -> {e.printTileCoord(); System.out.printf(" : ");});
            System.out.println();
        }
    }



    
    // Test Bench Below
    // Test Bench Below
    // Test Bench Below

    private static boolean totalPassed = true;
    private static int totalTestCount = 0;
    private static int totalPassCount = 0;

    public static void main(String args[])
    {
        testAddEdge1();
        testAddEdge2();
        testAddEdgeCustom();

        testGetAdjacentVertices1();
        testGetAdjacentVertices2();
        testGetAdjacentVerticesCustom();

        testDFT1();
        testDFT2();
        testDFTCustom();
        testFindShortestPath1();
        testFindShortestPath2();
        testFindShortestPathCustom();


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

    // Add Vertices and Edges
    // Add Vertices and Edges
    // Add Vertices and Edges

    private static void testAddVertex1()
    {
        // Setup
        System.out.println("============testAddVertex1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(4, 0, 'I', -5),
                              new Tile(0, 4, 'I', -5),
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5)};

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);

        // Action
        for(int i = 0; i < 5; i++)
        {
            System.out.printf(">> Check Tile: ");            
            tileArray[i].printTileCoord();
            System.out.println();

            passed &= assertEquals(true, testGraph.adjList.containsKey(tileArray[i]));
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void testAddVertex2()
    {
        // Setup
        System.out.println("============testAddVertex2=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(4, 0, 'I', -5),
                              new Tile(0, 4, 'I', -5),
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5),
                              new Tile(10, 16, 'I', -5), 
                              new Tile(10, 23, 'I', -5),
                              new Tile(4, 8, 'I', -5),
                              new Tile(16, 4, 'I', -5),
                              new Tile(16, 23, 'I', -5)};

        for(int i = 0; i < 10; i++)
            testGraph.addVertex(tileArray[i]);

        // Action
        for(int i = 0; i < 10; i++)
        {
            System.out.printf(">> Check Tile: ");            
            tileArray[i].printTileCoord();
            System.out.println();

            passed &= assertEquals(true, testGraph.adjList.containsKey(tileArray[i]));        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
      
    private static void testAddEdge1()
    {
        // Setup
        System.out.println("============testAddEdge1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(4, 0, 'I', -5),
                              new Tile(0, 4, 'I', -5),
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5)};

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);

        testGraph.addEdge(tileArray[0], tileArray[1]);
        testGraph.addEdge(tileArray[0], tileArray[2]);
        testGraph.addEdge(tileArray[1], tileArray[4]);
        testGraph.addEdge(tileArray[2], tileArray[3]);
        testGraph.addEdge(tileArray[3], tileArray[4]);


        // Action
        LinkedList<Tile> tempList;       
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[0].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[0]);
        passed &= assertEquals(true, tempList.contains(tileArray[1]));
        passed &= assertEquals(true, tempList.contains(tileArray[2]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[1].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[1]);
        passed &= assertEquals(true, tempList.contains(tileArray[4]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[2].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[2]);
        passed &= assertEquals(true, tempList.contains(tileArray[3]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[3].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[3]);
        passed &= assertEquals(true, tempList.contains(tileArray[4]));

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
    
    private static void testAddEdge2()
    {
        // Setup
        System.out.println("============testAddEdge2=============");
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

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);

        testGraph.addEdge(tileArray[0], tileArray[1]);
        testGraph.addEdge(tileArray[0], tileArray[2]);
        testGraph.addEdge(tileArray[0], tileArray[3]);
        testGraph.addEdge(tileArray[1], tileArray[4]);
        testGraph.addEdge(tileArray[1], tileArray[5]);
        testGraph.addEdge(tileArray[2], tileArray[3]);
        testGraph.addEdge(tileArray[2], tileArray[4]);
        testGraph.addEdge(tileArray[3], tileArray[6]);
        testGraph.addEdge(tileArray[4], tileArray[5]);
        testGraph.addEdge(tileArray[5], tileArray[6]);
        testGraph.addEdge(tileArray[5], tileArray[7]);
        testGraph.addEdge(tileArray[6], tileArray[7]);

        // Action
        LinkedList<Tile> tempList;       
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[0].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[0]);
        passed &= assertEquals(true, tempList.contains(tileArray[1]));
        passed &= assertEquals(true, tempList.contains(tileArray[2]));
        passed &= assertEquals(true, tempList.contains(tileArray[3]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[1].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[1]);
        passed &= assertEquals(true, tempList.contains(tileArray[4]));
        passed &= assertEquals(true, tempList.contains(tileArray[5]));
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[2].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[2]);
        passed &= assertEquals(true, tempList.contains(tileArray[3]));
        passed &= assertEquals(true, tempList.contains(tileArray[4]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[3].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[3]);
        passed &= assertEquals(true, tempList.contains(tileArray[6]));
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[4].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[4]);
        passed &= assertEquals(true, tempList.contains(tileArray[5]));
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[5].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[5]);
        passed &= assertEquals(true, tempList.contains(tileArray[6]));
        passed &= assertEquals(true, tempList.contains(tileArray[7]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[6].printTileCoord();
        System.out.println();
        tempList = testGraph.adjList.get(tileArray[6]);        
        passed &= assertEquals(true, tempList.contains(tileArray[7]));

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
    
    private static void testAddEdgeCustom()
    {
        // Setup
        System.out.println("============testAddEdgeCustom=============");
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
    


    // Remove Vertices and Edges
    // Remove Vertices and Edges
    // Remove Vertices and Edges

   
  
   
    
    


    
    // Get Adjacent Vertices
       private static void testGetAdjacentVertices1()
    {
        // Setup
        System.out.println("============testGetNeighbours1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(4, 0, 'I', -5),
                              new Tile(0, 4, 'I', -5),
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5)};

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);

        testGraph.addEdge(tileArray[0], tileArray[1]);
        testGraph.addEdge(tileArray[0], tileArray[2]);
        testGraph.addEdge(tileArray[1], tileArray[4]);
        testGraph.addEdge(tileArray[2], tileArray[3]);
        testGraph.addEdge(tileArray[3], tileArray[4]);


        // Action
        LinkedList<Tile> tempList;       
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[0].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[0]);
        passed &= assertEquals(true, tempList.contains(tileArray[1]));
        passed &= assertEquals(true, tempList.contains(tileArray[2]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[1].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[1]);
        passed &= assertEquals(true, tempList.contains(tileArray[4]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[2].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[2]);
        passed &= assertEquals(true, tempList.contains(tileArray[3]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[3].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[3]);
        passed &= assertEquals(true, tempList.contains(tileArray[4]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[4].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[4]);
        passed &= assertEquals(true, tempList.isEmpty());



        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testGetAdjacentVertices2()
    {
        // Setup
        System.out.println("============testAddEdge2=============");
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

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);

        testGraph.addEdge(tileArray[0], tileArray[1]);
        testGraph.addEdge(tileArray[0], tileArray[2]);
        testGraph.addEdge(tileArray[0], tileArray[3]);
        testGraph.addEdge(tileArray[1], tileArray[4]);
        testGraph.addEdge(tileArray[1], tileArray[5]);
        testGraph.addEdge(tileArray[2], tileArray[3]);
        testGraph.addEdge(tileArray[2], tileArray[4]);
        testGraph.addEdge(tileArray[3], tileArray[6]);
        testGraph.addEdge(tileArray[4], tileArray[5]);
        testGraph.addEdge(tileArray[5], tileArray[6]);
        testGraph.addEdge(tileArray[5], tileArray[7]);
        testGraph.addEdge(tileArray[6], tileArray[7]);

        // Action
        LinkedList<Tile> tempList;       
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[0].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[0]);
        passed &= assertEquals(true, tempList.contains(tileArray[1]));
        passed &= assertEquals(true, tempList.contains(tileArray[2]));
        passed &= assertEquals(true, tempList.contains(tileArray[3]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[1].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[1]);
        passed &= assertEquals(true, tempList.contains(tileArray[4]));
        passed &= assertEquals(true, tempList.contains(tileArray[5]));
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[2].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[2]);
        passed &= assertEquals(true, tempList.contains(tileArray[3]));
        passed &= assertEquals(true, tempList.contains(tileArray[4]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[3].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[3]);
        passed &= assertEquals(true, tempList.contains(tileArray[6]));
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[4].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[4]);
        passed &= assertEquals(true, tempList.contains(tileArray[5]));
        
        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[5].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[5]);
        passed &= assertEquals(true, tempList.contains(tileArray[6]));
        passed &= assertEquals(true, tempList.contains(tileArray[7]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[6].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[6]);        
        passed &= assertEquals(true, tempList.contains(tileArray[7]));

        System.out.printf(">> Check Vertex Adjacency List: ");
        tileArray[7].printTileCoord();
        System.out.println();
        tempList = testGraph.getAdjacentVertices(tileArray[7]);
        passed &= assertEquals(true, tempList.isEmpty());

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
       
    
    private static void testGetAdjacentVerticesCustom()
    {
        // Setup
        System.out.println("============testGetNeighboursCustom=============");
        boolean passed = true;
        totalTestCount++;

        // Add your own custom test here
        // Design another case to test your get neighbour method
        // You must have minimally 5 vertices and 5 edges in the graph,
        // Then test getNeighbours from at least one selected vertices.

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
    


    // Depth-First Traversal
    // Depth-First Traversal
    // Depth-First Traversal

    private static void testDFT1()
    {
        // Setup
        System.out.println("============testDFT1=============");
        boolean passed = true;
        totalTestCount++;

        TileGraph testGraph = new TileGraph();
        Tile tileArray[] =  { new Tile(0, 0, 'I', -5), 
                              new Tile(4, 0, 'I', -5),
                              new Tile(0, 4, 'I', -5),
                              new Tile(5, 5, 'I', -5),
                              new Tile(5, 10, 'I', -5)};

        for(int i = 0; i < 5; i++)
            testGraph.addVertex(tileArray[i]);

        testGraph.addEdge(tileArray[0], tileArray[1]);
        testGraph.addEdge(tileArray[0], tileArray[2]);
        testGraph.addEdge(tileArray[1], tileArray[4]);
        testGraph.addEdge(tileArray[2], tileArray[3]);
        testGraph.addEdge(tileArray[3], tileArray[4]);


        // Action
        LinkedList<Tile> dftList = testGraph.depthFirstTraversal(tileArray[0]);       

        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(0).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[0], dftList.get(0));
        
        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(1).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[1], dftList.get(1));
        
        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(2).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[4], dftList.get(2));
        
        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(3).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[2], dftList.get(3));
        
        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(4).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[3], dftList.get(4));


        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testDFT2()
    {
        // Setup
        System.out.println("============testDFT2=============");
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

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);

        testGraph.addEdge(tileArray[0], tileArray[1]);
        testGraph.addEdge(tileArray[0], tileArray[2]);
        testGraph.addEdge(tileArray[0], tileArray[3]);
        testGraph.addEdge(tileArray[1], tileArray[4]);
        testGraph.addEdge(tileArray[1], tileArray[5]);
        testGraph.addEdge(tileArray[2], tileArray[3]);
        testGraph.addEdge(tileArray[2], tileArray[4]);
        testGraph.addEdge(tileArray[3], tileArray[6]);
        testGraph.addEdge(tileArray[4], tileArray[5]);
        testGraph.addEdge(tileArray[5], tileArray[6]);
        testGraph.addEdge(tileArray[5], tileArray[7]);
        testGraph.addEdge(tileArray[6], tileArray[7]);

        // Action
        LinkedList<Tile> dftList = testGraph.depthFirstTraversal(tileArray[0]);       
        
        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(0).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[0], dftList.get(0));

        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(1).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[1], dftList.get(1));

        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(2).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[4], dftList.get(2));

        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(3).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[5], dftList.get(3));

        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(4).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[6], dftList.get(4));

        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(5).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[7], dftList.get(5));

        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(6).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[2], dftList.get(6));

        System.out.printf(">> Check DFT Resultant List: ");
        dftList.get(7).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[3], dftList.get(7));

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testDFTCustom()
    {
        // Setup
        System.out.println("============testDFTCustom=============");
        boolean passed = true;
        totalTestCount++;

        // Add your own custom test here
        // Design another case to test your get neighbour method
        // You must have minimally 8 vertices and 12 edges in the graph,
        // Then carry out DFT from a selected vertex.

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
    


    // Find Shortest Path using Breadth-First Search
    // Find Shortest Path using Breadth-First Search
    // Find Shortest Path using Breadth-First Search

    private static void testFindShortestPath1()
    {
        // Setup
        System.out.println("============testFindShortestPath1=============");
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

        for(int i = 0; i < 8; i++)
            testGraph.addVertex(tileArray[i]);

        testGraph.addEdge(tileArray[0], tileArray[1]);
        testGraph.addEdge(tileArray[0], tileArray[2]);
        testGraph.addEdge(tileArray[0], tileArray[3]);
        testGraph.addEdge(tileArray[1], tileArray[4]);
        testGraph.addEdge(tileArray[1], tileArray[5]);
        testGraph.addEdge(tileArray[2], tileArray[3]);
        testGraph.addEdge(tileArray[2], tileArray[4]);
        testGraph.addEdge(tileArray[3], tileArray[6]);
        testGraph.addEdge(tileArray[4], tileArray[5]);
        testGraph.addEdge(tileArray[5], tileArray[6]);
        testGraph.addEdge(tileArray[5], tileArray[7]);
        testGraph.addEdge(tileArray[6], tileArray[7]);

        // Action
        LinkedList<Tile> pathList = testGraph.findShortestPath(tileArray[0], tileArray[7]);       
        
        // for(int i = 0; i < pathList.size(); i++)
        // {
        //     pathList.get(i).printTileCoord();
        //     System.out.println();
        // }

        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(0).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[7], pathList.get(0));

        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(1).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[6], pathList.get(1));

        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(2).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[3], pathList.get(2));

        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(3).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[0], pathList.get(3));

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testFindShortestPath2()
    {
        // Setup
        System.out.println("============testFindShortestPath2=============");
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
                              new Tile(10, 23, 'I', -5),
                              new Tile(5, 13, 'I', -5),
                              new Tile(10, 13, 'I', -5),
                              new Tile(16, 23, 'I', -5), 
                              new Tile(16, 25, 'I', -5)};

        for(int i = 0; i < 12; i++)
            testGraph.addVertex(tileArray[i]);

        testGraph.addEdge(tileArray[0], tileArray[1]);
        testGraph.addEdge(tileArray[0], tileArray[2]);
        testGraph.addEdge(tileArray[0], tileArray[3]);
        testGraph.addEdge(tileArray[1], tileArray[4]);
        testGraph.addEdge(tileArray[1], tileArray[5]);
        testGraph.addEdge(tileArray[2], tileArray[3]);
        testGraph.addEdge(tileArray[2], tileArray[4]);
        testGraph.addEdge(tileArray[3], tileArray[6]);
        testGraph.addEdge(tileArray[3], tileArray[8]);
        testGraph.addEdge(tileArray[4], tileArray[5]);
        testGraph.addEdge(tileArray[5], tileArray[6]);
        testGraph.addEdge(tileArray[5], tileArray[7]);
        testGraph.addEdge(tileArray[5], tileArray[9]);
        testGraph.addEdge(tileArray[6], tileArray[7]);
        testGraph.addEdge(tileArray[6], tileArray[10]);
        testGraph.addEdge(tileArray[7], tileArray[11]);
        testGraph.addEdge(tileArray[8], tileArray[6]);
        testGraph.addEdge(tileArray[8], tileArray[10]);
        testGraph.addEdge(tileArray[8], tileArray[9]);
        testGraph.addEdge(tileArray[9], tileArray[11]);
        testGraph.addEdge(tileArray[10], tileArray[11]);
        

        // Action
        LinkedList<Tile> pathList = testGraph.findShortestPath(tileArray[0], tileArray[11]);       
        
        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(0).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[11], pathList.get(0));

        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(1).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[9], pathList.get(1));

        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(2).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[8], pathList.get(2));

        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(3).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[3], pathList.get(3));

        System.out.printf(">> Check Shortest Path List (BFS): ");
        pathList.get(4).printTileCoord();
        System.out.println();
        passed &= assertEquals(tileArray[0], pathList.get(4));

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testFindShortestPathCustom()
    {
        // Setup
        System.out.println("============testFindShortestPathCustom=============");
        boolean passed = true;
        totalTestCount++;

        // Add your own custom test here
        // Design another case to test your get neighbour method
        // You must have minimally 8 vertices and 12 edges in the graph,
        // Then carry out Find Shortest Path from a selected Starting vertex to
        // a selected Goal vertex

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
}

