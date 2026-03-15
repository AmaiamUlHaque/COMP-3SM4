public LinkedList<Tile> findShortestPath(Tile start, Tile end)
{
    // if start or end is null, return empty list
    if (start == null || end == null) {
        return new LinkedList<>();
    }
    
    // Check if start equals end
    if (start.isEqual(end)) {
        LinkedList<Tile> path = new LinkedList<>();
        path.add(start);
        return path;
    }
    
    Queue<Tile> queue = new LinkedList<>(); // queue for BFS
    Set<Tile> visited = new HashSet<>(); // track visited vertices
    Map<Tile, Tile> parent = new HashMap<>(); // track parent of each vertex to reconstruct path
    
    // initialize
    queue.add(start);
    visited.add(start);
    parent.put(start, null);
    
    boolean found = false;
    
    // BFS loop
    while (!queue.isEmpty() && !found) {
        Tile current = queue.poll();
        
        // get all neighbors
        LinkedList<Tile> neighbors = getAdjacentVertices(current);
        if (neighbors != null) {
            // Process neighbors in order: first RIGHT, then DOWN
            // Create two lists to prioritize right then down
            List<Tile> rightNeighbors = new ArrayList<>();
            List<Tile> downNeighbors = new ArrayList<>();
            
            for (Tile neighbor : neighbors) {
                if (neighbor.getX() > current.getX()) {
                    rightNeighbors.add(neighbor); // Right neighbor
                } else if (neighbor.getY() > current.getY()) {
                    downNeighbors.add(neighbor); // Down neighbor
                }
            }
            
            // Sort right neighbors by x (ascending)
            Collections.sort(rightNeighbors, (t1, t2) -> 
                Integer.compare(t1.getX(), t2.getX()));
            
            // Sort down neighbors by y (ascending)
            Collections.sort(downNeighbors, (t1, t2) -> 
                Integer.compare(t1.getY(), t2.getY()));
            
            // First process right neighbors, then down neighbors
            List<Tile> orderedNeighbors = new ArrayList<>();
            orderedNeighbors.addAll(rightNeighbors);
            orderedNeighbors.addAll(downNeighbors);
            
            for (Tile neighbor : orderedNeighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    
                    // Check if this is the destination
                    if (neighbor.isEqual(end)) {
                        found = true;
                        break;
                    }
                    
                    queue.add(neighbor);
                }
            }
        }
    }
    
    // reconstruct path in reversed order (from end to start)
    LinkedList<Tile> path = new LinkedList<>();
    
    if (found) {
        Tile current = end;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        // The path is already from end to start, which matches requirement
    }
    
    return path;
}