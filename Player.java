// Still need to decide how to call people. 
public class Player {
	public static enum Roles {
		Duke, 
		Assassin,
		Ambassador,
		Captain,
		Contessa
	}
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
	public int chooseAction() {
		double rand = Math.random();
		int index = -1;
		while (rand > 0) {
			index++;
			rand -= this.actions[index];
		}
		this.countAct[index]++;
		return index;
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
