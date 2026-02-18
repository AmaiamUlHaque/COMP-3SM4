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

        // Step 2 - Game Loop Logic
        //  - Given that POCKETSIZE = 25 and a 5-card hand is consumed at each round, our game loop should only 
        //    repeat 5 times.  You can optionally parameterize the iteration count for scalability.

            // Step 2-1 : Print Both AI and Player Pocket Cards for Strategy Analysis
            //            - Also check if RBT is empty.  If yes, notify player that he/she is out of moves.
            //            - When printing the Player pocket cards, you **MUST** print with serial number.

            // Step 2-2 : Use the provided getUserHand() method to allow player to pick the 5-card hand from
            //            the pocket cards.
            //              - After the hand is chosen, check if this hand can be found in the RBT
            //              -  If this hand is not in the RBT and the RBT is not empty
            //                 notify the player that there are still valid 5-card hands and cannot pass.
            //                 Wait for Player to input another hand

            // Step 2-3 : Save the chosen hand as "PLAYERHAND", and update pocket card and RBT
            //            - Delete the invalid hands from the RBT using deleteInvalidHands()
            //            - Remove the consumed 5 cards from the pocket cards. 
            //            - Remember to reduce the pocket size by 5.

            // Step 2-4 : Using the logic from Lab 2, construct the Aggressive AI Logic
            //            - If you have completed Lab 2, you may use HandsMaxHeap.  
            //              Otherwise, you can use the provided HandsBST (a binary search tree of hands - apply knowledge from 2SI3)
            //            - For every 5-card move made, remove the consumed 5 cards from AI pocket cards, reduce the pocket size
            //              then regenerate the MaxHeap/HandsBST 
            //            - Save the chosen move as the "AIHAND"
            //            - Remember, once out of valid hands, AI can pick any cards to form a 5-card pass move.

            // Step 2-5 : Determine the Win/Lose result for this round, and update the scores for AI or Player
            //            - Print both PLAYERHAND and AIHAND for visual confirmation
            //            - Compare hands, and increase the score for the respective party who wins the round
            //            - An unlikely Draw (no winner) condition will result in no score increase for either party


        // Step 3 - Report the Results
        //  - This part is easy.  Refer to the provided sample execution for printout format
        
        myInputScanner.close();
    }

    public static void generateHandsIntoBST(Card[] cards, HandsBST thisBST)
    {
        // Implement this if you are using the BST version for the Aggressive AI
        //Populate all valid hands into the BST
    
    }

    public static void generateHandsIntoRBT(Card[] cards, HandsRBT thisRBT)
    {
        // Populate all valid hands into the RBT
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
    public static Hands getUserHand(Card[] myCards)
    {
        int size = myCards.length;
        int[] mySelection = new int[5];  
        myInputScanner = new Scanner(System.in);

        System.out.println();
        for(int i = 0; i < 5; i++)
        {            
            System.out.printf("Card Choice #%d: ", i + 1);
            mySelection[i] = myInputScanner.nextInt() - 1;
            if(mySelection[i] > size) mySelection[i] = size - 1;
            if(mySelection[i] < 0) mySelection[i] = 0;            
        }
        
        Hands newHand = new Hands(  myCards[mySelection[0]], 
                                    myCards[mySelection[1]], 
                                    myCards[mySelection[2]], 
                                    myCards[mySelection[3]], 
                                    myCards[mySelection[4]]);

        return newHand;
    }

}
