public class BinTree {
    private TNode root;
    private int num = 0; //num of code words

    //constructor 1; creates a tree with only the root node
    public  BinTree() {
        root = new TNode(null, null, null);
    }

    //constructor 2
    public BinTree(String[] a) throws IllegalArgumentException {

        int k = a.length; // array length
        
        //check if all items in the array are binary strings
	    //if they are not, throw an exception
            for (int i = 0; i < k; i++){ 
                
                //loop through the current string;
                //if current character is not '0' or '1', throw exception

                int n = a[i].length(); //length of current string

                for (int j = 0; j < n; j++){
                    if (a[i].charAt(j) != '0' && a[i].charAt(j) != '1'){
                        throw new IllegalArgumentException("Invalid argument!");
                    }
                }

            }
    
        //'stafa was here :<
        //Now, we know that all inputs are binary; 
	    //we start constructing the tree by creating the root node
        this.root = new TNode(null, null,null);
        
	    //loop through the array and insert each codeword by using insertCodeword()  
        for( int i = 0; i < k; i++){
            // insert the codeword a[i] in the tree with corresponding symbol "c" + i
            String symbol = "c" + i;
            insertCodeword(symbol, a[i]);
        }
    }    

    private void insertCodeword(String symbol, String binary) throws IllegalArgumentException {
        
        TNode node = this.root; 
        
        //read the characters in the binary string and take the appropriate action for each of them

        int n = binary.length(); // length of binary string
        char c; // to store the current character in the binary string
	
	    //loop through the binary string
        for(int i = 0; i < n; i++){
	        c = binary.charAt(i); // read current character in the binary string

            //We can write the method in such a way to make sure that at this point in the code 
	        //the current node exists (it is not null);  
            //go to left child if c=='0' or to right child if c=='1', 
            // but if the corresponding child does not exist, create it

            if(c=='0'){
                //check if left child exists; if it doesn't exist, 
		        //create it as an internal node with data == null; then go to the left child
               


            }

            else{
                //check if right child exists; if it doesn't exist, create it as an internal node;
		        //then go to the right child


            }

            //if current node is a codeword, this means that the prefix condition is violated
	        //and an exception is thrown
            if(...)
                throw new IllegalArgumentException("Prefix condition violated!");

        }

        // now the current node is at the end of the path corresponding to the input binary string
        // if the current node is not a leaf then there must a codeword in the tree 
	    //that is suffix of the input binary string, so the prefix condition is violated
        if(...)
            throw new IllegalArgumentException("Prefix condition violated!");
        else {
            ... //copy symbol in the node
            ... //increase by 1 the number of codewords
        }
    }

    public void printTree(){
        printTree(root);
    }

    private void printTree(TNode t){
        
    }




}



    
    