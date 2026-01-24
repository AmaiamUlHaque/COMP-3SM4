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

        // // equivalent to if else statements above:
        // if( currNode.left == null && currNode.right == null) {//check if leaf node
        //     currNode.data = symbol;
        //     num++;
        // }
        // else { //is internal node
        //     throw new IllegalArgumentException("Prefix condition violated!");            
        // }
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
        ArrayList<String> codewords = new ArrayList<>();
        return getCodewords(root, null, codewords);                                                                                                                                                                                                                                                                                                                                                                                                                                                          );        
    }

    
    private ArrayList<String> getCodewords(TNode t, String path, ArrayList<String> codewords){
        // store codewords in lexicographical/binary order
        // each item in the list is a str of 0/1s
        // each codeword insertation should be done with an arraylist class method
        //KEEP TRACK OF THE PATH?

        if (t!=null){

            getCodewords(t.left, path+"0", codewords);

            if (t.data == null) { //internal node
                System.out.print("I "); 
                //go left
                //once done, go center --> nvm, it must be internal --> no codeword
                //once done, go right
            }
            else { // leaf node / codeword
                //add codeword to arrayList
                codewords.add(path);
                return codewords;
            }
            getCodewords(t.right, path+"1", codewords);        
        }

        return codewords;

    }

    public ArrayList<String> decode(String s){
        // outputs the sequence of alphabet symbols obtained by decoding the binary sequence s
        // Each alphabet symbol has to be stored as a separate item in the list.
        // assume thats is a nonempty, valid binary sequence that can be divided into codewords.
        int n = s.length();
        for (int i=0; i<n; i++){
            if (s.charAt(i) == '0'){
                
            }
            else { //(s.charAt(i) == '1')

            }
        }

    }


}



    
    