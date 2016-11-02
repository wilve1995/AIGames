import static org.junit.Assert.*;

import org.junit.Test;
import java.util.Arrays;

public class PlayerTest {
    // Tsujita Ramen in Los Angeles area. 
	Adversary op1 = new Adversary();
	Player p1 = new Player(Player.Roles.Duke, Player.Roles.Captain);

	@Test
	
	public void testAdversary() {
		assertEquals(op1.numLives(), 2);
		assertEquals(op1.getCoins(), 2);
		//Start checking Probabilities
		op1.increment(0);
		assertEquals(op1.numActions()[0], 2);
		assertEquals(op1.getProbs()[0], (double)2/3, 0.01);
		op1.increment(0);
		op1.increment(0);
		op1.increment(0);
		op1.increment(1);
		op1.increment(0); // Making sure overcount works
		assertEquals(op1.getProbs()[0], 1, 0.01);
		
		assertFalse(op1.processDeath(0)); // Try losing a card
		assertEquals(op1.numLives(), 1);
		double[] res = op1.getProbs();
		System.out.println(Arrays.toString(res));
		assertTrue(op1.processDeath(4));
		
	}
	@Test
	
	public void testPlayer() {
		assertEquals(p1.getCards().length, 2);
		assertEquals(p1.getCoins(), 2);
		assertTrue(p1.hasDuke());
		assertFalse(p1.hasAssassin());
	}

}
