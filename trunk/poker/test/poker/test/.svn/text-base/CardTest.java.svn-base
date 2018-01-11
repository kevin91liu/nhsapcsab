/**
 * $Id$
 * @author Wilson Lee (kourge@gmail.com)
 */

package poker.test;

import poker.engine.Card;
import junit.framework.TestCase;

public class CardTest extends TestCase {
	Card x = new Card(Card.Suit.SPADES, Card.ACE);
	Card y = new Card(Card.Suit.SPADES, Card.ACE);
	
	public void testGetSuit() {
		assertEquals(x.getSuit(), Card.Suit.SPADES);
	}

	public void testGetNumber() {
		assertEquals(x.getNumber(), Card.ACE);
	}
	
	public void testDeflatability() throws Exception {
		Card z = new Card();
		assertEquals(x, z.inflate(x.deflate()));
	}
	
	public void testClone() {
		Card clone = x.clone();
		assertEquals(x, clone);
	}

	public void testEqualsObject() {
		assertEquals(x, y);
	}

	public void testCompareTo() {
		Card z = new Card(Card.Suit.HEARTS, Card.QUEEN);
		assertEquals(x.compareTo(y), 0);
		assertTrue(x.compareTo(z) > 0);
	}

	public void testToString() {
		assertEquals(x.toString(), "Ace of SPADES");
	}

}
