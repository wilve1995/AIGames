// Still need to decide how to call people. 
public class Player {
	public static enum Roles {
		Duke, 
		Assassin,
		Ambassador,
		Captain,
		Contessa
	}
	// How do we see our adversary's probabilities? Maybe we can just have 
	// it as a function argument to the getTarget and Block/Call fxns. 
	private Roles[] cards;
	private int numCoins;
	private double[] actions;
	private int[] countAct;
	// Indices are: (0, Duke), (1, Assassin), (2, Ambassador), (3, Captain), (4, Income), (5, Aid), (6, Coup)
	public Player(Roles role1, Roles role2) {
		Roles[] cards = {role1, role2}; // set second to null, if there's a death. 
		this.cards = cards;
		this.numCoins = 2;
		this.actions = assignProbs(role1, role2, this.numCoins);
		int[] fives = {0, 0, 0, 0, 0};
		this.countAct = fives;
	}
	public Roles[] getCards() {
		return this.cards;
	}
	public int getCoins() {
		return this.numCoins;
	}
	public double[] actions() {
		return this.actions;
	}
	public void income() {
		this.numCoins++;
	}
	public void foreignAid(boolean blocked) {
		if (!blocked) {
			this.numCoins += 2;
		}
	}
	public void Duke() {
		//System.out.println("I gained 3 coins!");
		this.numCoins += 3;
	}
	public void Assassin() {
		this.numCoins -= 3;

	}
	public void Captain() {
		this.numCoins+=2;
	}
	public void Robbed() {
		this.numCoins -= 2;
	}
	
	public int Coup(int target) {
		this.numCoins -= 7;
		return target;
	}
	public boolean hasDuke() {
		return this.cards[0] == Roles.Duke || this.cards[1] == Roles.Duke;
	}
	public boolean hasAssassin() {
		return this.cards[0] == Roles.Assassin || this.cards[1] == Roles.Assassin;
	}
	public boolean hasAmbassador() {
		return this.cards[0] == Roles.Ambassador || this.cards[1] == Roles.Ambassador;
	}
	public boolean hasCaptain() {
		return this.cards[0] == Roles.Captain || this.cards[1] == Roles.Captain;
	}
	public boolean hasContessa() {
		return this.cards[0] == Roles.Contessa || this.cards[1] == Roles.Contessa;
	}
	
	// Right now, maybe just do 15 cases? 
	// Always initialize roles in order of: Duke, Assassin, Ambassador, Captain, Contessa.
	public double[] assignProbs(Roles role1, Roles role2, int coins) { // Have a boolean for No Dukes
		double[] probs = new double[7];
		if (coins >= 7) { // At first, just Coup if you can. 
			double[] mustCoup = {0, 0, 0, 0, 0, 0, 1};
			return mustCoup;
		}
		double[] rand = {0, 1, 2, 3};
		if (role1 == Roles.Contessa && role2 == Roles.Contessa) { // Choose 1 card to pretend
			int card = (int)(4*Math.random());
			for (int i = 0; i < 7; i++) {
				if (i == card) {
					probs[i] = (double)2/3;
					//if (NoDukes()) {
					//    probs[5] = (double)1/3;
					//} else {
					probs[4] = (double)1/3;
				}
			}
		}
		else if (this.hasContessa()) { // Choose 2 cards to pretend
			int first = (int)(4*Math.random());
			probs[first] = (double)2/5;
			rand[first] = rand[3];
			int second = (int)(3*Math.random());
			probs[second] = (double)2/5;
			probs[4] = (double)1/5;
		}
		else { // Choose 1 truthful, 1 lying (but maybe truthful) action here.
			int first = (int)(4*Math.random());
			probs[first] = (double)2/5;
			rand[first] = rand[3];
			int second = (int)(3*Math.random());
			probs[second] = (double)2/5;
			probs[4] = (double)1/5;
		}
		return probs;
	}
	public String chooseAction() {
		double rand = Math.random();
		int index = -1;
		while (rand > 0) {
			index++;
			rand -= this.actions[index];
		}
		this.countAct[index]++;
		switch(index) {
		case 0: return "Duke";
		case 1: return "Assassin";
		case 2: return "Ambassador";
		case 3: return "Captain";
		case 4: return "Income";
		case 5: return "ForeignAid";
		case 6: return "Coup";
		}
		return "";
	}
	/** Okay, so if the action is Assassin, then we want to choose the 
	 * opponent with the lowest probability of Contessa. 
	 * If it's Captain, then we want to choose the opponent with the lowest 
	 * combined probability of Ambassador + Captain.
	 * And if the action is coup, choose anyone with 2 cards and fewer than
	 * 7 coins, otherwise pick 1 card with max coins. 
	*/
	public int chooseTarget(Adversary[] opponents, String action) {
		int target = 0;
		double minProb = 1;
		if (action.compareTo("Assassin") == 0) {
			for (int i = 0; i < opponents.length; i++) {
				Adversary op = opponents[i];
				if (op.getProbs()[4] < minProb) {
					target = i;
				}
			}
			return target;
		}
		else if (action.compareTo("Captain") == 0) {
			minProb = 2;
			for (int i = 0; i < opponents.length; i++) {
				Adversary op = opponents[i];
				if (op.getProbs()[2] + op.getProbs()[3] < minProb) {
					target = i;
				}
			}
			return target;
		}
		else if (action.compareTo("Coup") == 0) {
			int maxCoins = 0;
			boolean doubleLives = false;
			for (int i = 0; i < opponents.length; i++) {
				Adversary op = opponents[i];
				if (op.numLives() == 2 && op.getCoins() > maxCoins) {
					doubleLives = true;
					target = i;
				}
				else if (!doubleLives && op.numLives() == 2) {
					doubleLives = true;
					maxCoins = op.getCoins();
					target = i;
				}
				else if (!doubleLives && op.getCoins() > maxCoins) {
					maxCoins = op.getCoins();
					target = i;
				}
			}
			return target;
		}
		else {
			return -1; // No Target for this action. 
		}
	}
	// This one, we need probabilities of 1, plus cutoffs by action. 
	public boolean call(Adversary op, String action) {
		switch(action)
		{
		case "Duke": {
			if (op.getProbs()[0] < 0.3) { return true; }
			break;
		}
		case "Assassin":  {
			if (op.getProbs()[1] < 0.2 || cards[1] == null && !blockKilling()) { return true; }
		}
		case "Ambassador": {
			if (op.getProbs()[2] < 0.35) { return true; }
		}
		case "Captain":  {
			if (op.getProbs()[3] < 0.3) { return true; }
		}
		case "Contessa": {
			if (op.getProbs()[4] < 0.25) { return true; }
		}
		}
		return false;
	}
	
	public void loseLife() {
		if (this.cards[1] == null) {
			System.out.println();
			System.exit(1);
		}
		else {
			this.cards[1] = null;
		}
	}
	
	public boolean blockAid() {
		if (this.actions[0] > 0) {
			double d = Math.random();
			if (d > 0.2) {
				return true;
			}
		}
		return false;
	}

	public boolean blockStealing() {
		double d = Math.random();
		if (this.actions[2] > 0 || this.actions[3] > 0) {
			if (d > 0.35) {
				return true;
			}
		}
		else if (d > 0.5) {
			return true;
		}
		return false;
	}
	public boolean blockKilling() {
		double d = Math.random();
		if (this.hasContessa()) {
			if (this.cards[1] == null) {
				return true;
			}
			else {
				if (d < 0.9) {
					return true;
				}
			}
		}
		
		if (d > 0.5) {
			return true;
		}
		return false;
	}
}
