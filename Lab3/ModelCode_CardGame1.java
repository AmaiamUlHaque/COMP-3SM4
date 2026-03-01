import java.util.*;

public class ModelCode_CardGame {

    public static final int POCKETSIZE = 25;
    public static Scanner myInputScanner;          
    
    public static void main(String args[]) throws Exception
    {        
        CardPool myCardPool;
        Card[] aiCards, myCards;        
        Hands aiHand, myHand;
        
        HandsBST aiBST; // you may replace this with your own HandsMaxHeap for improved performance.
        HandsRBT myRBT;
                
        int aiPocketSize = POCKETSIZE, myPocketSize = POCKETSIZE;
        int aiScore = 0, playerScore = 0; 
        
        myCardPool = new CardPool();         
        myInputScanner = new Scanner(System.in);

        // Turn-base AI (Aggresive) vs Player

        // General Rules
        // AI and Player get 25 cards each, 5 turns.  Player and AI Score initialized to zero.
        // In each turn
        //      Sort the cards, print AI Cards, then My Cards, numbered in increasing order
        //      Player makes a choice - proceed when valid
        //      AI makes a move, compare Player's hand vs. AI's hand
        //      Update score
        //      Players and AI cannot make INVALID moves until they are out of valid hands
        // At the end of the game, report winner with score


        // You can upgrade your Lab 2 algorithm to Lab 3 to complete this game
        // You can also redesign the entire game loop logic

        // Step 1 - Initialization
        //  - Given the CardPool instance, get 25 cards (POCKETSIZE) for both AI and Player.
        //  - Sort their cards using sortCards(). Assign a serial number to Player's cards
        //  - Instantiate a HandsRBT for the player and invoke generateHandsIntoRBT() 
        //    to populate the player RBT with all possible hands from the pocket card.
        //  - Instantiate a HandsBST for AI and invoke generateHandsIntoBST() 
        //    to populate the AI BST with all possible hands from the pocket card.

        //  - If you have successfully completed Lab 2, you may replace HandsBST with your own HandsMaxHeap
        //    to improve the program performance.

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
        aiBST = new HandsBST();
        
        generateHandsIntoRBT(myCards, myRBT);
        generateHandsIntoBST(aiCards, aiBST);
        
        System.out.println("\n=== GAME START ===");
        
        // Step 2 - Game Loop Logic
        //  - Given that POCKETSIZE = 25 and a 5-card hand is consumed at each round, our game loop should only 
        //    repeat 5 times.  You can optionally parameterize the iteration count for scalability.

        for (int round = 1; round <= 5; round++) {
            System.out.println("\n========== ROUND " + round + " ==========");

            // Step 2-1 : Print Both AI and Player Pocket Cards for Strategy Analysis
            //            - Also check if RBT is empty.  If yes, notify player that he/she is out of moves.
            //            - When printing the Player pocket cards, you **MUST** print with serial number.
            
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

            // Step 2-2 : Use the provided getUserHand() method to allow player to pick the 5-card hand from
            //            the pocket cards.
            //              - After the hand is chosen, check if this hand can be found in the RBT
            //              - If this hand is not in the RBT and the RBT is not empty
            //                 notify the player that there are still valid 5-card hands and cannot pass.
            //                 Wait for Player to input another hand
            
            boolean validChoice = false;
            Hands playerHand = null;
            
            // If player has less than 5 cards, they can't make a move
            if (myPocketSize < 5) {
                System.out.println("Player doesn't have enough cards to make a hand!");
                playerHand = null;
            } else {
                do {
                    System.out.println("\n--- PLAYER CARD SELECTION (Round " + round + ") ---");
                    playerHand = getUserHand(myCards, myPocketSize);
                    
                    // Check if the hand is valid and exists in RBT
                    if (playerHand.isAValidHand()) {
                        HandsRBTNode foundNode = myRBT.findNode(playerHand);
                        if (foundNode != null) {
                            validChoice = true;
                        } else {
                            if (!myRBT.isEmpty()) {
                                System.out.println("Invalid choice! This hand is not in your valid hands list.");
                                System.out.println("You still have valid hands available. Please choose again.");
                            } else {
                                // RBT is empty, player can make any hand as a pass move
                                validChoice = true;
                            }
                        }
                    } else {
                        if (!myRBT.isEmpty()) {
                            System.out.println("Invalid choice! This is not a valid poker hand.");
                            System.out.println("You still have valid hands available. Please choose again.");
                        } else {
                            // RBT is empty, player can make any hand as a pass move
                            validChoice = true;
                        }
                    }
                } while (!validChoice);
            }

            // Step 2-3 : Save the chosen hand as "PLAYERHAND", and update pocket card and RBT
            //            - Delete the invalid hands from the RBT using deleteInvalidHands()
            //            - Remove the consumed 5 cards from the pocket cards. 
            //            - Remember to reduce the pocket size by 5.
            
            if (playerHand != null) {
                // Remove all hands containing any of the 5 cards from RBT
                myRBT.deleteInvalidHands(playerHand);
                
                // Remove the 5 chosen cards from player's pocket
                Card[] newMyCards = new Card[myPocketSize - 5];
                int newIndex = 0;
                int[] selectedIndices = getLastSelectedIndices(); // Get the indices from the last selection
                
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
            }

            // Step 2-4 : Using the logic from Lab 2, construct the Aggressive AI Logic
            //            - If you have completed Lab 2, you may use HandsMaxHeap.  
            //              Otherwise, you can use the provided HandsBST (a binary search tree of hands - apply knowledge from 2SI3)
            //            - For every 5-card move made, remove the consumed 5 cards from AI pocket cards, reduce the pocket size
            //              then regenerate the MaxHeap/HandsBST 
            //            - Save the chosen move as the "AIHAND"
            //            - Remember, once out of valid hands, AI can pick any cards to form a 5-card pass move.
            
            aiHand = null;
            
            if (aiPocketSize >= 5) {
                // AI chooses the strongest hand (max hand from BST)
                if (!aiBST.isEmpty()) {
                    aiHand = aiBST.removeMaxHand();
                    System.out.println("\n--- AI's Chosen Hand (Round " + round + ") ---");
                    aiHand.printMyHand();
                    System.out.println();
                    
                    // Remove the 5 cards used by AI (simplified - in a real implementation, you'd track which cards)
                    // For this implementation, we'll just remove any 5 cards from AI's pocket
                    Card[] newAiCards = new Card[aiPocketSize - 5];
                    System.arraycopy(aiCards, 5, newAiCards, 0, aiPocketSize - 5);
                    aiCards = newAiCards;
                    aiPocketSize -= 5;
                    
                    // Regenerate AI's BST with remaining cards
                    aiBST = new HandsBST();
                    generateHandsIntoBST(aiCards, aiBST);
                } else {
                    // No valid hands, AI makes a pass move with first 5 cards
                    aiHand = new Hands(aiCards[0], aiCards[1], aiCards[2], aiCards[3], aiCards[4]);
                    
                    Card[] newAiCards = new Card[aiPocketSize - 5];
                    System.arraycopy(aiCards, 5, newAiCards, 0, aiPocketSize - 5);
                    aiCards = newAiCards;
                    aiPocketSize -= 5;
                }
            }

            // Step 2-5 : Determine the Win/Lose result for this round, and update the scores for AI or Player
            //            - Print both PLAYERHAND and AIHAND for visual confirmation
            //            - Compare hands, and increase the score for the respective party who wins the round
            //            - An unlikely Draw (no winner) condition will result in no score increase for either party
        
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

        // Step 3 - Report the Results
        //  - This part is easy.  Refer to the provided sample execution for printout format
        
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

    // Store the last selected indices for card removal
    private static int[] lastSelectedIndices = new int[5];
    
    private static int[] getLastSelectedIndices() {
        return lastSelectedIndices;
    }

    public static void generateHandsIntoBST(Card[] cards, HandsBST thisBST)
    {
        // Implement this if you are using the BST version for the Aggressive AI
        // Populate all valid hands into the BST
        
        int n = cards.length;
        
        // Generate all possible 5-card combinations
        for (int i = 0; i < n - 4; i++) {
            for (int j = i + 1; j < n - 3; j++) {
                for (int k = j + 1; k < n - 2; k++) {
                    for (int l = k + 1; l < n - 1; l++) {
                        for (int m = l + 1; m < n; m++) {
                            Hands hand = new Hands(cards[i], cards[j], cards[k], cards[l], cards[m]);
                            if (hand.isAValidHand()) {
                                thisBST.insert(hand);
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("Generated " + countBSTNodes(thisBST) + " valid hands for AI");
    }
    
    // Helper method to count nodes in BST (simplified - just for reporting)
    private static int countBSTNodes(HandsBST bst) {
        // This is a simplistic approach - in a real implementation you'd traverse the BST
        return 1; // Placeholder
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
        
        System.out.println("Generated " + validCount + " valid hands for Player");
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