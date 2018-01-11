package poker.test;

import junit.framework.TestCase;
import poker.engine.Money;

public class MoneyTest extends TestCase {
	Money m1 = new Money(40000, Money.Currency.DOLLARS);
	
	public void testGetAmount() {
		assertEquals(m1.getAmount(), 40000.0);
	}

	public void testGetType() {
		assertEquals(m1.getType(), Money.Currency.DOLLARS);
	}

	public void testAdd() {
		assertEquals(m1.add(new Money(50000, Money.Currency.DOLLARS)),
					 new Money(90000, Money.Currency.DOLLARS));
	}

	public void testSubtract() {
		assertEquals(m1.subtract(new Money(39999, Money.Currency.DOLLARS)),
					 new Money(1, Money.Currency.DOLLARS));
	}

	public void testDeflatability() {
		try {
			Money m = new Money();
			assertEquals(m.inflate(m1.deflate()), m1);
		} catch (Exception e) {}
	}

	public void testToString() {
		assertEquals(m1.toString(), "40000.000000 DOLLARS");
	}

}
