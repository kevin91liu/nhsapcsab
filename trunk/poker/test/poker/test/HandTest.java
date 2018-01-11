package poker.test;

import poker.engine.*;
import junit.framework.TestCase;

public class HandTest extends TestCase {

	Hand h1 = new Hand(new Card[] {
		new Card(Card.Suit.CLUBS, Card.SIX),
		new Card(Card.Suit.DIAMONDS, Card.NINE),
		new Card(Card.Suit.HEARTS, Card.NINE),
		new Card(Card.Suit.SPADES, Card.JACK),
		new Card(Card.Suit.HEARTS, Card.ACE),
	});
	
	Hand h2 = new Hand(new Card[] {
		new Card(Card.Suit.SPADES, Card.NINE),
		new Card(Card.Suit.DIAMONDS, Card.FOUR),
	});
	
	Hand h3 = new Hand(new Card[] {
		new Card(Card.Suit.DIAMONDS, Card.NINE),
		new Card(Card.Suit.HEARTS, Card.NINE),
		new Card(Card.Suit.SPADES, Card.NINE),
		new Card(Card.Suit.SPADES, Card.JACK),
		new Card(Card.Suit.HEARTS, Card.ACE),
	});
	
	public void testClear() {
		Hand hand = new Hand(new Card[] {new Card(Card.Suit.HEARTS, Card.ACE)});
		Hand emptyHand = new Hand(new Card[] {});
		hand.clear();
		assertEquals(hand, emptyHand);
		assertEquals(hand.size(), 0);
		assertEquals(emptyHand.size(), 0);
	}

	public void testsize() {
		assertEquals(h1.size(), 5);
		assertEquals(h2.size(), 2);
		assertEquals((new Hand(5)).size(), 0);
	}

	public void testAdd() {
		Hand hand = new Hand(2);
		Card card = new Card(Card.Suit.HEARTS, Card.ACE);
		Card card2 = new Card(Card.Suit.HEARTS, Card.QUEEN);
		assertEquals(hand.size(), 0);
		assertTrue(hand.add(card));
		assertFalse(hand.add(card));
		assertTrue(hand.add(card2));
		assertFalse(hand.add(card2));
		assertFalse(hand.add(new Card(Card.Suit.SPADES, Card.ACE)));
		assertEquals(hand.size(), 2);
	}

	// testAddLong is almost the same as testAdd. 
	public void testAddLong() {
		Hand hand = new Hand(2);
		Card card = new Card(Card.Suit.HEARTS, Card.ACE);
		Card card2 = new Card(Card.Suit.HEARTS, Card.QUEEN);
		assertEquals(hand.size(), 0);
		assertTrue(hand.add(card.getValue()));
		assertFalse(hand.add(card.getValue()));
		assertTrue(hand.add(card2.getValue()));
		assertFalse(hand.add(card2.getValue()));
		assertFalse(hand.add((new Card(Card.Suit.SPADES, Card.ACE)).getValue()));
		assertEquals(hand.size(), 2);
	}

	public void testRemove() {
		Hand hand = new Hand(new Card[] {
			new Card(Card.Suit.SPADES, Card.NINE),
			new Card(Card.Suit.DIAMONDS, Card.FOUR),
		});
		assertTrue(hand.remove(new Card(Card.Suit.SPADES, Card.NINE)));
		assertEquals(hand.size(), 1);
		assertFalse(hand.remove(new Card(Card.Suit.HEARTS, Card.SEVEN)));
		assertTrue(hand.remove(new Card(Card.Suit.DIAMONDS, Card.FOUR)));
		assertEquals(hand.size(), 0);
	}

	public void testMerge() {
//		fail("Not yet implemented");
	}

	public void testGetHighestHand() {
		assertEquals(Hand.getHighestHand(h1, h2), h3);
	}

	public void testGetBitCards() {
//		fail("Not yet implemented");
	}

	public void testGetCards() {
		Card[] shouldHave = new Card[] {
			new Card(Card.Suit.CLUBS, Card.SIX),
			new Card(Card.Suit.DIAMONDS, Card.NINE),
			new Card(Card.Suit.HEARTS, Card.NINE),
			new Card(Card.Suit.SPADES, Card.JACK),
			new Card(Card.Suit.HEARTS, Card.ACE),
		};
		Card[] cards = h1.getCards();
		for (Card card : shouldHave)
			assertTrue(new Object() {
				public boolean has(Card x, Card[] cards) {
					for (Card card : cards)
						if (card.equals(x))
							return true;
					return false;
				}
			}.has(card, cards));
	}

	public void testEqualsObject() {
		assertEquals(h2, new Hand(new Card[] {
			new Card(Card.Suit.SPADES, Card.NINE),
			new Card(Card.Suit.DIAMONDS, Card.FOUR),
		}));
	}

	public void testClone() {
		Hand clone = h2.clone();
		assertEquals(h2, clone);
	}

	public void testCompareTo() {
		assertTrue(h1.compareTo(h2) > 0);
		assertTrue(h1.compareTo(h1) == 0);
		assertTrue(h2.compareTo(h2) == 0);
	}
	
	public void testDeflatability() {
		try {
			Hand z = new Hand(1);
			assertEquals(h1, z.inflate(h1.deflate()));
		} catch (Exception e) {}
	}

	public void testHasFlush() {
//		fail("Not yet implemented");
	}

	public void testHasStraight() {
//		fail("Not yet implemented");
	}

	public void testQuadMask() {
//		fail("Not yet implemented");
	}

	public void testTripleMask() {
//		fail("Not yet implemented");
	}

	public void testCountPairs() {
//		fail("Not yet implemented");
	}

	public void testPairMask() {
//		fail("Not yet implemented");
	}

	public void testHasFullHouse() {
//		fail("Not yet implemented");
	}

	public void testHasStraightFlush() {
//		fail("Not yet implemented");
	}

	public void testCountBits() {
//		fail("Not yet implemented");
	}

	public void testToString() {
		assertEquals(h1.toString(), "Six of CLUBS, Nine of DIAMONDS, " +
				"Nine of HEARTS, Jack of SPADES, Ace of HEARTS");
		assertEquals(h2.toString(), "Four of DIAMONDS, Nine of SPADES");
	}
        
        /*
         *  This Section is for testing the CompareTo in depth - Shawn
         */
        //adjust this value to test a bigger sample. Warning: Heavy CPU usage
        private static final int repeats = 1;
                
        
        public void testPairsOverPairs()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 14; i++)
            {
                long[] smallPairs = g.generatePairs(i);
                for(long l : smallPairs)
                {
                    for (int z = 0; z < repeats; z++)
                    {
                        long smallPairWithDebris = g.generateDebris(l, i);                        
                        for (int j = i + 1; j < 15; j++)
                        {
                            long[] pairs = g.generatePairs(j);
                            for (long p : pairs)
                            {
                                for (int y = 0; y < repeats; y++)
                                {
                                    long bigPairWithDebris = g.generateDebris(p, j);
                                    Hand big = createHand(bigPairWithDebris);
                                    Hand small = createHand(smallPairWithDebris);
                                    assertTrue(big.toString() + "\nnot better than:\n" 
                                        + small.toString(),big.compareTo(small) > 0);                                    
                                }
                            }
                        }
                    }                                       
                }
            }
        }
        
        public void testTriplesOverPairs()
        {                   
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 15; i++)
            {
                long[] triples = g.generateTriples(i);
                for(long l : triples)
                {
                    for (int z = 0; z < repeats; z++)
                    {
                        long tripleWithDebris = g.generateDebris(l, i);                        
                        for (int j = 2; j < 15; j++)
                        {
                            long[] pairs = g.generatePairs(j);
                            for (long p : pairs)
                            {
                                for (int y = 0; y < repeats; y++)
                                {
                                    long pairWithDebris = g.generateDebris(p, j);
                                    Hand big = createHand(tripleWithDebris);
                                    Hand small = createHand(pairWithDebris);
                                    assertTrue(big.toString() + "\nnot better than:\n" 
                                        + small.toString(),big.compareTo(small) > 0);                                    
                                }
                            }
                        }
                    }                                       
                }
            }
        }
        
        public void testTriplesOverTriples()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 14; i++)
            {
                long[] smallTriples = g.generateTriples(i);
                for(long l : smallTriples)
                {
                    for (int z = 0; z < repeats; z++)
                    {
                        long smallTripleWithDebris = g.generateDebris(l, i);                        
                        for (int j = i + 1; j < 15; j++)
                        {
                            long[] bigTriples = g.generateTriples(j);
                            for (long p : bigTriples)
                            {
                                for (int y = 0; y < repeats; y++)
                                {
                                    long bigTripleWithDebris = g.generateDebris(p, j);
                                    Hand big = createHand(bigTripleWithDebris);
                                    Hand small = createHand(smallTripleWithDebris);
                                    assertTrue(big.toString() + "\nnot better than:\n" 
                                        + small.toString(),big.compareTo(small) > 0);                                    
                                }
                            }
                        }
                    }                                       
                }
            }
        }
        
        public void testFullHouseOverPairs()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 15; i++)
            {
                for (int k = 2; k < 15; k++)
                {
                    if (k == i) continue;
                    
                    long[] fs = g.generateFullHouse(i, k);
                    
                    for (long fh : fs)
                    {                                            
                        for (int j = 2; j < 15; j++)
                        {
                            long[] p = g.generatePairs(j);                                               
                            for (long pr : p)
                            {
                                for (int y = 0; y < repeats; y++)
                                {                                    
                                    Hand small = createHand(g.generateDebris(pr, j));
                                    Hand big = createHand(fh);
                                    assertTrue(big.toString() + "\nnot better than:\n" 
                                        + small.toString(),big.compareTo(small) > 0);                                    
                                }
                            }
                        }
                    }                                       
                }
            }
        }
        
        public void testFullHouseOverTriples()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 15; i++)
            {
                for (int k = 2; k < 15; k++)
                {
                    if (k == i) continue;
                    
                    long[] fs = g.generateFullHouse(i, k);
                    
                    for (long fh : fs)
                    {                                            
                        for (int j = 2; j < 15; j++)
                        {
                            long[] t = g.generateTriples(j);                                               
                            for (long tr : t)
                            {
                                for (int y = 0; y < repeats; y++)
                                {
                                    long triple = g.generateDebris(tr, j);
                                    Hand small = createHand(triple);
                                    Hand big = createHand(fh);
                                    assertTrue(big.toString() + "\nnot better than:\n" 
                                        + small.toString(),big.compareTo(small) > 0);                                    
                                }
                            }
                        }
                    }                                       
                }
            }
        }
        
        // this one has 7 million comparisons, so...
        public void testFullHouseOverFullHouse()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 14; i++)
            {
                for (int k = 2; k < 15; k++)
                {
                    if (k == i) continue;
                    
                    long[] sfs = g.generateFullHouse(i, k); 
                    
                    for (long sfh : sfs)
                    {                                            
                        for (int j = i + 1; j < 15; j++)
                        {                                                                          
                            for (int l = 2; l < 15; l++)
                            {             
                                if (l == j) continue;
                                long[] bfs = g.generateFullHouse(j, l);
                                for (long bfh : bfs)
                                {
                                    Hand small = createHand(sfh);
                                    Hand big = createHand(bfh);
                                    assertTrue(big.toString() + "\nnot better than:\n" 
                                        + small.toString(),big.compareTo(small) > 0);                                    
                                }
                            }
                        }
                    }                                       
                }
            }
        }
        
        public void testFoursOverPairs()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 15; i++)
            {
                long four = g.generateFours(i);
                
                for (int z = 0; z < repeats; z++)
                {
                    long fourWithDebris = g.generateDebris(four, i);                    
                    for (int j = 2; j < 15; j++)
                    {
                        long[] pairs = g.generatePairs(j);
                        for (long p : pairs)
                        {
                            for (int y = 0; y < repeats; y++)
                            {
                                long pairWithDebris = g.generateDebris(p, j);
                                Hand big = createHand(fourWithDebris);
                                Hand small = createHand(pairWithDebris);
                                assertTrue(big.toString() + "\nnot better than:\n" 
                                    + small.toString(),big.compareTo(small) > 0);                                    
                            }
                        }
                    }
                }                                       
                
            }
        }
        
        public void testFoursOverTriples()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 15; i++)
            {
                long four = g.generateFours(i);
                
                for (int z = 0; z < repeats; z++)
                {
                    long fourWithDebris = g.generateDebris(four, i);                    
                    for (int j = 2; j < 15; j++)
                    {
                        long[] triples = g.generatePairs(j);
                        for (long p : triples)
                        {
                            for (int y = 0; y < repeats; y++)
                            {
                                long tripleWithDebris = g.generateDebris(p, j);
                                Hand big = createHand(fourWithDebris);
                                Hand small = createHand(tripleWithDebris);
                                assertTrue(big.toString() + "\nnot better than:\n" 
                                    + small.toString(),big.compareTo(small) > 0);                                    
                            }
                        }
                    }
                }                                       
                
            }
        }
        
        public void testFoursOverFours()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 14; i++)
            {
                long four = g.generateFours(i);
                
                for (int z = 0; z < repeats; z++)
                {
                    long fourWithDebris = g.generateDebris(four, i);                    
                    for (int j = i + 1; j < 15; j++)
                    {
                        long bigFour = g.generateFours(j);                                               
                        for (int y = 0; y < repeats; y++)
                        {
                            long bigFourWithDebris = g.generateDebris(bigFour, j);
                            Hand big = createHand(bigFourWithDebris);
                            Hand small = createHand(fourWithDebris);
                            assertTrue(big.toString() + "\nnot better than:\n" 
                                + small.toString(),big.compareTo(small) > 0);                                    
                        }
                        
                    }
                }                                       
                
            }
        }                
        
        public void testFoursOverFullHouse()        
        {
            CardGenerator g = new CardGenerator();
            for (int i = 2; i < 15; i++)
            {
                for (int k = 2; k < 15; k++)
                {
                    if (k == i) continue;
                    
                    long[] fs = g.generateFullHouse(i, k);
                    
                    for (long fh : fs)
                    {                                            
                        for (int j = 2; j < 15; j++)
                        {
                            long four = g.generateFours(j);                                               
                            for (int y = 0; y < repeats; y++)
                            {
                                long fourWithDebris = g.generateDebris(four, j);
                                Hand small = createHand(fh);
                                Hand big = createHand(fourWithDebris);
                                assertTrue(big.toString() + "\nnot better than:\n" 
                                    + small.toString(),big.compareTo(small) > 0);                                    
                            }
                        }
                    }                                       
                }
            }
        }
       
        
        private Hand createHand(long cards)
        {
            Hand h = new Hand(5);
            while (cards != 0L)
            {
                long bit = cards & -cards;
                h.add(bit);
                cards ^= bit;
            }
            return h;
        }

}
