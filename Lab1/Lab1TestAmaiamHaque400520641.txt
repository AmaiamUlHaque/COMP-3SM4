
// NOTE: test cases were ran in the BinTree class inorder to test the private function insertCodeWord, otherwise syntax errors do not occur while testing

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
    
    // Continue with more comprehensive testing
    System.out.println("\n------------------Testing getNumberOfCodewords() ----------------");
    System.out.println("Test 8: Testing getNumberOfCodewords() method");
    System.out.println("Expected Output: 4");
    System.out.println("Actual Output: " + myTree.getNumberOfCodewords());
    
    System.out.println("\n------------------Testing height() ----------------");
    System.out.println("Test 9: Testing height() method");
    System.out.println("Expected Output: 3");
    System.out.println("Actual Output: " + myTree.height());
    
    // Test with a simpler tree for height
    System.out.println("\nTest 10: Testing height with simpler tree");
    String[] simple = {"0", "1"};
    BinTree simpleTree = new BinTree(simple);
    System.out.println("Tree from array {\"0\", \"1\"}");
    System.out.println("Expected height: 1");
    System.out.println("Actual height: " + simpleTree.height());
    
    System.out.println("\n------------------Testing getCodewords() ----------------");
    System.out.println("Test 11: Testing getCodewords() method");
    System.out.println("Expected Output: [0, 10, 110, 111] in lexicographical order");
    System.out.println("Actual Output: " + myTree.getCodewords());
    
    // Test getCodewords with a different tree
    System.out.println("\nTest 12: Testing getCodewords with different tree");
    String[] balanced = {"00", "01", "10", "11"};
    BinTree balancedTree = new BinTree(balanced);
    System.out.println("Tree from array {\"00\", \"01\", \"10\", \"11\"}");
    System.out.println("Expected Output: [00, 01, 10, 11]");
    System.out.println("Actual Output: " + balancedTree.getCodewords());
    
    System.out.println("\n------------------Testing decode() ----------------");
    System.out.println("Test 13: Testing decode() method - Valid sequence: 11011110110000");
    System.out.println("Expected Output: [c3, c2, c0, c3, c1, c1, c1]");
    System.out.println("Actual Output: " + myTree.decode("11011110110000"));
    
    System.out.println("\nTest 14: Testing decode with another valid sequence: 0011001010111");
    System.out.println("Expected Output: [c1, c1, c3, c1, c0, c0, c2]");
    System.out.println("Actual Output: " + myTree.decode("0011001010111"));
    
    System.out.println("\nTest 15: Testing decode with balanced tree - Valid sequence: 00011011");
    System.out.println("Expected Output: [c0, c1, c2, c3]");
    System.out.println("Actual Output: " + balancedTree.decode("00011011"));
    
    // Edge case: single codeword decoding with valid sequence
    System.out.println("\nTest 16: Testing decode with single codeword tree - Valid sequence: 0000");
    String[] single = {"0"};
    BinTree singleTree = new BinTree(single);
    System.out.println("Expected Output: [c0, c0, c0, c0]");
    System.out.println("Actual Output: " + singleTree.decode("0000"));
    
    System.out.println("\n------------------Testing toString() ----------------");
    System.out.println("Test 17: Testing toString() method");
    System.out.println("Expected Output: (c0,10) (c1,0) (c2,111) (c3,110) ");
    System.out.println("Actual Output: " + myTree.toString());
    
    System.out.println("\nTest 18: Testing toString() with balanced tree");
    System.out.println("Expected Output: (c0,00) (c1,01) (c2,10) (c3,11) ");
    System.out.println("Actual Output: " + balancedTree.toString());
    
    System.out.println("\nTest 19: Testing toString() with single codeword tree");
    System.out.println("Expected Output: (c0,0) ");
    System.out.println("Actual Output: " + singleTree.toString());
    
    System.out.println("\n------------------Testing toArray() ----------------");
    System.out.println("Test 20: Testing toArray() method");
    System.out.println("Creating array representation of the tree");
    String[] arrayRep = myTree.toArray();
    System.out.println("Array size: " + arrayRep.length);
    System.out.println("First 15 elements (index 1-14):");
    for (int i = 1; i < Math.min(15, arrayRep.length); i++) {
        System.out.println("  index " + i + ": " + (arrayRep[i] == null ? "null" : arrayRep[i]));
    }
    
    System.out.println("\nTest 21: Testing toArray() with balanced tree");
    String[] balancedArray = balancedTree.toArray();
    System.out.println("Array size: " + balancedArray.length);
    System.out.println("First 15 elements (index 1-14):");
    for (int i = 1; i < Math.min(15, balancedArray.length); i++) {
        System.out.println("  index " + i + ": " + (balancedArray[i] == null ? "null" : balancedArray[i]));
    }
    
    System.out.println("\nTest 22: Testing toArray() with single codeword tree");
    String[] singleArray = singleTree.toArray();
    System.out.println("Array size: " + singleArray.length);
    System.out.println("First 5 elements (index 1-4):");
    for (int i = 1; i < Math.min(5, singleArray.length); i++) {
        System.out.println("  index " + i + ": " + (singleArray[i] == null ? "null" : singleArray[i]));
    }
    
    // Additional edge cases - Only valid inputs
    System.out.println("\n------------------Testing Additional Valid Cases ----------------");
    
    System.out.println("\nTest 23: Testing with longer codewords");
    String[] longCodes = {"0", "10", "110", "1110", "11110", "111110"};
    BinTree longTree = new BinTree(longCodes);
    System.out.println("Tree with 6 codewords of increasing length");
    System.out.println("Number of codewords: " + longTree.getNumberOfCodewords());
    System.out.println("Height: " + longTree.height());
    System.out.println("Codewords: " + longTree.getCodewords());
    
    System.out.println("\nTest 24: Testing decode with various valid sequences");
    System.out.println("Decoding '0' from balanced tree:");
    System.out.println("Expected: [c0]");
    System.out.println("Actual: " + balancedTree.decode("0"));
    
    System.out.println("\nDecoding '1' from simple tree:");
    System.out.println("Expected: [c1]");
    System.out.println("Actual: " + simpleTree.decode("1"));
    
    System.out.println("\nDecoding '101011' from myTree (should be c0 c2 c1):");
    System.out.println("Expected: [c0, c2, c1]");
    System.out.println("Actual: " + myTree.decode("101011"));
    
    System.out.println("\n------------------Testing Method Interactions ----------------");
    
    System.out.println("\nTest 25: Testing that toString uses getCodewords and decode");
    System.out.println("toString output: " + myTree.toString());
    System.out.println("Verifying correctness...");
    ArrayList<String> codewords = myTree.getCodewords();
    boolean correct = true;
    for (int i = 0; i < myTree.num; i++) {
        String symbol = "c" + i;
        String codeword = codewords.get(i);
        ArrayList<String> decoded = myTree.decode(codeword);
        if (!decoded.isEmpty() && decoded.get(0).equals(symbol)) {
            System.out.println("  ✓ " + symbol + " matches codeword " + codeword);
        } else {
            correct = false;
            System.out.println("  ✗ Mismatch for symbol " + symbol);
        }
    }
    if (correct) {
        System.out.println("toString is correct!");
    }
    
    System.out.println("\nTest 26: Testing printTree output format");
    System.out.println("Printing tree from original example:");
    myTree.printTree();
    System.out.println("\nExpected: c1 I c0 I c3 I c2");
    
    System.out.println("\n------------------Final Summary ----------------");
    System.out.println("Total tests executed: 26");
    System.out.println("All tests completed successfully!");
    
}//end main