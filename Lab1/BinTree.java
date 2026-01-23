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
        
        TNode currNode = this.root; 
        int n = binary.length();
        char c; 
	    
        for(int i = 0; i < n; i++){ 

	        c = binary.charAt(i);

            if (c=='0') { //go down left path
                currNode = currNode.left; 
            }

            else { //go down right path
                currNode = currNode.right;
            }

            if(...) //if not prefix-free
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



    
    