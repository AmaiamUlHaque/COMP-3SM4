/* This method partially tests the constructors and the method insertCodeword() and is declared in the class BinTree */

public static void main(String[] args){
        System.out.println("------------------Testing Constructor 1--------------");
        System.out.println("Expected Output: \"I num=0\"");
        BinTree myTree = new BinTree();
        myTree.printTree();
        System.out.println("num=" + myTree.num);


        System.out.println("\n------------------Testing method insertCodeword--------------");
        System.out.println("\n------------------Test 1. Valid Input--------------");
        myTree.insertCodeword("c0", "10");
        System.out.println("Expected Output: \"I c0 I num=1\""); 
        myTree.printTree();
        System.out.println("num=" + myTree.num);

        System.out.println("\n------------------Testing method insertCodeword--------------");
        System.out.println("\n------------------Test 2. Valid Input--------------");
        myTree.insertCodeword("c1", "001");
        System.out.println("Expected Output: \"I c1 I I c0 I num=2\""); 
        myTree.printTree();
        System.out.println("num=" + myTree.num);

         System.out.println("\n------------------Testing method insertCodeword--------------");
        System.out.println("\n------------------Test 3. Invalid Input. Duplicate codeword already in the tree--------------");
        System.out.println("Expected Output: \"Prefix condition violated!\""); 
        try{
            myTree.insertCodeword("c2", "001");
            myTree.printTree();
            System.out.println("num=" + myTree.num);
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

         System.out.println("\n------------------Testing method insertCodeword--------------");
        System.out.println("\n------------------Test 4. Invalid Input. A strict sufffix already in the tree--------------");
        System.out.println("Expected Output: \"Prefix condition violated!\""); 
        try{
            myTree.insertCodeword("c2", "00");
            myTree.printTree();
            System.out.println("num=" + myTree.num);
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

         System.out.println("\n------------------Testing method insertCodeword--------------");
        System.out.println("\n------------------Test 5. Invalid Input. A strict prefix already in the tree--------------");
        System.out.println("Expected Output: \"Prefix condition violated!\""); 
        try{
            myTree.insertCodeword("c2", "0011");
            myTree.printTree();
            System.out.println("num=" + myTree.num);
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        System.out.println("\n------------------Testing Constructor 2--------------");
        System.out.println("\n------------------Test 6. Valid Input--------------");
        String[] a = {"10", "0", "111", "110"};
        myTree = new BinTree(a);
        System.out.println("Expected Output: \"c1 I c0 I c3 I c2 num=4\""); 
        myTree.printTree();
        System.out.println("num=" + myTree.num);

        System.out.println("\n------------------Testing Constructor 2--------------");
        System.out.println("\n------------------Test 7. Invalid Input. Some inputs are not binary--------------");
        String[] b = {"10", "0", "1a1", "110"};
        System.out.println("Expected Output: \"Invalid Argument!\""); 
        try{
            myTree = new BinTree(b);
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        
    }//end main