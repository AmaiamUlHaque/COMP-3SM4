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
        if (node == null) return -1; // if tree empty
        if (node.left == null && node.right == null){ //no children
            return(0);
        }

        int lHeight = height(node.left);
        int rHeight = height(node.right);

        return ((lHeight > rHeight) ? lHeight : rHeight) + 1;
    }

    public ArrayList<String> getCodewords(){
        ArrayList<String> codewords = new ArrayList<>();
        getCodewords(root, "", codewords);     
        return codewords;        
    }

    private void getCodewords(TNode t, String path, ArrayList<String> codewords){
        // store codewords in lexicographical/binary order
        // each item in the list is a str of 0/1s
        // each codeword insertation should be done with an arraylist class method
            //go left
            //once done, go center --> nvm, it must be internal --> no codeword
            //once done, go right

        if (t!=null){

            if (t.data != null) {
                //add codeword to arrayList
                codewords.add(path);
            }
            
            getCodewords(t.left, path+"0", codewords);
            getCodewords(t.right, path+"1", codewords);        
        }

    }

    public ArrayList<String> decode(String s){
        // outputs the sequence of alphabet symbols obtained by decoding the binary sequence s
        // Each alphabet symbol has to be stored as a separate item in the list.
        // assume thats is a nonempty, valid binary sequence that can be divided into codewords.

        ArrayList<String> decoded = new ArrayList<>();
        TNode currNode = root; //starting point
        int n = s.length();

        for (int i=0; i<n; i++){

            if (s.charAt(i) == '0'){
                currNode = currNode.left;
            } 
            else { //(s.charAt(i) == '1')
                currNode = currNode.right;
            }

            if (currNode != null && currNode.data != null) {
                decoded.add(currNode.data);
                currNode = root; // Reset for next codeword
            }

        }

        return decoded;

    }

    public String toString(){
        // returns the string representation of the prefix-free code 
        // as a sequence of pairs (symbol, codeword) 
        // listed in increasing order of the symbol index, 
        // separated by empty spaces and ending with an empty space. 
        // For our example, this string is “(c0,10) (c1,0) (c2,111) (c3, 110) ”

        
        ArrayList<String> codewords = getCodewords();

        String[] codewordsInOrder = new String[num]; //stores each pair at its corresponding index

        for (int i=0; i<num; i++){

            String codeword = codewords.get(i);
            String decoded = decode(codeword).get(0); //only one item in arraylist --> always at index 0
            String symbolNum = decoded.substring(1); //exclude the char 'c'
            int codeNum = Integer.parseInt(symbolNum);


            String str = "(c" + codeNum + "," + codeword + ") ";
            codewordsInOrder[codeNum] = str;

        }

        String str = "";

        for (int i=0; i<num; i++){
            str += codewordsInOrder[i];
        }

        return str;
    }

    public String[] toArray(){
        int h = height();
        int size = (int) Math.pow(2, h+1);
        String[] array = new String[size];

        fillArray(root, 1, array);
        return array;
    }

    private void fillArray(TNode node, int index, String[] array) {
        
        if (node == null || index >= array.length) { // Base case: null node or index out of bounds
            return;
        }
        
        // Store current node value
        array[index] = node.data == null ? "I" : node.data;
        
        // Recursively fill children
        fillArray(node.left, 2*index, array);
        fillArray(node.right, 2*index+1, array); 
    }

}