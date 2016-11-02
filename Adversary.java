
public class Adversary {
	private int numCards;
	private int numCoins;
	private double[] probCards;
	private int[] countActions;
	
	public Adversary() {
		this.numCards = 2;
		this.numCoins = 2;
		double[] init = {0.4, 0.4, 0.4, 0.4, 0.4};
		this.probCards = init;
		int[] inact = {1, 1, 1, 1, 1};
		this.countActions = inact;
	}
	public int numLives() {
		return this.numCards;
	}
	public int getCoins() {
		return this.numCoins;
	}
	public double[] getProbs() {
		return this.probCards;
	}
	public int[] numActions() {
		return this.countActions;
	}
	public void increment(int index) {
		this.countActions[index] = this.countActions[index] + 1;
		double totalActions = 0;
		for (int i = 0; i < 5; i++) {
			totalActions += this.countActions[i];
		}
		boolean overAct = false; // Is true if number of actions weights one role at Prob > 1
		for (int j = 0; j < 5; j++) {
			if (this.countActions[j] > totalActions/2) {
				overAct = true;
				totalActions -= this.countActions[j];
				this.probCards[j] = 1;
				// Fill in other probabilities
				for (int k = 0; k < 5; k++) {
					if (k != j) {
						this.probCards[k] = (double)this.countActions[k]/totalActions;
					}
				}
			}
		}
		if (!overAct) {
			for (int m = 0; m < 5; m++) {
				this.probCards[m] = (double)(2*this.countActions[m])/totalActions;			
			}
		}	
	}
	public boolean processDeath(int index) { // Optimize later for better guess on opponent's role
		this.numCards--;
		if (this.numCards == 0) {
			return true;
		}
		for (int a = 0; a < 5; a++) {
			this.probCards[a] = this.probCards[a]/2;
		}
		double adj = this.probCards[index];
		this.probCards[index] = 0;
		for (int b = 0; b < 5; b++) {
			this.probCards[b] += adj/5;
		}
		return false;
	}
	public void income() {
		this.numCoins++;
	}
	public void foreignAid(boolean blocked) {
		if (!blocked) { this.numCoins += 2; }
	}
	public void Duke(boolean called) {
		if (!called) { this.numCoins += 3; }
	}
	public int Assassin(boolean blocked, boolean called, int target) {
		this.numCoins -= 3;
		if (!blocked && !called) {
			return target;
		}
		return -100; // Placeholder. 
	}
	public int Captain(boolean blocked, boolean called, int target) {
		if (!blocked && !called) {
			this.numCoins++;
		}
		return target;
	}
	public int Coup(int target) {
		this.numCoins -= 7;
		return target;
	}
	// Deal with Assassin, Ambassador, Contessa, in game. 
}
