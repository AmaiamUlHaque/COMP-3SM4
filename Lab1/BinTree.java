import java.util.ArrayList;
import java.lang.String;
import java.lang.Math;

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
                if (currNode.left == null){ //check if left exist
                    currNode.left = new TNode(null, null, null);
                }
                currNode = currNode.left;
            }

            else { //go down right path
                if (currNode.right == null){ //check if right exist
                    currNode.right = new TNode(null, null, null);
                }
                currNode = currNode.right;
            }
            
            if(currNode.data != null) //current node already contains data --> not prefix free
                throw new IllegalArgumentException("Prefix condition violated!");

        }

        if( currNode.left != null || currNode.right != null) {//check if internal node
            throw new IllegalArgumentException("Prefix condition violated!");
        }
        else {
            currNode.data = symbol;
            num++;
        }
    }

    public void printTree(){
        printTree(root);
    }

    private void printTree(TNode t){
        if (t!=null){
            printTree(t.left);
            if (t.data == null) {
                System.out.print("I "); }
            else {
                System.out.print(t.data + " "); }
            printTree(t.right);
        }
    }

    public int getNumberOfCodewords(){
        return num;
    }

    public int height(){
        return height(root);
    }

    private int height (TNode node){
        if (node.left == null || node.right == null){ //if subtree empty
            return(0);
        }

        int lHeight = height(node.left);
        int rHeight = height(node.right);

        return ((lHeight > rHeight) ? height(node.left) : height(node.right)) + 1;
    }

    public ArrayList<String> getCodewords(){
        
    }


}



    
    