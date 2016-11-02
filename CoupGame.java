import java.util.Scanner;

public class CoupGame {
	// Maybe use an enum for actions?
	
     public static void main(String[] args) {
    	 // Initialize the Player and views of their opponents
    	 // Run the game, and get input as it comes
    	 
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
    		 if (turn >= 0) { // Adversary turn. Must input information
    			 boolean elimination = false;
    			 // These are the 5 inputs
    			 String move = s.nextLine();
    			 int target = s.nextInt();
    			 String counter = s.nextLine();
    			 
    			 //Set these to -1 if there's no death this round. 
    			 int lostPlayer = s.nextInt(); // Index of player losing a life
    			 int lostLife = s.nextInt(); // Index of type of card.
    			 
    			 boolean blocked = false;
    			 if (counter.compareTo("Blocked") == 0) {
    				 blocked = true;
    			 }
    			 boolean called = false;
    			 if (counter.compareTo("Called") == 0) {
    				 called = true;
    			 }
    			 switch(move) {
    			 case "Income": {
    			     opponents[turn].income();
    				 break;
    			 }
    			 case "Foreign Aid": {
    				 opponents[turn].foreignAid(blocked);
    				 break;
    			 }
    			 case "Coup": {
    				 elimination = opponents[target].processDeath(lostLife);
    				 break;
    			 }
    			 case "Duke": {
    				 opponents[turn].Duke(called);
    				 if (called) {
    					 elimination = opponents[lostPlayer].processDeath(lostLife);   					 
    				 }
    				 break;
    			 }
    			 case "Assassin": {
    				 break;
    			 }
    			 case "Ambassador": {
    				 break;
    			 }
    			 case "Captain": {
    				 break;
    			 }
    			 }
    		 }
    		 //Cycle through Players and adversary. 
    		 //Collect input and use that to determine what happens. 
    	 }
     }
}
