import java.util.Random;

public class HandsMaxHeap {
    private Hands[] myHeap; // array
    private int size;       // heap size (number of items stored in the heap)
    private int capacity;   // heap capacity (the maximum number of items the heap could store)

    // Constructor 1: creates an empty heap with a given capacity
    public HandsMaxHeap(int bufSize)
    {
        // set capacity = bufSize, and size = 0
        // instantiate myHeap as a Hands array with capacity + 1 slots (think about why capacity + 1)
        // finally, set the first element in the Hands array to a dummy Hand (using the default Hands() constructor)
        
        capacity = bufSize;
        size=0;
        myHeap = new Hands[capacity+1]; //index 0 = dummy header
        myHeap[0] = new Hands();

    }

    // Constructor 2: constructs a heap out of the array someHands 
    public HandsMaxHeap(Hands[] someHands)
    {
        // the first element in the array is treated as a dummy, the remaining elements are organized as a heap using 
        // the private method bulidMaxHeap, which you have to implement

        myHeap = someHands;
        capacity = someHands.length - 1; //myHeap [capacity+1] --> capacity = myHeap.length-1
        size = capacity;
        buildMaxHeap();

    }

    // [Problem 0] Implement buildMaxHeap 
    private void buildMaxHeap() //organizes the array as a heap
    {
        // When this method is invoked by the constructor, the array myHeap is not organized as a heap yet;
        // the method should organize the array as a heap (disregarding the element at index 0) using the O(n)-time algorithm 
        
        for (int i=size/2; i>=1; i--){
            //insert each hand into the heap
            downHeapify(i);
        }

    }
    

    // [Problem 1] Implement Max Heap for 5-Card Hands

    // [Problem 1-1] Implement Private Utility Methods
    private int parent(int index) //calculates the parent index
    {
        return index/2;
    }
    
    private int leftChild(int index) // calculates the left-child index
    {
        return 2*index;
    }

    private int rightChild(int index) //calculates the right-child index
    {
        return 2*index+1;
    }

    // [Problem 1-2] Implement the Downward Heap Reorganization Private Method from the provided index 

    // private void downHeapifyyy(int index) //percolateDown
    // {
    //     //percolateDown the Heap Node at index; stop when it fits
    //     Hands key = myHeap[index];
    //     int child = 2*index; //left child
    //    
    //     while (child <= size){
    //
    //         if (child > size && myHeap[child+1].isMyHandLarger(myHeap[child])){
    //             child++; //right child
    //         }
    //         if (myHeap[child].isMyHandSmaller(key)){
    //             myHeap[index] = myHeap[child];
    //             index = child;
    //             child = 2*index;
    //         }
    //     }
    //     myHeap[index]=key;
    // } 

    private void downHeapify(int index)
    {
        while (leftChild(index) <= size) { //check if the node at index is a leaf (has no children)
            
            int largerChild = leftChild(index);
            int rightChild = rightChild(index);
            
            //check if right child exists and is larger than left child
            if (rightChild <= size && myHeap[rightChild].isMyHandLarger(myHeap[largerChild])){
                largerChild = rightChild;
            }
            
            //check if the larger child is larger than the current node
            if (myHeap[largerChild].isMyHandLarger(myHeap[index])){

                // Swap the current node with the larger child
                Hands temp = myHeap[index];
                myHeap[index] = myHeap[largerChild];
                myHeap[largerChild] = temp;
                
                // Move down to the child's position
                index = largerChild;
            }

            else { //heap property is satisfied
                break;
            }
        }
    }

    // [Problem 1-3] Implement Upward Heap Reorganization Private Method from the provided index 

    // private void heapifyUppp(int index) //percolateUp
    // {   
    //     // percolateUp the Heap Node at index; stop when it fits
    //     // for this, first copy the Heap Node at index into temp
    //     // compare the temp node against the parent node and so on               
    //  
    //     Hands key = myHeap[index];
    //     int parent = index/2;
    //
    //     while (parent <= size){
    //
    //         if (myHeap[parent].isMyHandLarger(key)){
    //             myHeap[index] = myHeap[parent];
    //             index = parent;
    //             parent = index/2;
    //         }
    //
    //     }
    //
    //     myHeap[index]=key;
    //
    // }

    private void heapifyUp(int index) //percolateUp
    {
        // percolateUp the Heap Node at index; stop when it fits
        // for this, first copy the Heap Node at index into temp
        // compare the temp node against the parent node and so on    

        while (index > 1){  // stop when we reach the root (index 1)

            int parentIndex = parent(index);
            
            //swap with parent if current node is larger
            if (myHeap[index].isMyHandLarger(myHeap[parentIndex])){
                
                Hands temp = myHeap[index];
                myHeap[index] = myHeap[parentIndex];
                myHeap[parentIndex] = temp;
                index = parentIndex;
            }

            else { //heap property is satisfied
                break;
            }
        }
    }


    // [Problem 1-4] Complete the Max Heap ADT Public Methods

    public void insert(Hands thisHand)
    {
        // insert thisHand into the heap; 
        // if there is no room for insertion allocate a bigger array 
        // (the capacity of the new heap should be twice larger) and copy the data over     

        if (size == capacity){ //no more room --> add more

            Hands[] temp = myHeap;
            myHeap = new Hands[2*capacity];

            for (int i=1; i<=size; i++){
                myHeap[i] = temp[i];
            }

            capacity *= 2;

        }

        size++;
        myHeap[size] = thisHand;
        heapifyUp(size); //restores heap ordering property

    }

    public Hands removeMax() throws RuntimeException //remove the largest Hand from the heap; if the heap is empty throw a RuntimeException
    {
        if (size == 0){ //throw runtime exception
            throw new RuntimeException("Heap is empty");
        }

        Hands maxHand = myHeap[1];
        myHeap[1] = myHeap[size]; //swap last indexed hand to root
        myHeap[size] = new Hands();
        size--;

        if (size>0){
            downHeapify(1); //percolateDown's the newly swapped hand
        }

        return maxHand;
    }

    public int getSize() // return the size of the heap
    {
        return size;
    }

    public boolean isEmpty() //return true if heap is empty; return false otherwise
    {
        return size==0;
    }

    public void printHeap() //prints all Hands in heap by traversing the heap in level order
    {
        // For Debugging Purpose - Print all the heap elements (i.e. Hands) by traversing the heap in level order        
        //  For valid hands, print the hand using the respective method in Hands class
        //  For invalid hands, just print "--INV--"
        //  Use the required method in Hands class to determine whether a Hand is valid.

        for (int i=1; i<=size; i++){
            myHeap[i].printMyHand();
        }

    }

    public static void heapSort(Hands myHands[])
    {
        // Sort In place the incoming array myHands in DESCENDING order
        // using the heap sort algorithm
        
        if (myHands == null || myHands.length <= 1) {
            return;
        }

        int n = myHands.length;

        // Build max heap in O(n) time
        for (int i = n/2 - 1; i >= 0; i--) {
            heapify(myHands, n, i);
        }

        // Extract elements one by one to get descending order
        for (int i = n - 1; i > 0; i--) {
            // Swap root (largest) with current last element
            Hands temp = myHands[0];
            myHands[0] = myHands[i];
            myHands[i] = temp;

            // Heapify the reduced heap (size = i)
            heapify(myHands, i, 0);
        }
        // After this loop, array is in ASCENDING order
        
        // Reverse to get DESCENDING order
        for (int i = 0; i < n/2; i++) {
            Hands temp = myHands[i];
            myHands[i] = myHands[n - 1 - i];
            myHands[n - 1 - i] = temp;
        }
    }

    private static void heapify(Hands[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // If left child exists and is larger than current largest
        if (left < n && arr[left].isMyHandLarger(arr[largest])) {
            largest = left;
        }

        // If right child exists and is larger than current largest
        if (right < n && arr[right].isMyHandLarger(arr[largest])) {
            largest = right;
        }

        // If largest is not root
        if (largest != i) {
            // Swap
            Hands temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            // Recursively heapify the affected subtree
            heapify(arr, n, largest);
        }
    }
    

    // This is the Test Bench!!

    private static boolean totalPassed = true;
    private static int totalTestCount = 0;
    private static int totalPassCount = 0;

    public static void main(String args[])
    {
        testParent();
        testLeftChild();
        testRightChild();
        testHandsMaxHeap1();        
        testHandsMaxHeap2();
        testInsert1();
        testInsert2();
        
        testGetSize1();
        testGetSize2();
        testRemoveMax1();
        testRemoveMax2();

        CustomTestInsert1();
        CustomTestInsert2();
        CustomTestRemoveMax1();
        CustomTestRemoveMax2();

        testHeapSort1();
        testHeapSort2();

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

    private static void testParent(){
        
        // Setup
        System.out.println("============testParent=============");
        boolean passed = true;
        totalTestCount++;

        HandsMaxHeap myMaxHeap = new HandsMaxHeap(10);
        int result, expected;
       
        // Action
        for(int i = 1; i < 50; i++)
        {
            result = myMaxHeap.parent(i);
            expected = i / 2;
            passed &= assertEquals(expected, result);
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
        
    }
    
    private static void testLeftChild(){
        // Setup
        System.out.println("============testLeftChild=============");
        boolean passed = true;
        totalTestCount++;

        HandsMaxHeap myMaxHeap = new HandsMaxHeap(10);
        int result, expected;
        
        // Action
        for(int i = 1; i < 50; i++)
        {
            result = myMaxHeap.leftChild(i);
            expected = 2 * i;
            passed &= assertEquals(expected, result);
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testRightChild(){
        // Setup
        System.out.println("============testRightChild=============");
        boolean passed = true;
        totalTestCount++;

        HandsMaxHeap myMaxHeap = new HandsMaxHeap(10);
        int result, expected;
        
        // Action
        for(int i = 1; i < 50; i++)
        {
            result = myMaxHeap.rightChild(i);
            expected = (2 * i) + 1;
            passed &= assertEquals(expected, result);
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testHandsMaxHeap1(){
        // Setup
        System.out.println("============testHandsMaxHeap1=============");
        boolean passed = true;
        totalTestCount++;

        HandsMaxHeap myMaxHeap = new HandsMaxHeap(20);
        int result, expected;

        // Action
        result = myMaxHeap.capacity;
        expected = 20;
        passed &= assertEquals(expected, result);
        
        result = myMaxHeap.size;
        expected = 0;
        passed &= assertEquals(expected, result);

        Hands expectedHand = new Hands();
        Hands actualHand = myMaxHeap.myHeap[0];
        passed &= assertEquals(expectedHand, actualHand);
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testHandsMaxHeap2(){
        // Setup
        System.out.println("============testHandsMaxHeap2=============");
        boolean passed = true;
        totalTestCount++;

        HandsMaxHeap myMaxHeap = new HandsMaxHeap(50);
        int result, expected;

        // Action
        result = myMaxHeap.capacity;
        expected = 50;
        passed &= assertEquals(expected, result);
        
        result = myMaxHeap.size;
        expected = 0;
        passed &= assertEquals(expected, result);

        Hands expectedHand = new Hands();
        Hands actualHand = myMaxHeap.myHeap[0];
        passed &= assertEquals(expectedHand, actualHand);
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testInsert1(){
        // Setup
        System.out.println("============testInsert1=============");
        boolean passed = true;
        totalTestCount++;
        
        HandsMaxHeap myMaxHeap = new HandsMaxHeap(20);
        Hands myHandsArray[] = new Hands[15];  
        Hands expectedHandsArray[] = new Hands[20];
        
        // [Scott] Need initialization of all hands
        myHandsArray[0] = new Hands(new Card(2, 'C'), new Card(2, 'D'), new Card(6, 'C'), new Card(6, 'S'), new Card(6, 'H'));
        myHandsArray[1] = new Hands(new Card(8, 'D'), new Card(9, 'D'), new Card(10, 'H'), new Card(11, 'D'), new Card(12, 'H'));
        myHandsArray[2] = new Hands(new Card(4, 'C'), new Card(5, 'C'), new Card(6, 'C'), new Card(7, 'C'), new Card(8, 'C'));
        myHandsArray[3] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D'));
        myHandsArray[4] = new Hands(new Card(10, 'C'), new Card(11, 'D'), new Card(10, 'D'), new Card(10, 'S'), new Card(10, 'H'));
        myHandsArray[5] = new Hands(new Card(6, 'S'), new Card(7, 'D'), new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'));
        myHandsArray[6] = new Hands(new Card(14, 'C'), new Card(14, 'D'), new Card(6, 'C'), new Card(14, 'S'), new Card(14, 'H'));
        myHandsArray[7] = new Hands(new Card(11, 'H'), new Card(11, 'D'), new Card(11, 'C'), new Card(5, 'H'), new Card(5, 'S'));
        myHandsArray[8] = new Hands(new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H'));
        myHandsArray[9] = new Hands(new Card(8, 'S'), new Card(8, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(8, 'H'));
        myHandsArray[10] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(13, 'S'), new Card(14, 'S'));
        myHandsArray[11] = new Hands(new Card(12, 'D'), new Card(12, 'C'), new Card(9, 'C'), new Card(12, 'S'), new Card(9, 'H'));
        myHandsArray[12] = new Hands(new Card(5, 'S'), new Card(10, 'D'), new Card(10, 'C'), new Card(5, 'C'), new Card(10, 'H'));
        myHandsArray[13] = new Hands(new Card(7, 'S'), new Card(6, 'C'), new Card(5, 'C'), new Card(4, 'H'), new Card(3, 'H'));
        myHandsArray[14] = new Hands(new Card(3, 'C'), new Card(5, 'D'), new Card(3, 'S'), new Card(5, 'S'), new Card(3, 'D'));
        
        for(int i = 0; i < 15; i++)
            myMaxHeap.insert(myHandsArray[i]);

        expectedHandsArray[0] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(13, 'S'), new Card(14, 'S'));
        expectedHandsArray[1] = new Hands(new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H'));
        expectedHandsArray[2] = new Hands(new Card(14, 'C'), new Card(14, 'D'), new Card(6, 'C'), new Card(14, 'S'), new Card(14, 'H'));
        expectedHandsArray[3] = new Hands(new Card(10, 'C'), new Card(11, 'D'), new Card(10, 'D'), new Card(10, 'S'), new Card(10, 'H'));
        expectedHandsArray[4] = new Hands(new Card(4, 'C'), new Card(5, 'C'), new Card(6, 'C'), new Card(7, 'C'), new Card(8, 'C'));
        expectedHandsArray[5] = new Hands(new Card(12, 'D'), new Card(12, 'C'), new Card(9, 'C'), new Card(12, 'S'), new Card(9, 'H'));
        expectedHandsArray[6] = new Hands(new Card(2, 'C'), new Card(2, 'D'), new Card(6, 'C'), new Card(6, 'S'), new Card(6, 'H'));
        expectedHandsArray[7] = new Hands(new Card(8, 'D'), new Card(9, 'D'), new Card(10, 'H'), new Card(11, 'D'), new Card(12, 'H'));
        expectedHandsArray[8] = new Hands(new Card(11, 'H'), new Card(11, 'D'), new Card(11, 'C'), new Card(5, 'H'), new Card(5, 'S'));
        expectedHandsArray[9] = new Hands(new Card(8, 'S'), new Card(8, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(8, 'H'));
        expectedHandsArray[10] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D'));
        expectedHandsArray[11] = new Hands(new Card(6, 'S'), new Card(7, 'D'), new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'));
        expectedHandsArray[12] = new Hands(new Card(5, 'S'), new Card(10, 'D'), new Card(10, 'C'), new Card(5, 'C'), new Card(10, 'H'));
        expectedHandsArray[13] = new Hands(new Card(7, 'S'), new Card(6, 'C'), new Card(5, 'C'), new Card(4, 'H'), new Card(3, 'H'));
        expectedHandsArray[14] = new Hands(new Card(3, 'C'), new Card(5, 'D'), new Card(3, 'S'), new Card(5, 'S'), new Card(3, 'D'));
                

        // Action
        for(int i = 0; i < myMaxHeap.size; i++)
            passed &= assertEquals(expectedHandsArray[i], myMaxHeap.myHeap[i + 1]);
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testInsert2(){
        // Setup
        System.out.println("============testInsert2=============");
        boolean passed = true;
        totalTestCount++;
        
        HandsMaxHeap myMaxHeap = new HandsMaxHeap(20);
        Hands myHandsArray[] = new Hands[22];  
        Hands expectedHandsArray[] = new Hands[20];
        
        // [Scott] Need initialization of all hands
        myHandsArray[0] = new Hands(new Card(6, 'S'), new Card(3, 'D'), new Card(6, 'C'), new Card(6, 'H'), new Card(3, 'H'));
        myHandsArray[1] = new Hands(new Card(6, 'D'), new Card(6, 'S'), new Card(3, 'H'), new Card(6, 'C'), new Card(6, 'H'));
        myHandsArray[2] = new Hands(new Card(9, 'C'), new Card(11, 'C'), new Card(12, 'D'), new Card(10, 'H'), new Card(8, 'H'));
        myHandsArray[3] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D'));
        myHandsArray[4] = new Hands(new Card(14, 'D'), new Card(12, 'D'), new Card(10, 'D'), new Card(11, 'D'), new Card(13, 'D'));
        myHandsArray[5] = new Hands(new Card(10, 'S'), new Card(10, 'D'), new Card(8, 'H'), new Card(10, 'H'), new Card(8, 'S'));
        myHandsArray[6] = new Hands(new Card(11, 'C'), new Card(12, 'D'), new Card(12, 'C'), new Card(12, 'S'), new Card(12, 'H'));
        myHandsArray[7] = new Hands(new Card(3, 'H'), new Card(5, 'D'), new Card(4, 'C'), new Card(6, 'H'), new Card(2, 'S'));
        myHandsArray[8] = new Hands(new Card(8, 'H'), new Card(10, 'H'), new Card(9, 'H'), new Card(11, 'H'), new Card(7, 'H'));
        myHandsArray[9] = new Hands(new Card(2, 'S'), new Card(2, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(2, 'H'));
        myHandsArray[10] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(8, 'S'), new Card(9, 'S'));
        myHandsArray[11] = new Hands(new Card(12, 'D'), new Card(12, 'C'), new Card(9, 'C'), new Card(12, 'S'), new Card(9, 'H'));
        myHandsArray[12] = new Hands(new Card(6, 'S'), new Card(5, 'D'), new Card(6, 'C'), new Card(5, 'C'), new Card(5, 'H'));
        myHandsArray[13] = new Hands(new Card(7, 'S'), new Card(6, 'C'), new Card(5, 'C'), new Card(8, 'H'), new Card(9, 'H'));
        myHandsArray[14] = new Hands(new Card(13, 'C'), new Card(5, 'D'), new Card(13, 'S'), new Card(5, 'S'), new Card(13, 'D'));
        myHandsArray[15] = new Hands(new Card(7, 'C'), new Card(8, 'C'), new Card(10, 'C'), new Card(11, 'C'), new Card(9, 'C'));
        myHandsArray[16] = new Hands(new Card(4, 'C'), new Card(4, 'D'), new Card(4, 'S'), new Card(6, 'S'), new Card(4, 'H'));
        myHandsArray[17] = new Hands(new Card(3, 'H'), new Card(5, 'H'), new Card(3, 'S'), new Card(5, 'S'), new Card(5, 'D'));
        myHandsArray[18] = new Hands(new Card(10, 'S'), new Card(8, 'C'), new Card(8, 'S'), new Card(10, 'H'), new Card(10, 'D'));
        myHandsArray[19] = new Hands(new Card(5, 'C'), new Card(8, 'D'), new Card(7, 'S'), new Card(6, 'S'), new Card(9, 'D'));
        myHandsArray[20] = new Hands(new Card(7, 'S'), new Card(7, 'D'), new Card(4, 'S'), new Card(4, 'D'), new Card(7, 'C'));
        myHandsArray[21] = new Hands(new Card(9, 'D'), new Card(10, 'D'), new Card(13, 'D'), new Card(11, 'D'), new Card(12, 'D'));

        
        for(int i = 0; i < 20; i++)
            myMaxHeap.insert(myHandsArray[i]);
        

        expectedHandsArray[0] = new Hands(new Card(14, 'D'), new Card(12, 'D'), new Card(10, 'D'), new Card(11, 'D'), new Card(13, 'D'));
        expectedHandsArray[1] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(8, 'S'), new Card(9, 'S'));
        expectedHandsArray[2] = new Hands(new Card(11, 'C'), new Card(12, 'D'), new Card(12, 'C'), new Card(12, 'S'), new Card(12, 'H'));
        expectedHandsArray[3] = new Hands(new Card(7, 'C'), new Card(8, 'C'), new Card(10, 'C'), new Card(11, 'C'), new Card(9, 'C'));
        expectedHandsArray[4] = new Hands(new Card(8, 'H'), new Card(10, 'H'), new Card(9, 'H'), new Card(11, 'H'), new Card(7, 'H'));
        expectedHandsArray[5] = new Hands(new Card(12, 'D'), new Card(12, 'C'), new Card(9, 'C'), new Card(12, 'S'), new Card(9, 'H'));
        expectedHandsArray[6] = new Hands(new Card(13, 'C'), new Card(5, 'D'), new Card(13, 'S'), new Card(5, 'S'), new Card(13, 'D'));
        expectedHandsArray[7] = new Hands(new Card(6, 'D'), new Card(6, 'S'), new Card(3, 'H'), new Card(6, 'C'), new Card(6, 'H'));
        expectedHandsArray[8] = new Hands(new Card(10, 'S'), new Card(8, 'C'), new Card(8, 'S'), new Card(10, 'H'), new Card(10, 'D'));
        expectedHandsArray[9] = new Hands(new Card(2, 'S'), new Card(2, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(2, 'H'));
        expectedHandsArray[10] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D'));
        expectedHandsArray[11] = new Hands(new Card(9, 'C'), new Card(11, 'C'), new Card(12, 'D'), new Card(10, 'H'), new Card(8, 'H'));
        expectedHandsArray[12] = new Hands(new Card(6, 'S'), new Card(5, 'D'), new Card(6, 'C'), new Card(5, 'C'), new Card(5, 'H'));
        expectedHandsArray[13] = new Hands(new Card(7, 'S'), new Card(6, 'C'), new Card(5, 'C'), new Card(8, 'H'), new Card(9, 'H'));
        expectedHandsArray[14] = new Hands(new Card(10, 'S'), new Card(10, 'D'), new Card(8, 'H'), new Card(10, 'H'), new Card(8, 'S'));
        expectedHandsArray[15] = new Hands(new Card(3, 'H'), new Card(5, 'D'), new Card(4, 'C'), new Card(6, 'H'), new Card(2, 'S'));
        expectedHandsArray[16] = new Hands(new Card(4, 'C'), new Card(4, 'D'), new Card(4, 'S'), new Card(6, 'S'), new Card(4, 'H'));
        expectedHandsArray[17] = new Hands(new Card(3, 'H'), new Card(5, 'H'), new Card(3, 'S'), new Card(5, 'S'), new Card(5, 'D'));
        expectedHandsArray[18] = new Hands(new Card(6, 'S'), new Card(3, 'D'), new Card(6, 'C'), new Card(6, 'H'), new Card(3, 'H'));
        expectedHandsArray[19] = new Hands(new Card(5, 'C'), new Card(8, 'D'), new Card(7, 'S'), new Card(6, 'S'), new Card(9, 'D'));
            
        // Action
        for(int i = 0; i < myMaxHeap.size; i++)
            passed &= assertEquals(expectedHandsArray[i], myMaxHeap.myHeap[i + 1]);
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void testGetSize1(){
        // Setup
        System.out.println("============testGetSize1=============");
        boolean passed = true;
        totalTestCount++;

        Random rn = new Random();
        HandsMaxHeap myMaxHeap = new HandsMaxHeap(20);

        int expected = rn.nextInt(10);
        for(int i = 0; i < expected; i++)
            myMaxHeap.insert(
                new Hands(new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H'))
            );

        // Action
        passed &= assertEquals(expected, myMaxHeap.getSize());

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testGetSize2(){
        // Setup
        System.out.println("============testGetSize2=============");
        boolean passed = true;
        totalTestCount++;

        Random rn = new Random();
        HandsMaxHeap myMaxHeap = new HandsMaxHeap(20);

        int expected = rn.nextInt(19);
        for(int i = 0; i < expected; i++)
            myMaxHeap.insert(
                new Hands(new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H'))
            );

        // Action
        passed &= assertEquals(expected, myMaxHeap.getSize());

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testRemoveMax1(){
        // Setup
        System.out.println("============testRemoveMax1=============");
        boolean passed = true;
        totalTestCount++;
        
        HandsMaxHeap myMaxHeap = new HandsMaxHeap(20);
        Hands myHandsArray[] = new Hands[15];  
        Hands expectedMaxHand[] = new Hands[3];
        
        // [Scott] Need initialization of all hands
        myHandsArray[0] = new Hands(new Card(2, 'C'), new Card(2, 'D'), new Card(6, 'C'), new Card(6, 'S'), new Card(6, 'H'));
        myHandsArray[1] = new Hands(new Card(8, 'D'), new Card(9, 'D'), new Card(10, 'H'), new Card(11, 'D'), new Card(12, 'H'));
        myHandsArray[2] = new Hands(new Card(4, 'C'), new Card(5, 'C'), new Card(6, 'C'), new Card(7, 'C'), new Card(8, 'C'));
        myHandsArray[3] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D'));
        myHandsArray[4] = new Hands(new Card(10, 'C'), new Card(11, 'D'), new Card(10, 'D'), new Card(10, 'S'), new Card(10, 'H'));
        myHandsArray[5] = new Hands(new Card(6, 'S'), new Card(7, 'D'), new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'));
        myHandsArray[6] = new Hands(new Card(14, 'C'), new Card(14, 'D'), new Card(6, 'C'), new Card(14, 'S'), new Card(14, 'H'));
        myHandsArray[7] = new Hands(new Card(11, 'H'), new Card(11, 'D'), new Card(11, 'C'), new Card(5, 'H'), new Card(5, 'S'));
        myHandsArray[8] = new Hands(new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H'));
        myHandsArray[9] = new Hands(new Card(8, 'S'), new Card(8, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(8, 'H'));
        myHandsArray[10] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(13, 'S'), new Card(14, 'S'));
        myHandsArray[11] = new Hands(new Card(12, 'D'), new Card(12, 'C'), new Card(9, 'C'), new Card(12, 'S'), new Card(9, 'H'));
        myHandsArray[12] = new Hands(new Card(5, 'S'), new Card(10, 'D'), new Card(10, 'C'), new Card(5, 'C'), new Card(10, 'H'));
        myHandsArray[13] = new Hands(new Card(7, 'S'), new Card(6, 'C'), new Card(5, 'C'), new Card(4, 'H'), new Card(3, 'H'));
        myHandsArray[14] = new Hands(new Card(3, 'C'), new Card(5, 'D'), new Card(3, 'S'), new Card(5, 'S'), new Card(3, 'D'));
        
        for(int i = 0; i < 15; i++)
            myMaxHeap.insert(myHandsArray[i]);

        
        expectedMaxHand[0] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(13, 'S'), new Card(14, 'S'));        
        expectedMaxHand[1] = new Hands(new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H'));        
        expectedMaxHand[2] = new Hands(new Card(4, 'C'), new Card(5, 'C'), new Card(6, 'C'), new Card(7, 'C'), new Card(8, 'C'));
               
        // Action
        for(int i = 0; i < 3; i++)
        {
            passed &= assertEquals(expectedMaxHand[i], myMaxHeap.removeMax());            
        }
            
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }
    
    private static void testRemoveMax2(){
        // Setup
        System.out.println("============testRemoveMax2=============");
        boolean passed = true;
        totalTestCount++;

        HandsMaxHeap myMaxHeap = new HandsMaxHeap(20);
        Hands myHandsArray[] = new Hands[22];  
        Hands expectedMaxHand[] = new Hands[5];

        // [Scott] Need initialization of all hands
        myHandsArray[0] = new Hands(new Card(6, 'S'), new Card(3, 'D'), new Card(6, 'C'), new Card(6, 'H'), new Card(3, 'H'));
        myHandsArray[1] = new Hands(new Card(6, 'D'), new Card(6, 'S'), new Card(3, 'H'), new Card(6, 'C'), new Card(6, 'H'));
        myHandsArray[2] = new Hands(new Card(9, 'C'), new Card(11, 'C'), new Card(12, 'D'), new Card(10, 'H'), new Card(8, 'H'));
        myHandsArray[3] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D'));
        myHandsArray[4] = new Hands(new Card(14, 'D'), new Card(12, 'D'), new Card(10, 'D'), new Card(11, 'D'), new Card(13, 'D'));
        myHandsArray[5] = new Hands(new Card(10, 'S'), new Card(10, 'D'), new Card(8, 'H'), new Card(10, 'H'), new Card(8, 'S'));
        myHandsArray[6] = new Hands(new Card(11, 'C'), new Card(12, 'D'), new Card(12, 'C'), new Card(12, 'S'), new Card(12, 'H'));
        myHandsArray[7] = new Hands(new Card(3, 'H'), new Card(5, 'D'), new Card(4, 'C'), new Card(6, 'H'), new Card(2, 'S'));
        myHandsArray[8] = new Hands(new Card(8, 'H'), new Card(10, 'H'), new Card(9, 'H'), new Card(11, 'H'), new Card(7, 'H'));
        myHandsArray[9] = new Hands(new Card(2, 'S'), new Card(2, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(2, 'H'));
        myHandsArray[10] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(8, 'S'), new Card(9, 'S'));
        myHandsArray[11] = new Hands(new Card(12, 'D'), new Card(12, 'C'), new Card(9, 'C'), new Card(12, 'S'), new Card(9, 'H'));
        myHandsArray[12] = new Hands(new Card(6, 'S'), new Card(5, 'D'), new Card(6, 'C'), new Card(5, 'C'), new Card(5, 'H'));
        myHandsArray[13] = new Hands(new Card(7, 'S'), new Card(6, 'C'), new Card(5, 'C'), new Card(8, 'H'), new Card(9, 'H'));
        myHandsArray[14] = new Hands(new Card(13, 'C'), new Card(5, 'D'), new Card(13, 'S'), new Card(5, 'S'), new Card(13, 'D'));
        myHandsArray[15] = new Hands(new Card(7, 'C'), new Card(8, 'C'), new Card(10, 'C'), new Card(11, 'C'), new Card(9, 'C'));
        myHandsArray[16] = new Hands(new Card(4, 'C'), new Card(4, 'D'), new Card(4, 'S'), new Card(6, 'S'), new Card(4, 'H'));
        myHandsArray[17] = new Hands(new Card(3, 'H'), new Card(5, 'H'), new Card(3, 'S'), new Card(5, 'S'), new Card(5, 'D'));
        myHandsArray[18] = new Hands(new Card(10, 'S'), new Card(8, 'C'), new Card(8, 'S'), new Card(10, 'H'), new Card(10, 'D'));
        myHandsArray[19] = new Hands(new Card(5, 'C'), new Card(8, 'D'), new Card(7, 'S'), new Card(6, 'S'), new Card(9, 'D'));
        myHandsArray[20] = new Hands(new Card(7, 'S'), new Card(7, 'D'), new Card(4, 'S'), new Card(4, 'D'), new Card(7, 'C'));
        myHandsArray[21] = new Hands(new Card(9, 'D'), new Card(10, 'D'), new Card(13, 'D'), new Card(11, 'D'), new Card(12, 'D'));


        for(int i = 0; i < 20; i++)
        myMaxHeap.insert(myHandsArray[i]);


        expectedMaxHand[0] = new Hands(new Card(14, 'D'), new Card(12, 'D'), new Card(10, 'D'), new Card(11, 'D'), new Card(13, 'D'));
        expectedMaxHand[1] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(8, 'S'), new Card(9, 'S'));
        expectedMaxHand[2] = new Hands(new Card(8, 'H'), new Card(10, 'H'), new Card(9, 'H'), new Card(11, 'H'), new Card(7, 'H'));
        expectedMaxHand[3] = new Hands(new Card(7, 'C'), new Card(8, 'C'), new Card(10, 'C'), new Card(11, 'C'), new Card(9, 'C'));
        expectedMaxHand[4] = new Hands(new Card(11, 'C'), new Card(12, 'D'), new Card(12, 'C'), new Card(12, 'S'), new Card(12, 'H'));
                
        // Action
        for(int i = 0; i < 5; i++)
        {
            passed &= assertEquals(expectedMaxHand[i], myMaxHeap.removeMax());            
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void testHeapSort1(){
        // Setup
        System.out.println("============testHeapSort1=============");
        boolean passed = true;
        totalTestCount++;

        Hands myHandsArray[] = new Hands[10];  
        Hands expectedHandsArray[] = new Hands[10];
        
        // [Scott] Need initialization of all hands
        myHandsArray[0] = new Hands(new Card(2, 'C'), new Card(2, 'D'), new Card(6, 'C'), new Card(6, 'S'), new Card(6, 'H')); // FH 6
        myHandsArray[1] = new Hands(new Card(8, 'D'), new Card(9, 'D'), new Card(10, 'H'), new Card(11, 'D'), new Card(12, 'H')); // S HQ
        myHandsArray[2] = new Hands(new Card(4, 'C'), new Card(5, 'C'), new Card(6, 'C'), new Card(7, 'C'), new Card(8, 'C')); // SF C8
        myHandsArray[3] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D')); // FH A
        myHandsArray[4] = new Hands(new Card(10, 'C'), new Card(11, 'D'), new Card(10, 'D'), new Card(10, 'S'), new Card(10, 'H')); // FK 10
        myHandsArray[5] = new Hands(new Card(6, 'S'), new Card(7, 'D'), new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H')); // SF H10
        myHandsArray[6] = new Hands(new Card(14, 'C'), new Card(14, 'D'), new Card(6, 'C'), new Card(14, 'S'), new Card(14, 'H')); // FK A
        myHandsArray[7] = new Hands(new Card(11, 'H'), new Card(11, 'D'), new Card(11, 'C'), new Card(5, 'H'), new Card(5, 'S')); // FH J
        myHandsArray[8] = new Hands(new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H')); // SF HQ
        myHandsArray[9] = new Hands(new Card(8, 'S'), new Card(8, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(8, 'H'));  // FH 8
        
        expectedHandsArray[0] = new Hands(new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H')); // SF HQ
        expectedHandsArray[1] = new Hands(new Card(4, 'C'), new Card(5, 'C'), new Card(6, 'C'), new Card(7, 'C'), new Card(8, 'C')); // SF C8
        expectedHandsArray[2] = new Hands(new Card(14, 'C'), new Card(14, 'D'), new Card(6, 'C'), new Card(14, 'S'), new Card(14, 'H')); // FK A
        expectedHandsArray[3] = new Hands(new Card(10, 'C'), new Card(11, 'D'), new Card(10, 'D'), new Card(10, 'S'), new Card(10, 'H')); // FK 10
        expectedHandsArray[4] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D')); // FH A
        expectedHandsArray[5] = new Hands(new Card(11, 'H'), new Card(11, 'D'), new Card(11, 'C'), new Card(5, 'H'), new Card(5, 'S')); // FH J
        expectedHandsArray[6] = new Hands(new Card(8, 'S'), new Card(8, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(8, 'H'));  // FH 8
        expectedHandsArray[7] = new Hands(new Card(2, 'C'), new Card(2, 'D'), new Card(6, 'C'), new Card(6, 'S'), new Card(6, 'H')); // FH 6
        expectedHandsArray[8] = new Hands(new Card(8, 'D'), new Card(9, 'D'), new Card(10, 'H'), new Card(11, 'D'), new Card(12, 'H')); // S HQ
        expectedHandsArray[9] = new Hands(new Card(6, 'S'), new Card(7, 'D'), new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H')); // S H10
        
        // Action
        heapSort(myHandsArray);

        for(int i = 0; i < 10; i++)
        {            
            passed &= assertEquals(expectedHandsArray[i], myHandsArray[i]);            
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void testHeapSort2(){
        // Setup
        System.out.println("============testHeapSort2=============");
        boolean passed = true;
        totalTestCount++;

        Hands myHandsArray[] = new Hands[20];  
        Hands expectedHandsArray[] = new Hands[20];
       
        // [Scott] Need initialization of all hands
        myHandsArray[0] = new Hands(new Card(6, 'S'), new Card(3, 'D'), new Card(6, 'C'), new Card(6, 'H'), new Card(3, 'H'));
        myHandsArray[1] = new Hands(new Card(6, 'D'), new Card(6, 'S'), new Card(3, 'H'), new Card(6, 'C'), new Card(6, 'H'));
        myHandsArray[2] = new Hands(new Card(9, 'C'), new Card(11, 'C'), new Card(12, 'D'), new Card(10, 'H'), new Card(8, 'H'));
        myHandsArray[3] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D'));
        myHandsArray[4] = new Hands(new Card(14, 'D'), new Card(12, 'D'), new Card(10, 'D'), new Card(11, 'D'), new Card(13, 'D'));
        myHandsArray[5] = new Hands(new Card(10, 'S'), new Card(10, 'D'), new Card(8, 'H'), new Card(10, 'H'), new Card(8, 'S'));
        myHandsArray[6] = new Hands(new Card(11, 'C'), new Card(12, 'D'), new Card(12, 'C'), new Card(12, 'S'), new Card(12, 'H'));
        myHandsArray[7] = new Hands(new Card(3, 'H'), new Card(5, 'D'), new Card(4, 'C'), new Card(6, 'H'), new Card(2, 'S'));
        myHandsArray[8] = new Hands(new Card(8, 'H'), new Card(10, 'H'), new Card(9, 'H'), new Card(11, 'H'), new Card(7, 'H'));
        myHandsArray[9] = new Hands(new Card(2, 'S'), new Card(2, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(2, 'H'));
        myHandsArray[10] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(8, 'S'), new Card(9, 'S'));
        myHandsArray[11] = new Hands(new Card(12, 'D'), new Card(12, 'C'), new Card(9, 'C'), new Card(12, 'S'), new Card(9, 'H'));
        myHandsArray[12] = new Hands(new Card(6, 'S'), new Card(5, 'D'), new Card(6, 'C'), new Card(5, 'C'), new Card(5, 'H'));
        myHandsArray[13] = new Hands(new Card(7, 'S'), new Card(6, 'C'), new Card(5, 'C'), new Card(8, 'H'), new Card(9, 'H'));
        myHandsArray[14] = new Hands(new Card(13, 'C'), new Card(5, 'D'), new Card(13, 'S'), new Card(5, 'S'), new Card(13, 'D'));
        myHandsArray[15] = new Hands(new Card(7, 'C'), new Card(8, 'C'), new Card(10, 'C'), new Card(11, 'C'), new Card(9, 'C'));
        myHandsArray[16] = new Hands(new Card(4, 'C'), new Card(4, 'D'), new Card(4, 'S'), new Card(6, 'S'), new Card(4, 'H'));
        myHandsArray[17] = new Hands(new Card(3, 'H'), new Card(5, 'H'), new Card(3, 'S'), new Card(5, 'S'), new Card(5, 'D'));
        myHandsArray[18] = new Hands(new Card(10, 'S'), new Card(8, 'C'), new Card(8, 'S'), new Card(10, 'H'), new Card(10, 'D'));
        myHandsArray[19] = new Hands(new Card(5, 'C'), new Card(8, 'D'), new Card(7, 'S'), new Card(6, 'S'), new Card(9, 'D'));
        
        expectedHandsArray[0] = new Hands(new Card(14, 'D'), new Card(12, 'D'), new Card(10, 'D'), new Card(11, 'D'), new Card(13, 'D'));
        expectedHandsArray[1] = new Hands(new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(8, 'S'), new Card(9, 'S'));
        expectedHandsArray[2] = new Hands(new Card(8, 'H'), new Card(10, 'H'), new Card(9, 'H'), new Card(11, 'H'), new Card(7, 'H'));
        expectedHandsArray[3] = new Hands(new Card(7, 'C'), new Card(8, 'C'), new Card(10, 'C'), new Card(11, 'C'), new Card(9, 'C'));
        expectedHandsArray[4] = new Hands(new Card(11, 'C'), new Card(12, 'D'), new Card(12, 'C'), new Card(12, 'S'), new Card(12, 'H'));
        expectedHandsArray[5] = new Hands(new Card(6, 'D'), new Card(6, 'S'), new Card(3, 'H'), new Card(6, 'C'), new Card(6, 'H'));
        expectedHandsArray[6] = new Hands(new Card(4, 'C'), new Card(4, 'D'), new Card(4, 'S'), new Card(6, 'S'), new Card(4, 'H'));
        expectedHandsArray[7] = new Hands(new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D'));
        expectedHandsArray[8] = new Hands(new Card(13, 'C'), new Card(5, 'D'), new Card(13, 'S'), new Card(5, 'S'), new Card(13, 'D'));
        expectedHandsArray[9] = new Hands(new Card(12, 'D'), new Card(12, 'C'), new Card(9, 'C'), new Card(12, 'S'), new Card(9, 'H'));
        expectedHandsArray[10] = new Hands(new Card(10, 'S'), new Card(10, 'D'), new Card(8, 'H'), new Card(10, 'H'), new Card(8, 'S'));
        expectedHandsArray[11] = new Hands(new Card(10, 'S'), new Card(8, 'C'), new Card(8, 'S'), new Card(10, 'H'), new Card(10, 'D'));
        expectedHandsArray[12] = new Hands(new Card(6, 'S'), new Card(3, 'D'), new Card(6, 'C'), new Card(6, 'H'), new Card(3, 'H'));
        expectedHandsArray[13] = new Hands(new Card(3, 'H'), new Card(5, 'H'), new Card(3, 'S'), new Card(5, 'S'), new Card(5, 'D'));
        expectedHandsArray[14] = new Hands(new Card(6, 'S'), new Card(5, 'D'), new Card(6, 'C'), new Card(5, 'C'), new Card(5, 'H'));
        expectedHandsArray[15] = new Hands(new Card(2, 'S'), new Card(2, 'D'), new Card(7, 'S'), new Card(7, 'C'), new Card(2, 'H'));
        expectedHandsArray[16] = new Hands(new Card(9, 'C'), new Card(11, 'C'), new Card(12, 'D'), new Card(10, 'H'), new Card(8, 'H'));
        expectedHandsArray[17] = new Hands(new Card(7, 'S'), new Card(6, 'C'), new Card(5, 'C'), new Card(8, 'H'), new Card(9, 'H'));
        expectedHandsArray[18] = new Hands(new Card(5, 'C'), new Card(8, 'D'), new Card(7, 'S'), new Card(6, 'S'), new Card(9, 'D'));
        expectedHandsArray[19] = new Hands(new Card(3, 'H'), new Card(5, 'D'), new Card(4, 'C'), new Card(6, 'H'), new Card(2, 'S'));
           
            
            
        
        // Action
        heapSort(myHandsArray);

        for(int i = 0; i < 20; i++)
        {
            passed &= assertEquals(expectedHandsArray[i], myHandsArray[i]);            
        }

        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static boolean assertEquals(Hands a, Hands b)
    {
        if(!a.isMyHandEqual(b))
        {
            System.out.println("\tAssert Failed!");
            System.out.printf("\tExpected: ");
            a.printMyHand();
            System.out.printf(", Actual: ");
            b.printMyHand();
            System.out.printf("\n");
            return false;
        }

        return true;
    }

    private static boolean assertEquals(int a, int b)
    {
        if(a != b)
        {
            System.out.println("\tAssert Failed!");
            System.out.printf("\tExpected: %d, Actual: %d\n\n", a, b);
            return false;
        }

        return true;
    }
    
   private static void CustomTestInsert1(){
        // Setup
        System.out.println("============CustomTestInsert1=============");
        boolean passed = true;
        totalTestCount++;

        // Test inserting into a heap with initial capacity 1 (testing resize functionality)
        HandsMaxHeap myMaxHeap = new HandsMaxHeap(1);
        
        // Create 3 hands to test resizing
        Hands[] testHands = new Hands[3];
        testHands[0] = new Hands(
            new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(13, 'S'), new Card(14, 'S')
        ); // Straight flush (highest)
        
        testHands[1] = new Hands(
            new Card(8, 'H'), new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H')
        ); // Straight flush (lower)
        
        testHands[2] = new Hands(
            new Card(14, 'S'), new Card(14, 'H'), new Card(14, 'D'), new Card(10, 'C'), new Card(10, 'D')
        ); // Full house
        
        // Insert all hands
        for (Hands hand : testHands) {
            myMaxHeap.insert(hand);
        }
        
        // Verify heap size and capacity after resizing
        if (myMaxHeap.getSize() != 3) {
            System.out.println("\tAssert Failed! Expected size: 3, Actual: " + myMaxHeap.getSize());
            passed = false;
        }
        
        if (myMaxHeap.capacity != 2) { // Should have doubled from 1 to 2
            System.out.println("\tAssert Failed! Expected capacity: 2, Actual: " + myMaxHeap.capacity);
            passed = false;
        }
        
        // Verify max hand is at root
        Hands maxHand = myMaxHeap.removeMax();
        if (!testHands[0].isMyHandEqual(maxHand)) {
            System.out.println("\tAssert Failed! Wrong max hand removed");
            passed = false;
        }
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void CustomTestInsert2(){
        // Setup
        System.out.println("============CustomTestInsert2=============");
        boolean passed = true;
        totalTestCount++;

        // Test inserting hands in random order and verify heap property
        HandsMaxHeap myMaxHeap = new HandsMaxHeap(10);
        
        // Create hands with different ranks (inserting in non-sorted order)
        Hands hand1 = new Hands(
            new Card(2, 'C'), new Card(2, 'D'), new Card(6, 'C'), new Card(6, 'S'), new Card(6, 'H')
        ); // Full house 6s over 2s
        
        Hands hand2 = new Hands(
            new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(13, 'S'), new Card(14, 'S')
        ); // Royal straight flush (highest)
        
        Hands hand3 = new Hands(
            new Card(4, 'C'), new Card(5, 'C'), new Card(6, 'C'), new Card(7, 'C'), new Card(8, 'C')
        ); // Straight flush
        
        Hands hand4 = new Hands(
            new Card(3, 'C'), new Card(3, 'D'), new Card(3, 'S'), new Card(5, 'H'), new Card(5, 'D')
        ); // Full house 3s over 5s (lowest)
        
        // Insert in random order
        myMaxHeap.insert(hand1);  // Full house
        myMaxHeap.insert(hand2);  // Royal straight flush (should be max)
        myMaxHeap.insert(hand3);  // Straight flush
        myMaxHeap.insert(hand4);  // Full house (lowest)
        
        // Verify heap size
        if (myMaxHeap.getSize() != 4) {
            System.out.println("\tAssert Failed! Expected size: 4, Actual: " + myMaxHeap.getSize());
            passed = false;
        }
        
        // Verify that root is the max (hand2)
        Hands maxHand = myMaxHeap.removeMax();
        if (!hand2.isMyHandEqual(maxHand)) {
            System.out.println("\tAssert Failed! Wrong max hand");
            passed = false;
        }
        
        // Verify next max (hand3)
        Hands nextHand = myMaxHeap.removeMax();
        if (!hand3.isMyHandEqual(nextHand)) {
            System.out.println("\tAssert Failed! Wrong second max hand");
            passed = false;
        }
        
        // Verify next (hand1 should be before hand4 since it's a higher full house)
        nextHand = myMaxHeap.removeMax();
        if (!hand1.isMyHandEqual(nextHand)) {
            System.out.println("\tAssert Failed! Wrong third hand");
            passed = false;
        }
        
        // Verify last (hand4)
        nextHand = myMaxHeap.removeMax();
        if (!hand4.isMyHandEqual(nextHand)) {
            System.out.println("\tAssert Failed! Wrong fourth hand");
            passed = false;
        }
        
        // Verify heap is empty
        if (!myMaxHeap.isEmpty()) {
            System.out.println("\tAssert Failed! Heap should be empty");
            passed = false;
        }
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void CustomTestRemoveMax1(){
        // Setup
        System.out.println("============CustomTestRemoveMax1=============");
        boolean passed = true;
        totalTestCount++;

        // Test removeMax on empty heap (should throw RuntimeException)
        HandsMaxHeap emptyHeap = new HandsMaxHeap(5);
        
        try {
            emptyHeap.removeMax();
            // If we get here, no exception was thrown - test fails
            System.out.println("\tAssert Failed! Expected RuntimeException for empty heap removal");
            passed = false;
        } catch (RuntimeException e) {
            // Expected exception - test passes
            passed = true;
        } catch (Exception e) {
            // Wrong type of exception
            System.out.println("\tAssert Failed! Expected RuntimeException but got: " + e.getClass().getName());
            passed = false;
        }
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }

    private static void CustomTestRemoveMax2(){
        // Setup
        System.out.println("============CustomTestRemoveMax2=============");
        boolean passed = true;
        totalTestCount++;

        // Test removeMax preserves heap property after each removal
        HandsMaxHeap myMaxHeap = new HandsMaxHeap(10);
        
        // Create 5 test hands
        Hands[] testHands = new Hands[5];
        testHands[0] = new Hands(
            new Card(10, 'S'), new Card(11, 'S'), new Card(12, 'S'), new Card(13, 'S'), new Card(14, 'S')
        ); // Highest
        
        testHands[1] = new Hands(
            new Card(9, 'H'), new Card(10, 'H'), new Card(11, 'H'), new Card(12, 'H'), new Card(13, 'H')
        ); // Second highest
        
        testHands[2] = new Hands(
            new Card(14, 'C'), new Card(14, 'D'), new Card(14, 'S'), new Card(14, 'H'), new Card(2, 'C')
        ); // Four of a kind Aces (third)
        
        testHands[3] = new Hands(
            new Card(13, 'C'), new Card(13, 'D'), new Card(13, 'S'), new Card(13, 'H'), new Card(3, 'C')
        ); // Four of a kind Kings (fourth)
        
        testHands[4] = new Hands(
            new Card(8, 'C'), new Card(8, 'D'), new Card(8, 'S'), new Card(8, 'H'), new Card(4, 'C')
        ); // Four of a kind 8s (lowest)
        
        // Insert in random order
        myMaxHeap.insert(testHands[3]); // Kings
        myMaxHeap.insert(testHands[1]); // Straight flush H13
        myMaxHeap.insert(testHands[4]); // 8s
        myMaxHeap.insert(testHands[0]); // Royal straight flush (highest)
        myMaxHeap.insert(testHands[2]); // Aces
        
        // Verify size
        if (myMaxHeap.getSize() != 5) {
            System.out.println("\tAssert Failed! Expected size: 5, Actual: " + myMaxHeap.getSize());
            passed = false;
        }
        
        // Remove all hands in descending order
        for (int i = 0; i < 5; i++) {
            Hands removed = myMaxHeap.removeMax();
            if (!testHands[i].isMyHandEqual(removed)) {
                System.out.println("\tAssert Failed! Wrong hand removed at position " + i);
                passed = false;
            }
            
            // After each removal, verify the heap size decreases
            if (myMaxHeap.getSize() != 4 - i) {
                System.out.println("\tAssert Failed! Expected size after removal: " + (4 - i) + 
                                ", Actual: " + myMaxHeap.getSize());
                passed = false;
            }
        }
        
        // Verify heap is empty after all removals
        if (!myMaxHeap.isEmpty()) {
            System.out.println("\tAssert Failed! Heap should be empty");
            passed = false;
        }
        
        // Try one more removal (should throw exception)
        try {
            myMaxHeap.removeMax();
            System.out.println("\tAssert Failed! Expected RuntimeException after emptying heap");
            passed = false;
        } catch (RuntimeException e) {
            // Expected exception - this is good
            passed = passed && true;
        }
        
        // Tear Down
        totalPassed &= passed;
        if(passed) 
        {
            System.out.println("\tPassed");
            totalPassCount++;            
        }
    }



}
