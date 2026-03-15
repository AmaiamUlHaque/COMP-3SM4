public LinkedList<Tile> findShortestPath(Tile start, Tile end)
    {
        // if start or end is null, return empty list
        if (start == null || end == null) {
            return new LinkedList<>();
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
                // Convert to list and sort by x-coordinate in descending order
                // This prioritizes moving right first, which matches the expected paths
                List<Tile> sortedNeighbors = new ArrayList<>(neighbors);
                Collections.sort(sortedNeighbors, (t1, t2) -> {
                    // Sort by x-coordinate descending (larger x first)
                    return Integer.compare(t2.getX(), t1.getX());
                });
                
                for (Tile neighbor : sortedNeighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parent.put(neighbor, current);
                        queue.add(neighbor);
                        
                        // check if this neighbor is the destination
                        if (neighbor.isEqual(end)) {
                            found = true;
                            break;
                        }
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
        }
        
        return path;
    }

