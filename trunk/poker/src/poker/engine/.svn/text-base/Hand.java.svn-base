/**
 * $Id$
 * @author Kevin Liu (kevin91liu@gmail.com)
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package poker.engine;

import poker.gui.IDeflatable;


/*
 * This game assumes a 52-card deck (no more, no less), so there can not be 
 * two exact same cards (for example, 2 of spades and 2 of spades).
 */
public final class Hand implements Comparable<Hand>, Cloneable, IDeflatable<Hand>
{
    private static final int[] bitpos64 =
    {
		63,  0, 58,  1, 59, 47, 53,  2,
		60, 39, 48, 27, 54, 33, 42,  3,
		61, 51, 37, 40, 49, 18, 28, 20,
		55, 30, 34, 11, 43, 14, 22,  4,
		62, 57, 46, 52, 38, 26, 32, 41,
		50, 36, 17, 19, 29, 10, 13, 21,
		56, 45, 25, 31, 35, 16,  9, 12,
		44, 24, 15,  8, 23,  7,  6,  5
    };
    private static final int[] bc_to_prime =
    {
        2,2,2,2,//deuce
        3,3,3,3,//3
        5,5,5,5,//4
        7,7,7,7,
        11,11,11,11,
        13,13,13,13,
        17,17,17,17,
        19,19,19,19,
        23,23,23,23,
        29,29,29,29,
        31,31,31,31,
        37,37,37,37,
        41,41,41,41
    };   

    
	private int handSize;
	private int handSizeLimit;
	private long cards;
	
	/**
	 * 
	 * @param sizeLimit The maximum number of cards the hand can hold. 
	 */
	public Hand(int sizeLimit)
	{
		handSize = 0;
		handSizeLimit = sizeLimit;
		cards = 0L;
	}
	
	public Hand(Card... cards)
	{
		this(cards.length);
		for (Card card : cards)
			add(card);
	}
	
	//used in cloning
	public Hand(Hand h)
	{
		cards = h.cards;
		handSize = h.handSize;
		handSizeLimit = h.handSizeLimit;
	}
        
        public Hand(long v)
        {
            cards = v;
            handSize = countBits(v);
            handSizeLimit = handSize;
        }
	
	//used in cloning
	public Hand(long v, int _handSize, int _handSizeLimit)
	{
		cards = v;
		handSize = _handSize;
		handSizeLimit = _handSizeLimit;
	}
	
	public void clear()
        {
            cards = 0L;
            handSize = 0;
        }
        
        public int getHandSizeLimit()
        {
            return handSizeLimit;
        }
	
	/**
	 * number of cards in the hand
	 */
	public int size()
	{
            return handSize;
	}
	
	/**
	 * 
	 * @param c The card to add in.
	 * @return True if the card was successfully added to the hand, false if not.
	 *         If false is returned, it could be because:
	 *         <ul>
	 *         <li> the hand is full
	 *         <li> the card already exists in the hand
	 *         </ul>
	 */
	public boolean add(Card c)
	{
            return add(c.getValue());
	}
	
	/**
	 * add a card in its long form
	 * @param v the long bit representation of the card
	 * @return true if successfully added. false otherwise.
	 */
	public boolean add(long v)
	{
		if (handSize >= handSizeLimit) // Then the hand is full
			return false;
		
		if (has(v)) // Then the card is already in the hand
			return false;
		
		handSize++;
		cards ^= v;
		return true;
	}
	
	/**
	 * 
	 * @param c
	 * @return True if the hand contains the card c.
	 */
	public boolean has(Card c)
	{
		return has(c.getValue());
	}
	
	/**
	 * 
	 * @param v
	 * @return True if hand contains all the cards represented in v.
	 */
	public boolean has(long v)
	{
		return ((cards & v) != 0);
	}
	
	/**
     * @param c
	 * @return True if successfully removed, false otherwise. Could return 
	 * 		false if the card is not in the hand or if the card is invalid.
	 */
	public boolean remove(Card c)
	{
		return remove(c.getValue());
	}
	
	public boolean remove(long v)
	{
		if (!has(v)) return false;
		cards ^= v;
		handSize--;
		return true;
	}
	
	/**
	 * 
	 * @param pHand Player hand (2 cards).
	 * @param bHand Board hand (5 cards).
	 * @return
	 */
	public static Hand merge(Hand pHand, Hand bHand)
	{
		return new Hand((pHand.getBitCards() | bHand.getBitCards()), 
						pHand.handSize + bHand.handSize, 
						pHand.handSizeLimit + bHand.handSizeLimit);
	}
	
	/**
	 * 
	 * @param h a hand with 7 cards in it. 
	 * @return an array of all possible hand combinations of size 5
	 */
	public static Hand[] getCombinations7(Hand h)
	{
            long[] lcombos = getCombinations7(h.getBitCards());
            Hand[] ret = new Hand[21];
            for (int i = 0; i < 21; i++)
            {
                ret[i] = new Hand(lcombos[i]);
            }
            return ret;
	}
        
    public static long[] getCombinations7(long bigHand)
    {
        long copy = bigHand;      
        long[] bits = new long[7];
        long[] ret = new long[21];        
        
        for (int i = 0; i < 7; i++)        
            bigHand ^= bits[i] = bigHand & -bigHand;
        
        int idx = 0;
        for (int i = 0; i < 6; i++)
            for (int j = i + 1; j < 7; j++)
                ret[idx++] = copy ^ (bits[i] | bits[j]);
        
        return ret;
    }
        
        public static Hand[] getCombinations6(Hand h)
        {
            long[] lcombos = getCombinations6(h.getBitCards());
            Hand[] ret = new Hand[6];
            for (int i = 0; i < 6; i++)
            {
                ret[i] = new Hand(lcombos[i]);
            }
            return ret;
        }
        
    public static long[] getCombinations6(long bigHand)
    {
        long copy = bigHand;      
        long[] ret = new long[6];        
        
        long bit;
        for (int i = 0; i < 6; i++)        
        {
            ret[i] = copy ^ (bit = (bigHand & -bigHand));
            bigHand ^= bit;
        }        
        
        return ret;
    }
	
	/**
	 * @param pHand Player's 2 card hand.
	 * @param bHand Board's 3-5 cards. must have 3-5 cards
	 * @return The strongest poker hand that can be made from pHand and bHand.
	 */
    public static Hand getHighestHand(Hand pHand, Hand bHand)
    {
        return new Hand(getHighestHand(pHand.getBitCards(), bHand.getBitCards()), 5, 5);
    }    
    
    public static long getHighestHand(long pHand, long bHand)
    {
        long bigHand = pHand | bHand;
        long[] combos;
        int count = countBits(bigHand);
        switch (count)
        {
	        case 7:
	        	combos = getCombinations7(bigHand); break;
	        case 6: 
	        	combos = getCombinations6(bigHand); break;
	        default:
	        	return bigHand;
        }
        
        long highest = 0L;
        int highestRating = 0;
        for (long candidate : combos)
        {
            if (highest == 0L)
            {
                highest = candidate;
                highestRating = rate(candidate);
                continue;
            }
            
            int candidateRating;
            
            if ((candidateRating = rate(candidate)) < highestRating)
            {
                highest = candidate;
                highestRating = candidateRating;
            }
        }
        return highest;
    }    
    
        /**
	 * @param pHand Player's 2 card hand.
	 * @param bHand Board's 3-5 cards. must have 3-5 cards
	 * @return The strongest poker hand that can be made from pHand and bHand.
	 */
    //public static Hand getHighestHand(Hand pHand, Hand bHand)
    {
        //return new Hand(getHighestHand(pHand.getBitCards(), bHand.getBitCards()));
    }    
    
    //TODO: note to self, dependency on rate()...
        /**
	 * @param pHand a long representing Player's 2 card hand.
	 * @param bHand a long representing Board's 3-5 cards. must have 3-5 cards
	 * @return a long representing strongest poker hand that can be made from pHand and bHand.
	 */
    /*public static long getHighestHand(long pHand, long bHand)
    {
        long bigHand = pHand | bHand;
        long[] combos;
        int count = countBits(bigHand);
        if (count == 7)
            combos = getCombinations7(bigHand);
        else if (count == 6)
            combos = getCombinations6(bigHand);
        else
            return bigHand;
        
        long highest = 0L;
        int highestRating = 0;
        for (long candidate : combos)
        {
            if (highest == 0L)
            {
                highest = candidate;
                highestRating = rate(candidate);
                continue;
            }
            
            int candidateRating;
            
            if ((candidateRating = rate(candidate)) < highestRating)
            {
                highest = candidate;
                highestRating = candidateRating;
            }
        }
        return highest;
    }*/
	
	/**
	 * 
	 * @param h
	 * @return Highest card number of the hand. If hand is empty, returns -1.
	 */
	private static byte getHighestCardNumber(Hand h)
	{
		long temp = h.getBitCards();
		for (int a = Card.NUMBER_MASKS.length - 1; a > 0; a--) //a > 0 intentional and correct
			if ((temp & Card.NUMBER_MASKS[a]) != 0)
				return Card.convertMaskToByte(Card.NUMBER_MASKS[a]);
		return -1;
	}
	
	/**
	*
	* @param h A non-empty hand.
	* @return The highest card in the and h. Returns the card represented by 
	* 		the left-most bit in h's cards field. Therefore, when h contains 
	* 		more than one card of the same number (such as two 10s) it returns 
	* 		in order of spades, hearts, diamonds, clubs. Refer to the suit 
	* 		masks in Card. returns null if passed an empty Hand
         * 
	*/
	public static Card getHighestCard(Hand h)
	{
            long c;
            if ((c = getHighestCard(h.getBitCards())) == 0L) return null;
		return new Card(c); 
	}
	
	/**
	 * @return left-most bit, AKA the most significant bit
	 */
	public static long getHighestCard(long h)
	{
            if (h == 0L) return 0L;
            return 1L << mostSigBit(h);
	}
        
        //these arrays are used for the mostSigBit method
        private static final long bmask[] = {0x2L, 0xCL, 0xF0L, 0xFF00L, 0xFFFF0000L, 0xFFFFFFFF00000000L};
        private static final int S[] = {1, 2, 4, 8, 16, 32};

        /**
         * returns index of the most significant bit (left most bit)
         * if the left-most bit is on, then this returns 63. if the right most bit
         * is the MSB, then this returns 0
         */
        public static int mostSigBit(long v)
        {
            int idx = 0;
            for (int i = 5; i >= 0; i--)
            {
               if ((v & bmask[i]) != 0)
               {
                  v >>= S[i];
                  idx |= S[i];
               } 
            }
            return idx;
        }

	
	public long getBitCards()
	{
            return cards;
	}
	
	/**
	 * 
	 * @return array of deep cloned cards
	 */
	public Card[] getCards()
	{
		Card[] ret = new Card[handSize];
		int idx = 0;
		
		long temp = cards;
		while (temp != 0L)
		{
			long aBitCard = temp & -temp; //leftmost bit
			ret[idx] = new Card(aBitCard);
			temp ^= aBitCard;
			idx++;
		}
		return ret;
	}
	
    @Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Hand)) return false;
		return cards == ((Hand)o).cards;
	}

    @Override
        public int hashCode() 
        {
            int hash = 59 * 7 + (int) (this.cards ^ (this.cards >>> 32));
            return hash;
        }
	
    @Override
	public Hand clone()
	{
		return new Hand(this);
	}
    
	/*
	 * Case card values for use in evaluation and comparison.
	 */
	private static final int HIGHCARD = 1;
	private static final int PAIR = 20;
	private static final int TWOPAIR = 100;
	private static final int THREEOFAKIND = 500;
	private static final int STRAIGHT = 1000;
	private static final int FLUSH = 2000;
	private static final int FULLHOUSE = 3000;
	private static final int FOUROFAKIND = 4000;
	private static final int STRAIGHTFLUSH = 5000;


	/**
	 * Use this method only to compare 5-card hands to other 5-card hands. If 
	 * the return value is positive, then this is larger than h. If the return 
	 * value is 0, then the hands are equally strong. If the return value is 
	 * negative, then this is weaker than h.
	 */
	public int compareTo(Hand that)
	{
		return rate(that) - rate(this);
	}
	
    public static int rate(Hand h)
    {        
        return rate(h.getBitCards());
    }
	
    /**
     * There are 7462 distinct poker hands, in these categories (not in order of rank):
     * 
     * Straight Flush (includes Royal Flush)
     * Flush
     * 
     * Straight
     * High Card
     * 
     * Full House
     * Four of a Kind
     * Three of a Kind
     * Two Pair
     * One Pair
     * 
     * @param h a long representing the hand to rate.
     * @return rating - the lower the stronger
     */ 
    public static int rate(long h)
    {
        int slh = suitlessHand(h);
        
        // Takes care of the Flush and Straight Flush
        if (hasFlush(h))        
            return Game.flushes(slh);
        
        // Takes care of Straight and High Card
        if (Game.unique5(slh) != 0) //5 unique card values                   
            return Game.unique5(slh);
        
        // Takes care of the rest (used to be binary search, now using perfect hash)
        //int idx = binarySearch(multBits(h), 0, products.length-1);
        return Game.hash_values(perfect_hash(multBits(h)));
    }
	
	/* 
	 * deprecated function
	public int compareTo(Hand h)
	{
		if (countBits(this.getBitCards()) == 0 || countBits(h.getBitCards()) == 0)
			return 0;
		
		int thisVal = HIGHCARD + getHighestCardNumber(this);
		int otherVal = HIGHCARD + getHighestCardNumber(h);
		
		int tempThis = countPairs(this);
		int tempOther = countPairs(h);
		if (tempThis == 1) thisVal = PAIR;
		if (tempOther == 1) otherVal = PAIR;
		
		if (tempThis == 2) thisVal = TWOPAIR;
		if (tempOther == 2) otherVal = TWOPAIR;
		
		
		long thisTripleMask;
		long otherTripleMask;
		if ((thisTripleMask = tripleMask(this)) != -1L) thisVal = THREEOFAKIND;
		if ((otherTripleMask = tripleMask(h)) != -1L) otherVal = THREEOFAKIND;
		
		if (hasStraight(this)) thisVal = STRAIGHT;
		if (hasStraight(h)) otherVal = STRAIGHT;
		
		if (hasFlush(this)) thisVal = FLUSH;
		if (hasFlush(h)) otherVal = FLUSH;
		
		if (hasFullHouse(this)) thisVal = FULLHOUSE;
		if (hasFullHouse(h)) otherVal = FULLHOUSE;
		
		long thisQuadMask;
		long otherQuadMask;
		if ((thisQuadMask = quadMask(this)) != -1L) thisVal = FOUROFAKIND;
		if ((otherQuadMask = quadMask(h)) != -1L) otherVal = FOUROFAKIND;
		
		if (hasStraightFlush(this)) thisVal = STRAIGHTFLUSH;
		if (hasStraightFlush(h)) otherVal = STRAIGHTFLUSH;
		
		if (thisVal != otherVal)
			return thisVal - otherVal;
		
		if (thisVal == STRAIGHTFLUSH)
		{
			thisVal += getHighestCardNumber(this);
			otherVal += getHighestCardNumber(h);
			return thisVal - otherVal;
		}
		
		if (thisVal == FOUROFAKIND)
		{
			if (thisQuadMask != otherQuadMask)
				return Card.convertMaskToByte(thisQuadMask) - 
					Card.convertMaskToByte(otherQuadMask);
			Hand thisClone = this.clone();
			thisClone.remove(thisQuadMask);
			Hand otherClone = h.clone();
			otherClone.remove(otherQuadMask);
			return (getHighestCardNumber(thisClone) -
			getHighestCardNumber(otherClone) );
		}
		
		if (thisVal == FULLHOUSE)
		{ // Compare triple first. then double.
			if (thisTripleMask != otherTripleMask)
				return Card.convertMaskToByte(thisTripleMask) - 
					Card.convertMaskToByte(otherTripleMask);
			// Triple is the same. compare the pair
			return Card.convertMaskToByte(pairMask(this)) - 
				Card.convertMaskToByte(pairMask(h));
		}
		
		if (thisVal == FLUSH)
		{
			byte thisHighCN = getHighestCardNumber(this);
			byte otherHighCN = getHighestCardNumber(h);
			
			if (thisHighCN != otherHighCN)
				return thisHighCN - otherHighCN;
			
			Hand thisClone = this.clone();
			Hand otherClone = h.clone();
			thisClone.remove(getHighestCard(thisClone));
			otherClone.remove(getHighestCard(otherClone));
			return thisClone.compareTo(otherClone);
		}

		if (thisVal == STRAIGHT)
		{
			thisVal += getHighestCardNumber(this);
			otherVal += getHighestCardNumber(h);
			return thisVal - otherVal;
		}

		if (thisVal == THREEOFAKIND)
		{
			if (thisTripleMask != otherTripleMask)
				return Card.convertMaskToByte(thisTripleMask) - Card.convertMaskToByte(otherTripleMask);
			
			Hand thisClone = this.clone();
			thisClone.removeNumberCards(thisTripleMask);
			
			Hand otherClone = h.clone();
			otherClone.removeNumberCards(otherTripleMask);
			
			return thisClone.compareTo(otherClone);
		}
		
		if (thisVal == TWOPAIR)
		{
			long thisPairMask = highestPairMask(this);
			long otherPairMask = highestPairMask(h);
			if (thisPairMask != otherPairMask)
				return Card.convertMaskToByte(thisPairMask) - Card.convertMaskToByte(otherPairMask);
			Hand thisClone = this.clone();
			Hand otherClone = h.clone();
			thisClone.removeNumberCards(thisPairMask);
			otherClone.removeNumberCards(otherPairMask);
			return thisClone.compareTo(otherClone);
		}
		
		if (thisVal == PAIR)
		{
			long thisPairMask = highestPairMask(this); //same for this and h
			long otherPairMask = highestPairMask(h);
			if (thisPairMask != otherPairMask)
				return Card.convertMaskToByte(thisPairMask) - Card.convertMaskToByte(otherPairMask);
			
			Hand thisClone = this.clone();
			Hand otherClone = h.clone();
			thisClone.removeNumberCards(thisPairMask);
			otherClone.removeNumberCards(otherPairMask);
			return thisClone.compareTo(otherClone);
		}
		
		// By now, thisVal == otherVal, and their high cards are same value
		Hand thisClone = this.clone();
		Hand otherClone = h.clone();
		thisClone.remove(getHighestCard(thisClone));
		otherClone.remove(getHighestCard(otherClone));
		return thisClone.compareTo(otherClone);
	}
	
	*/
	
    private static int multBits(long h)
    {
        int result = 1;
        long bits = h;        
        long bit;
        while (bits != 0)
        {
            result *= bc_to_prime[bitpos64[(int)(((bit = (bits & -bits))*0x07EDD5E59A4E28C2L)>>>58)]];
            bits ^= bit;
        }
        return result;
    }
    
    private static int perfect_hash(int u)
    {
      int a, b, r;
      u += 0xE91AAA35;
      u ^= u >>> 16;
      u += u << 8;
      u ^= u >>> 4;
      b  = (u >>> 8) & 0x1FF;
      a  = (u + (u << 2)) >>> 19;
      r  = a ^ Game.hash_adjust(b);
      return r;
    }
    
    /**
     * converts a long in which 52 bits are used to an int in which 13 bits are used.
     * use this only for the special case of flush: each of the 5 cards
     * are unique in value and have the same suit
     * 
     * if passed a 5 card hand, this returns a value between
     * 0x1F00 and 0x001F
     */
    public static int suitlessHand(long h)
    {
        int slh = 0;
        int i = 0;
        while (h != 0L)
        {
            if ((h & 0xFL) != 0) //if right most 4 bits have a 1 set            
                slh |= (1 << i);            
            i++;
            h >>>= 4;
        }
        return slh;
    }           
    
	public long highestPairMask(Hand h)
	{
		long temp = h.getBitCards();
		for (int a = Card.NUMBER_MASKS.length - 1; a > 0; a--)
			if (countBits(temp & Card.NUMBER_MASKS[a]) == 2)
				return Card.NUMBER_MASKS[a];
		return -1;
	}
	
	public void removeNumberCards(long numberMask)
	{
		this.cards = (cards & (~numberMask));
	}
	
	/**
	 * 
	 * @param h A 5-card hand
	 * @return true If all 5 cards are of the same suit.
	 */
	public static boolean hasFlush(Hand h)
	{
		return hasFlush(h.getBitCards());
	}
	
    private static boolean hasFlush(long h)
    {
        if ((h | Card.SPADES_MASK) == Card.SPADES_MASK) return true;
        if ((h | Card.CLUBS_MASK) == Card.CLUBS_MASK) return true;
        if ((h | Card.DIAMONDS_MASK) == Card.DIAMONDS_MASK) return true;
        if ((h | Card.HEARTS_MASK) == Card.HEARTS_MASK) return true;
        return false;
    }
	
	/**
	 * 
	 * @param h A 5-card hand
	 */
	public static boolean hasStraight(Hand h)
	{
		long handBits = h.getBitCards(); //cache
		byte count = 0;
		for (long aMask : Card.NUMBER_MASKS)
		{
			if ((handBits & aMask) != 0L)
				count++;
			else
				count = 0;
			if (count == 5) return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return If the hand has a 4 of a kind, it will return the mask of that 
	 * 		4 of a kind. if there is no 4 of a kind, it will return -1, all 
	 * 		bits on.
	 */
	public static long quadMask(Hand h)
	{
            for (long aMask : Card.NUMBER_MASKS)
            {
                long handBits = h.getBitCards();
                if (countBits(handBits & aMask) == 4)                
                    return aMask;                
            }
            return -1L;
	}
	
	/**
	 * 
	 * @param h
	 * @return The mask of the triple in a hand. -1L if no triple is present.
	 * 
	 */
	public static long tripleMask(Hand h)
	{
	    long handBits = h.getBitCards();
	    for (int a = Card.NUMBER_MASKS.length-1; a > 0; a--)
                if (countBits(Card.NUMBER_MASKS[a] & handBits) == 3)
	            return Card.NUMBER_MASKS[a];
            return -1L;
	}
	
	public static int countPairs(Hand h)
	{
		long handBits = h.getBitCards();
		int pairCount = 0;
		for (int a = Card.NUMBER_MASKS.length - 1; a > 0; a--) // Skip 0th element since it is an ACE repeat
			if (countBits (handBits & Card.NUMBER_MASKS[a]) == 2)
				pairCount++;
		return pairCount;
	}
	
	/**
	 * 
	 * @return The mask for the highest pair's number.
	 */
	public static long pairMask(Hand h)
	{
		long handBits = h.getBitCards();
		for (int a = Card.NUMBER_MASKS.length-1; a > 0; a--)
			if (countBits(Card.NUMBER_MASKS[a] & handBits) == 2)
				return Card.NUMBER_MASKS[a];
		return -1L;
	}
	
	public static boolean hasFullHouse(Hand h)
	{
		return (countPairs(h) == 1) && (tripleMask(h) != -1L);
	}
	
	public static boolean hasStraightFlush(Hand h)
	{
		return hasStraight(h) && hasFlush(h);
	}
	
	/**
	 * 
	 * @param v
	 * @return Number of bits set to 1 in v.
	 */
	public static int countBits(long v)
	{
		int bits = 0;
		for ( ; v != 0; bits++)
			v &= v-1;
		return bits;
	}
        
        /**
         * utility method not used in engine, but provided to players
         */
        public static int countBits(int v)
        {
            int bits = 0;
            for (; v != 0; bits++)
            {
                v &= v-1;
            }
            return bits;
        }
        
	
    @Override
	public String toString()
	{
		String ret = "";
		Hand temp = this.clone();
		while (temp.handSize > 0)
		{
			ret += getHighestCard(temp);
			ret += ", ";
			temp.remove(getHighestCard(temp));
		}
		return ret.substring(0, ret.length() - 2);
	}

	public String deflate() 
        {
		String header = new String(new char[] {
			(char)('a' + handSizeLimit - 1), (char)('a' + handSize - 1)
		});
		return header + Long.toHexString(cards);
	}

	public Hand inflate(String s) throws Exception 
        {
		char[] raw = s.toCharArray();
		int limit = raw[0] - 'a' + 1;
		int size = raw[1] - 'a' + 1;
		long theCards = Long.parseLong(s.substring(2), 16);
		return new Hand(theCards, size, limit);
	}
        
}

