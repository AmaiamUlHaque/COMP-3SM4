import java.util.*;

public class ModelCode_CardGame_AI_MaxHeap
{
    public static final int POCKETSIZE = 25;
    public static Scanner myInputScanner;          
    
    public static void main(String args[]) throws Exception
    {        
        CardPool myCardPool;
        Card[] aiCards, myCards;        
        Hands aiHand, myHand;
        
        HandsMaxHeap aiHeap; // Using MaxHeap for AI (aggressive player)
        HandsRBT myRBT;      // Using RBT for human player
                
        int aiPocketSize = POCKETSIZE, myPocketSize = POCKETSIZE;
        int aiScore = 0, playerScore = 0; 
        
        myCardPool = new CardPool();         
        myInputScanner = new Scanner(System.in);

        System.out.println("=== GAME INITIALIZATION ===");
        
        // Get 25 random cards for both AI and Player
        myCards = myCardPool.getRandomCards(myPocketSize);
        aiCards = myCardPool.getRandomCards(aiPocketSize);
        
        // Sort cards
        sortCards(myCards);
        sortCards(aiCards);
        
        System.out.println("\n--- Player's Initial Cards ---");
        for (int i = 0; i < myPocketSize; i++) {
            System.out.print((i + 1) + ": ");
            myCards[i].printCard();
            System.out.println();
        }
        
        System.out.println("\n--- AI's Initial Cards ---");
        for (Card card : aiCards) {
            card.printCard();
        }
        System.out.println("\n");

        // Instantiate data structures and generate all possible hands
        myRBT = new HandsRBT();
        aiHeap = new HandsMaxHeap(calculateCombinations(aiPocketSize));
        
        generateHandsIntoRBT(myCards, myRBT);
        generateHandsIntoMaxHeap(aiCards, aiHeap);
        
        System.out.println("\n=== GAME START ===");
        
        // Step 2 - Game Loop Logic
        for (int round = 1; round <= 5; round++) {
            System.out.println("\n========== ROUND " + round + " ==========");

            // Print AI pocket cards
            System.out.println("\n--- AI POCKET CARDS (Round " + round + ") ---");
            if (aiPocketSize > 0) {
                for (int i = 0; i < aiPocketSize; i++) {
                    aiCards[i].printCard();
                    if ((i + 1) % 5 == 0) System.out.println();
                }
                System.out.println();
            } else {
                System.out.println("AI has no cards left!");
            }
            
            // Print all valid hands available to the player as a list
            System.out.println("\n--- PLAYER'S VALID HANDS AVAILABLE (Round " + round + ") ---");
            if (myRBT.isEmpty()) {
                System.out.println("No valid hands available! You must pass with any 5 cards.");
            } else {
                System.out.println("Here are all your valid hand options (" + myRBT.size() + " total):");
                myRBT.printAllHandsInOrder();
            }
            
            // Print Player pocket cards with serial numbers
            System.out.println("--- PLAYER POCKET CARDS (Round " + round + ") ---");
            if (myPocketSize > 0) {
                for (int i = 0; i < myPocketSize; i++) {
                    System.out.print((i + 1) + ": ");
                    myCards[i].printCard();
                    System.out.println();
                }
            } else {
                System.out.println("Player has no cards left!");
            }

            // PLAYER TURN - Select a hand
            boolean validChoice = false;
            Hands playerHand = null;
            
            if (myPocketSize < 5) {
                System.out.println("Player doesn't have enough cards to make a hand!");
                playerHand = null;
            } else {
                do {
                    System.out.println("\n--- PLAYER CARD SELECTION (Round " + round + ") ---");
                    if (!myRBT.isEmpty()) {
                        System.out.println("Remember: You must choose a valid hand from the list above!");
                    } else {
                        System.out.println("No valid hands left. You can choose any 5 cards to pass.");
                    }
                    
                    playerHand = getUserHand(myCards, myPocketSize);
                    
                    // Check if the hand is valid and exists in RBT
                    if (playerHand.isAValidHand()) {
                        HandsRBTNode foundNode = myRBT.findNode(playerHand);
                        if (foundNode != null) {
                            validChoice = true;
                            System.out.print("Valid hand chosen: ");
                            playerHand.printMyHand();
                            System.out.println();
                        } else {
                            if (!myRBT.isEmpty()) {
                                System.out.println("Invalid choice! This hand is not in your valid hands list.");
                                System.out.println("You still have valid hands available. Please choose again.");
                            } else {
                                validChoice = true;
                                System.out.print("Pass hand chosen: ");
                                playerHand.printMyHand();
                                System.out.println();
                            }
                        }
                    } else {
                        if (!myRBT.isEmpty()) {
                            System.out.println("Invalid choice! This is not a valid poker hand.");
                            System.out.println("You still have valid hands available. Please choose again.");
                        } else {
                            validChoice = true;
                            System.out.print("Pass hand chosen (invalid hand): ");
                            playerHand.printMyHand();
                            System.out.println();
                        }
                    }
                } while (!validChoice);
            }

            // Update Player's game state after move
            if (playerHand != null) {
                System.out.println("\n--- Updating Player's Game State ---");
                System.out.print("Removing hand: ");
                playerHand.printMyHand();
                System.out.println("\nRemoving all hands containing these cards from RBT...");
                
                // Remove all hands containing any of the 5 cards from RBT
                myRBT.deleteInvalidHands(playerHand);
                
                // Remove the 5 chosen cards from player's pocket
                Card[] newMyCards = new Card[myPocketSize - 5];
                int newIndex = 0;
                int[] selectedIndices = getLastSelectedIndices();
                
                for (int i = 0; i < myPocketSize; i++) {
                    boolean isSelected = false;
                    for (int j = 0; j < 5; j++) {
                        if (i == selectedIndices[j]) {
                            isSelected = true;
                            break;
                        }
                    }
                    if (!isSelected) {
                        newMyCards[newIndex++] = myCards[i];
                    }
                }
                
                myCards = newMyCards;
                myPocketSize -= 5;
                System.out.println("Player now has " + myPocketSize + " cards remaining.");
            }

            // AI TURN - Aggressive AI using MaxHeap
            aiHand = null;
            
            if (aiPocketSize >= 5) {
                // AI chooses the strongest hand (max hand from heap)
                if (!aiHeap.isEmpty()) {
                    try {
                        aiHand = aiHeap.removeMax();
                        System.out.println("\n--- AI's Chosen Hand (Round " + round + ") ---");
                        System.out.print("AI plays aggressive hand: ");
                        aiHand.printMyHand();
                        System.out.println();
                        
                        // Extract cards from AI's hand
                        Card[] aiPlayedCards = new Card[5];
                        for (int j = 0; j < 5; j++) {
                            aiPlayedCards[j] = aiHand.getCard(j);
                        }
                        
                        // Remove these cards from AI's pocket
                        Card[] newAiCards = new Card[aiPocketSize - 5];
                        int newAiIndex = 0;
                        
                        for (int i = 0; i < aiPocketSize; i++) {
                            boolean shouldRemove = false;
                            for (int j = 0; j < 5; j++) {
                                if (aiCards[i].isMyCardEqual(aiPlayedCards[j])) {
                                    shouldRemove = true;
                                    break;
                                }
                            }
                            if (!shouldRemove) {
                                newAiCards[newAiIndex++] = aiCards[i];
                            }
                        }
                        
                        aiCards = newAiCards;
                        aiPocketSize -= 5;
                        
                        // Regenerate AI's heap with remaining cards
                        aiHeap = new HandsMaxHeap(calculateCombinations(aiPocketSize));
                        generateHandsIntoMaxHeap(aiCards, aiHeap);
                        
                        System.out.println("AI now has " + aiPocketSize + " cards remaining.");
                        
                    } catch (RuntimeException e) {
                        System.out.println("Error removing max hand from heap: " + e.getMessage());
                    }
                } else {
                    // No valid hands, AI makes a pass move with first 5 cards
                    System.out.println("\n--- AI has no valid hands, must pass ---");
                    aiHand = new Hands(aiCards[0], aiCards[1], aiCards[2], aiCards[3], aiCards[4]);
                    System.out.print("AI passes with: ");
                    aiHand.printMyHand();
                    System.out.println();
                    
                    Card[] newAiCards = new Card[aiPocketSize - 5];
                    System.arraycopy(aiCards, 5, newAiCards, 0, aiPocketSize - 5);
                    aiCards = newAiCards;
                    aiPocketSize -= 5;
                    
                    // Regenerate heap with remaining cards (though it will be empty)
                    aiHeap = new HandsMaxHeap(calculateCombinations(aiPocketSize));
                    generateHandsIntoMaxHeap(aiCards, aiHeap);
                    
                    System.out.println("AI now has " + aiPocketSize + " cards remaining.");
                }
            }

            // Determine round winner
            System.out.println("\n--- ROUND " + round + " RESULTS ---");
            System.out.print("Player's Hand: ");
            if (playerHand != null) {
                playerHand.printMyHand();
            } else {
                System.out.print("No hand (out of cards)");
            }
            System.out.println();
            
            System.out.print("AI's Hand: ");
            if (aiHand != null) {
                aiHand.printMyHand();
            } else {
                System.out.print("No hand (out of cards)");
            }
            System.out.println();
            
            // Determine winner
            if (playerHand != null && aiHand != null) {
                if (playerHand.isMyHandLarger(aiHand)) {
                    System.out.println(">>> PLAYER WINS ROUND " + round + "! <<<");
                    playerScore++;
                } else if (aiHand.isMyHandLarger(playerHand)) {
                    System.out.println(">>> AI WINS ROUND " + round + "! <<<");
                    aiScore++;
                } else {
                    System.out.println(">>> ROUND " + round + " IS A DRAW! <<<");
                }
            } else if (playerHand != null) {
                System.out.println(">>> PLAYER WINS ROUND " + round + " (AI has no hand)! <<<");
                playerScore++;
            } else if (aiHand != null) {
                System.out.println(">>> AI WINS ROUND " + round + " (Player has no hand)! <<<");
                aiScore++;
            }
            
            System.out.println("\nCurrent Score - Player: " + playerScore + ", AI: " + aiScore);
        }

        // Report final results
        System.out.println("\n========== GAME OVER ==========");
        System.out.println("Final Score:");
        System.out.println("  Player: " + playerScore);
        System.out.println("  AI: " + aiScore);
        
        if (playerScore > aiScore) {
            System.out.println("\n*** CONGRATULATIONS! PLAYER WINS THE GAME! ***");
        } else if (aiScore > playerScore) {
            System.out.println("\n*** AI WINS THE GAME! BETTER LUCK NEXT TIME! ***");
        } else {
            System.out.println("\n*** THE GAME IS A TIE! ***");
        }
        
        myInputScanner.close();
    }

    // Helper method to calculate number of 5-card combinations
    private static int calculateCombinations(int n) {
        if (n < 5) return 10; // Small default if less than 5 cards
        return (n * (n - 1) * (n - 2) * (n - 3) * (n - 4)) / 120;
    }

    // Store the last selected indices for card removal
    private static int[] lastSelectedIndices = new int[5];
    
    private static int[] getLastSelectedIndices() {
        return lastSelectedIndices;
    }

    public static void generateHandsIntoMaxHeap(Card[] cards, HandsMaxHeap heap)
    {
        int n = cards.length;
        int validCount = 0;
        
        // Generate all possible 5-card combinations
        for (int i = 0; i < n - 4; i++) {
            for (int j = i + 1; j < n - 3; j++) {
                for (int k = j + 1; k < n - 2; k++) {
                    for (int l = k + 1; l < n - 1; l++) {
                        for (int m = l + 1; m < n; m++) {
                            Hands hand = new Hands(cards[i], cards[j], cards[k], cards[l], cards[m]);
                            if (hand.isAValidHand()) {
                                heap.insert(hand);
                                validCount++;
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("Generated " + validCount + " valid hands for AI (stored in MaxHeap)");
    }

    public static void generateHandsIntoRBT(Card[] cards, HandsRBT thisRBT)
    {
        // Populate all valid hands into the RBT
        int n = cards.length;
        int validCount = 0;
        
        // Generate all possible 5-card combinations
        for (int i = 0; i < n - 4; i++) {
            for (int j = i + 1; j < n - 3; j++) {
                for (int k = j + 1; k < n - 2; k++) {
                    for (int l = k + 1; l < n - 1; l++) {
                        for (int m = l + 1; m < n; m++) {
                            Hands hand = new Hands(cards[i], cards[j], cards[k], cards[l], cards[m]);
                            if (hand.isAValidHand()) {
                                thisRBT.insert(hand);
                                validCount++;
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("Generated " + validCount + " valid hands for Player (stored in RBT)");
    }

    public static void sortCards(Card[] cards)
    {
        // Sort the cards in increasing order of rank; for equal rank sort in increasing order of suite
        int j, size;
        Card temp; 
        size = cards.length;

        for(int i = 1; i < size; i++) 
        { 
            temp = cards[i];		
            for(j = i; j > 0 && cards[j-1].isMyCardLarger(temp); j--) 
                cards[j] = cards[j-1]; 
            cards[j] = temp;
        }  
    }

    // Helper method to print all hands in RBT as a list
    private static void printValidHandsList(HandsRBT rbt) {
        rbt.printAllHandsInOrder();
    }

    // This method enables Player to use the numerical key entries to select
    // the 5 cards to form a hand as a tentative move.
    public static Hands getUserHand(Card[] myCards, int pocketSize)
    {
        int[] mySelection = new int[5];  
        boolean validInput;

        System.out.println("Available cards (1-" + pocketSize + "):");
        
        for(int i = 0; i < 5; i++)
        {            
            do {
                validInput = true;
                System.out.printf("Card Choice #%d: ", i + 1);
                mySelection[i] = myInputScanner.nextInt() - 1; // Convert to 0-based index
                
                // Validate input
                if(mySelection[i] < 0 || mySelection[i] >= pocketSize) {
                    System.out.println("Invalid choice! Please enter a number between 1 and " + pocketSize);
                    validInput = false;
                }
                
                // Check for duplicate selections
                for(int j = 0; j < i; j++) {
                    if(mySelection[i] == mySelection[j]) {
                        System.out.println("Duplicate selection! Please choose a different card.");
                        validInput = false;
                        break;
                    }
                }
            } while(!validInput);
        }
        
        // Store the selected indices for later removal
        lastSelectedIndices = mySelection.clone();
        
        Hands newHand = new Hands(  myCards[mySelection[0]], 
                                    myCards[mySelection[1]], 
                                    myCards[mySelection[2]], 
                                    myCards[mySelection[3]], 
                                    myCards[mySelection[4]]);

        return newHand;
    }
}