import java.util.Scanner;

public class CoupGame {
	// Maybe use an enum for actions?
	
     public static void main(String[] args) {
    	 // Initialize the Player and views of their opponents
    	 // Run the game, and get input as it comes
    	 playGame(3);
     }
     
     public static void playGame(int numPlayers) {   	 
    	 Player me = new Player(Player.Roles.Duke, Player.Roles.Captain); // Change these manually for now
    	 Adversary[] opponents = new Adversary[numPlayers];
    	 int lastLiving = opponents.length-1;
    	 int turn = 0; // set different for each game. 
    	 for (int i = 0; i < numPlayers; i++) {
    		 Adversary a = new Adversary();
    		 opponents[i] = a;
    	 }
    	 Scanner s = new Scanner(System.in);
    	 // Input format: Name of Action, index of target player (or -1 for attack on Player)
    	 // If attacked, the next line will hold the response
    	 while (me != null && opponents[0] != null) { // Game loop.
			 boolean elimination = false; // True iff someone dies this round.
			 boolean success = true; // True if there's a challenge that fails
    		 if (turn >= 0) { // Adversary turn. Must input information
    			 // These are the 5 inputs
    			 String useless = s.nextLine(); // Keeps program from skipping first line.
    			 useless += "useless";
    			 String move = s.nextLine();
    			 System.out.println("MOVE: " + move);
    			 String counter = s.nextLine(); // In case of blocked and called, use called. 
    			 System.out.println("Expecting indices now");
    			 
    			 int target = s.nextInt(); // -1 for targeting player

    			 //Set these to -1 if there's no death this round. 
    			 int lostPlayer = s.nextInt(); // Index of player losing a life
    			 int lostLife = s.nextInt(); // Index of type of card.
    			 
    			 System.out.println("Player " + turn + " used " + move + " (on player) " + target + " and player " + lostPlayer + " died and lost role " + lostLife);
    			 
    			 boolean blocked = false;
    			 if (counter.compareTo("Blocked") == 0) {
    				 blocked = true;
    			 }
    			 boolean called = false;
    			 if (counter.compareTo("Called") == 0) {
    				 called = true;
    			 }
    			 switch(move) {
    			 // Give the player 1 coin, regardless. 
    			 case "Income": {
    			     opponents[turn].income();
    			     opponents[turn].increment(4); // Inaction makes Contessa more likely.
    			     System.out.println("Adversary " + turn + " has " + opponents[turn].getCoins() + " coins");
    				 break;
    			 }
    			 // Give the player 2 coins, if not blocked successfully. 
    			 case "Foreign Aid": {
    				 if (called) {
    					 elimination = opponents[lostPlayer].processDeath(lostLife);
    				 }
    				 opponents[turn].foreignAid(blocked);
    			     System.out.println("Adversary " + turn + " has " + opponents[turn].getCoins() + " coins");
    				 break;
    			 }
    			 // Kill the targeted player. 
    			 case "Coup": {
    				 if (target >= 0) {
    					 elimination = opponents[target].processDeath(lostLife);
    					 System.out.println("Adversary " + target + " was coup'ed");
    				 } 
    				 else {
    					 System.out.println("I just got coup'ed!");
    					 me.loseLife();
    				 }
    				 break;
    			 }
    			 // Give the player 3 coins, if not called successfully. 
    			 case "Duke": {
    				 if (called) {
    					 elimination = opponents[lostPlayer].processDeath(lostLife);
    					 System.out.println("P" + turn + " was called and P" + lostPlayer + " lost a life");
    					 if (lostPlayer == turn) { success = false; }
    				 }
    				 if (success) {
    					 opponents[turn].increment(0);
    					 opponents[turn].Duke();
        			     System.out.println("Adversary " + turn + " has " + opponents[turn].getCoins() + " coins");
    				 }
    				 break;
    			 }
    			 // Kill the targeted player, if not blocked or called successfully.
    			 // Process an extra death if a called block or call happens.
    			 case "Assassin": {
    				 if (called) {
    					 elimination = opponents[lostPlayer].processDeath(lostLife);
    					 System.out.println("P" + turn + " was called and P" + lostPlayer + " lost a life");

    					 if (lostPlayer == turn) { success = false; }
    				 }
    				 else if (blocked) {
    					 opponents[turn].Assassin(blocked, called, target); // Still loses the coins. 
    					 opponents[turn].increment(1);
    					 System.out.println("Assassination blocked");
    				 }
    				 else if (success) {
    					 if (target >= 0) {
    						 elimination = opponents[target].processDeath(lostLife);
    						 System.out.println("Adversary " + target + " was assassinated");
    					 }
    					 else if (target == -1) {
    						 me.loseLife();
    						 System.out.println("I just got assassinated!");
    					 }
    					 opponents[turn].Assassin(blocked, called, target);
    					 opponents[turn].increment(1);
    				 }
    				 break;
    			 }
    			 // Reconfigure probabilities for Adversary. 
    			 case "Ambassador": {
    				 if (called) {
    					 elimination = opponents[lostPlayer].processDeath(lostLife);
    					 if (lostPlayer == turn) { success = false; }
    					 System.out.println("P" + turn + " was called and P" + lostPlayer + " lost a life");
    				 }
    				 if (success) {
    					 opponents[turn].increment(2);
    				 }
    				 break;
    			 }
    			 // Move the 2 coins around if not blocked or called successfully.
    			 case "Captain": {
    				 if (called) {
    					 elimination = opponents[lostPlayer].processDeath(lostLife);
    					 if (lostPlayer == turn) { success = false; }
    					 System.out.println("P" + turn + " was called and P" + lostPlayer + " lost a life");
    				 }
    				 if (success) {
    					 opponents[turn].increment(3);
    					 if (!blocked) {
    						 opponents[turn].Captain(blocked, called, target);
    						 opponents[target].Robbed();
    						 System.out.println("Adversary " + turn + " stole from Adversary " + target);
    					 }
    				 }
    				 break;
    			 }
    			 }
    			 if (elimination) {
    				// Move the dead guy to the end, nullify. 
    				 // Reduce the number of players in the game by 1.
    				 opponents[lostLife] = opponents[lastLiving];
    				 opponents[lastLiving] = null;
    				 lastLiving--;  				 
    			 }
    		 }
    		 if (turn == -1) {
    			 String move = me.chooseAction();
    			 /** After my action is chosen, what other info do we need?
    			 // We still need the blocked/called, and uh... depending on
    			 // What action was called, we need a target (determined)
    			 // by the AI. We also need a blocked/called response from 
    			 // the player, and then an AI response to a block. 
    			 // Whoever loses the life also needs to be input. 
    			 // And if the adversary loses a life, then we need to know
    			 // what it was that was lost. Well... we actually... we won't
    			 // know for sure who lost a life until we determine the AI's
    			 // call / don't call response, so let's um... get those at the
    			 // end of this game method */ 
    			 String counter = s.nextLine();
    			 boolean blocked = false;
    			 if (counter.compareTo("blocked") == 0) { blocked = true; }
    			 boolean called = false;
    			 if (counter.compareTo("called") == 0) { called = true; }
    			 int target = me.chooseTarget(opponents, move);
    			 boolean calling = false;
    			 if (target >= 0) {
    				 calling = me.call(opponents[target], move);
    			 }
    			 if (calling || called) {
    				 int lostPlayer = s.nextInt(); // Index of player losing a life
        			 if (lostPlayer == -1) {
        				 success = false;
        			 }
    				 int lostLife = s.nextInt();
        			 if (opponents[lostPlayer].numLives() == 1) {
        				 elimination = true;
        			 }
        			 if (elimination) {
        				// Move the dead guy to the end, nullify. 
        				 // Reduce the number of players in the game by 1.
        				 opponents[lostLife] = opponents[lastLiving];
        				 opponents[lastLiving] = null;
        				 lastLiving--;  				 
        			 }
    			 }
    			 // Now to split up based on actions. Once we've done this, 
    			 // we can start testing the program as a whole!
    			 switch(move) {
    			 case "Income": {
    				 me.income();
    				 break;
    			 }
    			 case "Foreign Aid": { 
    				 if (calling && success) {
    					 me.foreignAid(success);
    				 }
    				 else {
    					 me.foreignAid(blocked);
    				 }
    				 break;
    			 }
    			 case "Duke": {
    				 if (!called || success) {
    					 me.Duke();
    				 }
    				 break;
    			 }
    			 case "Assassin": {
    				 me.Assassin();
    				 if (success && !blocked) {
    					 int assnated = s.nextInt(); // Grab this only if we have an assassination death.
    					 opponents[target].processDeath(assnated);
    				 }
    				 break;
    			 }
    			 case "Ambassador": {
    				 String card1 = s.nextLine();
    				 String card2 = s.nextLine();
    				 me.Ambassador(card1, card2);
    				 break;
    			 }
    			 case "Captain": {
    				 if (success && !blocked) {
    					 me.Captain();
    					 opponents[target].Robbed();
    				 }
    				 break;
    			 }
    			 case "Coup": {
    				 me.Coup(target);
    				 int couped = s.nextInt();
    				 elimination = opponents[target].processDeath(couped);
    				 break;
    			 }
    			 }

    		 }

    		 turn++;
    		 printGame(me, opponents);
    		 if (turn > lastLiving) {
    			 turn = -1; // reset to the beginning: -1 for Player
    		 }
    		 //Cycle through Players and adversary. 
    		 //Collect input and use that to determine what happens. 
    	 }
     }
     public static void printGame(Player ai, Adversary[] enemies) {
    	 System.out.println("CoupAI has roles " + ai.getCards()[0] + " and " +
    			 ai.getCards()[1] + " and " + ai.getCoins() + " coins.");
    	 for (int i = 0; i < enemies.length; i++) {
    		 Adversary p = enemies[i];
    		 if (p != null) {
    			 System.out.println("Adversary " + i + " has " + p.numLives() +
    					 " and " + p.getCoins() + " coins.");
    		 }
    	 }
     }
}
