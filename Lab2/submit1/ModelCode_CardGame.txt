public class ModelCode_CardGame {

    public static final int POCKETSIZE = 25;

    public static CardPool myCardPool;
    public static HandsMaxHeap myMaxHeap;

    public static Card[] myCards;
    public static int pocketSize = POCKETSIZE;

    // [Problem 2] Generate All Possible Valid Hands from the Pocket Cards and store them in myMaxHeap
    public static void generateHands(Card[] thisPocket)
    {
        // If thisPocket has less than 5 cards, no hand can be generated, thus the heap will be empty
        if (thisPocket.length < 5) {
            myMaxHeap = new HandsMaxHeap(10); // Create a small empty heap
            return;
        }
        
        // Otherwise, generate all possible valid hands from thisPocket and store them in myMaxHeap
        // Pay attention that memory needs to be allocated for the heap!
        
        // Calculate total number of 5 card combinations: C(n, 5) = n! / (5! * (n-5)!)
        int n = thisPocket.length;
        int totalCombinations = (n * (n - 1) * (n - 2) * (n - 3) * (n - 4)) / 120;

        // Create heap with capacity equal to total combinations
        myMaxHeap = new HandsMaxHeap(totalCombinations);

        // Generate all combinations of 5 cards from the pocket
        for (int i = 0; i < n - 4; i++) {
            for (int j = i + 1; j < n - 3; j++) {
                for (int k = j + 1; k < n - 2; k++) {
                    for (int l = k + 1; l < n - 1; l++) {
                        for (int m = l + 1; m < n; m++) {
                            // Create a hand with these 5 cards
                            Hands hand = new Hands(
                                thisPocket[i], thisPocket[j], thisPocket[k], 
                                thisPocket[l], thisPocket[m]
                            );
                            
                            // Only insert valid hands into the heap
                            if (hand.isAValidHand()) {
                                myMaxHeap.insert(hand);
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("Generated " + myMaxHeap.getSize() + " valid hands from pocket cards.");
    }

    // Sorts the array of Cards in ascending order: ascending order of ranks; 
    // cards of equal ranks are sorted in ascending order of suits
    public static void sortCards(Card[] cards)
    {
        int j;
        Card temp;        
        int size = cards.length;
        
        for(int i = 1; i < size; i++) 
        { 
            temp = cards[i];		
            for(j = i; j > 0 && cards[j-1].isMyCardLarger(temp); j--) 
                cards[j] = cards[j-1]; 
            cards[j] = temp;
        }  
    }
    
    // Helper method to remove cards from pocket after a move
    public static void removeCardsFromPocket(Card[] cardsToRemove) {
        // Create a new array for remaining cards
        Card[] newPocket = new Card[pocketSize - 5];
        int newIndex = 0;
        
        // For each card in current pocket
        for (int i = 0; i < pocketSize; i++) {
            boolean shouldRemove = false;
            
            // Check if this card is in the cards to remove
            for (int j = 0; j < 5; j++) {
                if (myCards[i].isMyCardEqual(cardsToRemove[j])) {
                    shouldRemove = true;
                    break;
                }
            }
            
            // If not to be removed, add to new pocket
            if (!shouldRemove) {
                newPocket[newIndex++] = myCards[i];
            }
        }
        
        // Update pocket
        myCards = newPocket;
        pocketSize -= 5;
        
        // Sort the new pocket
        sortCards(myCards);
    }
    
    // Helper method to update heap after removing cards
    public static void updateHeapAfterCardRemoval(Card[] removedCards) {
        // Create a new heap to store valid hands that don't contain removed cards
        HandsMaxHeap newHeap = new HandsMaxHeap(myMaxHeap.getSize() + 10);
        
        // Temporary array to store hands from current heap
        int tempSize = myMaxHeap.getSize();
        Hands[] tempHands = new Hands[tempSize];
        
        // Extract all hands from current heap
        for (int i = 0; i < tempSize; i++) {
            try {
                tempHands[i] = myMaxHeap.removeMax();
            } catch (RuntimeException e) {
                break; // Heap is empty
            }
        }
        
        // Re-insert only hands that don't contain removed cards
        for (int i = 0; i < tempSize; i++) {
            Hands hand = tempHands[i];
            if (hand == null) continue;
            
            boolean containsRemovedCard = false;
            
            // Check if this hand contains any of the removed cards
            for (int j = 0; j < 5; j++) {
                Card removedCard = removedCards[j];
                if (hand.hasCard(removedCard)) {
                    containsRemovedCard = true;
                    break;
                }
            }
            
            // Only insert if hand doesn't contain removed cards
            if (!containsRemovedCard) {
                newHeap.insert(hand);
            }
        }
        
        // Replace old heap with new heap
        myMaxHeap = newHeap;
    }

    public static void main(String args[])
    {
        Hands myMove;        
        
        myCardPool = new CardPool();        
        System.out.println("Full Card Pool:");
        myCardPool.printPool();

        myCards = myCardPool.getRandomCards(POCKETSIZE);  
        sortCards(myCards);

        // print cards
        System.out.println("\nMy Pocket Cards (25 cards, sorted):");
        for(int j = 0; j < pocketSize; j++)
        {            
            myCards[j].printCard();
            if ((j + 1) % 13 == 0) System.out.println(); // New line every 13 cards
        }
        System.out.println();
        
        generateHands(myCards); // generates all valid hands from myCards and stores them in myMaxHeap
        
        System.out.println("\nInitial Max Heap contents (" + myMaxHeap.getSize() + " hands):");
        myMaxHeap.printHeap(); // prints the contents of the initial heap
        System.out.println();

        // [Problem 3] Implementing Game Logic Part 1 - Aggressive AI: Always Picks the Strongest Hand
        int round = 1;
        while (pocketSize >= 5)
        {            
            System.out.println("\n=== Round " + round + " ===");
                                   
            // Step 1:
            // - Check if the Max Heap contains any valid hands 
            //   - if yes, remove the Largest Hand from the heap as the current Move
            //   - otherwise, you are out of valid hands.  Just pick any 5 cards from the pocket as a "Pass Move"
            // - Once a move is chosen, print the Hand for confirmation. MUST PRINT FOR VALIDATION!!
            
            if (!myMaxHeap.isEmpty()) {
                // Get the strongest hand (max hand)
                myMove = myMaxHeap.removeMax();
                System.out.print("AI plays aggressive hand: ");
                myMove.printMyHand();
                System.out.println();
            } else {
                // No valid hands left, pick any 5 cards as a "Pass Move"
                System.out.println("No valid hands left. Playing pass move with any 5 cards:");
                Card[] passCards = new Card[5];
                for (int j = 0; j < 5; j++) {
                    passCards[j] = myCards[j];
                }
                myMove = new Hands(passCards[0], passCards[1], passCards[2], passCards[3], passCards[4]);
                myMove.printMyHand();
                System.out.println(" [Pass Move]");
            }
            
            // Step 2:
            // - Remove the Cards used in the move from the pocket cards and update the Max Heap
            // - Print the remaining cards and the contents of the heap
            
            // Extract cards from the played hand
            Card[] playedCards = new Card[5];
            for (int j = 0; j < 5; j++) {
                playedCards[j] = myMove.getCard(j);
            }
            
            // Remove these cards from pocket
            removeCardsFromPocket(playedCards);
            
            // Update heap to remove all hands containing these cards
            updateHeapAfterCardRemoval(playedCards);
            
            // Print remaining pocket cards
            System.out.print("Remaining pocket cards (" + pocketSize + "): ");
            for (int j = 0; j < pocketSize; j++) {
                myCards[j].printCard();
            }
            System.out.println();
            
            // Print updated heap contents
            System.out.print("Updated heap contents (" + myMaxHeap.getSize() + " hands): ");
            if (myMaxHeap.getSize() > 0) {
                myMaxHeap.printHeap();
            } else {
                System.out.println("[Empty]");
            }
            
            round++;
        }
        
        System.out.println("\n=== Game Over ===");
        System.out.println("All cards have been used.");
        
        // Summary
        System.out.println("\nSummary:");
        System.out.println("- Started with 25 pocket cards");
        System.out.println("- Played " + (round-1) + " rounds");
        System.out.println("- Final pocket size: " + pocketSize + " cards");
    }
}