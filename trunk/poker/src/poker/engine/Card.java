/**
 * $Id: Card.java 301 2008-08-14 23:31:33Z kevin91liu $
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
 */

package poker.engine;

import poker.gui.IDeflatable;

public final class Card implements Comparable<Card>, Cloneable, IDeflatable<Card>
{
	private long myValue;
	private Suit mySuit;
	private byte myNumber;
	
	public static final byte TWO = 2;
	public static final byte THREE = 3;
	public static final byte FOUR = 4;
	public static final byte FIVE = 5;
	public static final byte SIX = 6;
	public static final byte SEVEN = 7;
	public static final byte EIGHT = 8;
	public static final byte NINE = 9;
	public static final byte TEN = 10;
	public static final byte JACK = 11;
	public static final byte QUEEN = 12;
	public static final byte KING = 13;
	public static final byte ACE = 14;
	
	public enum Suit
	{
		SPADES, HEARTS, DIAMONDS, CLUBS
	}
	
	/*
	 * Uses 52 bits of the 64-bit long to represent all 52 cards.
	 * The right-most 4 bits shall represent TWO, the 4 bits to the left shall 
	 * represent 3, and so on. Of the 4 bits, the order of suits shall be: 
	 * spades, hearts, diamonds, clubs.
	 */
	public static final long TWO_MASK =    0x000000000000FL;
	public static final long THREE_MASK =  0x00000000000F0L; 
	public static final long FOUR_MASK =   0x0000000000F00L;
	public static final long FIVE_MASK =   0x000000000F000L;
	public static final long SIX_MASK =    0x00000000F0000L;
	public static final long SEVEN_MASK =  0x0000000F00000L;
	public static final long EIGHT_MASK =  0x000000F000000L;
	public static final long NINE_MASK =   0x00000F0000000L;
	public static final long TEN_MASK =    0x0000F00000000L;
	public static final long JACK_MASK =   0x000F000000000L;
	public static final long QUEEN_MASK =  0x00F0000000000L;
	public static final long KING_MASK =   0x0F00000000000L;
	public static final long ACE_MASK =    0xF000000000000L;
	
	/*
	 * the repeat of the ace mask is intentional for straight detection
	 */
	public static final long[] NUMBER_MASKS =
	{
		ACE_MASK, TWO_MASK, THREE_MASK, FOUR_MASK, FIVE_MASK, SIX_MASK, SEVEN_MASK, EIGHT_MASK,
		NINE_MASK, TEN_MASK, JACK_MASK, QUEEN_MASK, KING_MASK, ACE_MASK
	}; 
	
	/*
	 * suit masks
	 */
	public static final long SPADES_MASK =   0x8888888888888L; // ...100010001000
	public static final long HEARTS_MASK =   0x4444444444444L; // ...010001000100
	public static final long DIAMONDS_MASK = 0x2222222222222L;
	public static final long CLUBS_MASK =    0x1111111111111L;
	
	/**
	 * 
	 * @param _suit The suit of this Card. Pass in one of the 4 enums from 
	 * 				Card.Suit, e.g.	Card.Suit.SPADES.
	 * @param _number The actual value of the card (2, 3, 4, ..., queen, king, 
	 * 				ace). It is recommended to pass in one of the 13 constants,
	 * 				e.g. Card.TWO, Card.ACE.
	 */
	public Card(Suit _suit, byte _number)
	{
		if (_number < TWO || _number > ACE)
			throw new IllegalArgumentException("_val must be between 2 and 14");
		myValue = getSuitMask(_suit) & getValueMask(_number);
		mySuit = _suit;
		myNumber = _number;
	}
	
	/**
	 * constructor for testing purposes added by wilson
	 * @see IDeflatable
	 */
	public Card()
	{
		// Off with her head!
		this(Suit.HEARTS, QUEEN);
	}
	
	/**
	 * protected - used in Hand
	 * @param v bitwise representation of the card
	 */
	protected Card(long v)
	{
		myValue = v;
		mySuit = getSuit(v);
		myNumber = getNumber(v);
	}
	
	/**
	 * used for clone method
	 */
	private Card(Suit s, byte n, long v)
	{
		mySuit = s;
		myNumber = n;
		myValue = v;
	}
	
	private long getSuitMask(Suit s)
	{
		if (Suit.SPADES.equals(s)) return SPADES_MASK;
		if (Suit.HEARTS.equals(s)) return HEARTS_MASK;
		if (Suit.DIAMONDS.equals(s)) return DIAMONDS_MASK;
		return CLUBS_MASK;
	}
	
	private Suit getSuit(long v)
	{
		if ((v & SPADES_MASK) != 0L) return Suit.SPADES;
		if ((v & HEARTS_MASK) != 0L) return Suit.HEARTS;
		if ((v & DIAMONDS_MASK) != 0L) return Suit.DIAMONDS;
		return Suit.CLUBS;
	}
	
	
	public long getValueMask(byte v)
	{
		switch (v)
		{
		case 2: return TWO_MASK;
		case 3: return THREE_MASK;
		case 4: return FOUR_MASK;
		case 5: return FIVE_MASK;
		case 6: return SIX_MASK;
		case 7: return SEVEN_MASK;
		case 8: return EIGHT_MASK;
		case 9: return NINE_MASK;
		case 10: return TEN_MASK;
		case 11: return JACK_MASK;
		case 12: return QUEEN_MASK;
		case 13: return KING_MASK;
		case 14: return ACE_MASK;
		}
		return 0; // This point should never be reached.
	}
	
	/**
	 * 
	 * @param theMask a number mask.
	 * @return a byte representing the number of the card
	 */
	public static byte convertMaskToByte(long theMask)
	{ //reversed order: ace to two to optimize
		if (theMask == ACE_MASK) return ACE;
		if (theMask == KING_MASK) return KING;
		if (theMask == QUEEN_MASK) return QUEEN;
		if (theMask == JACK_MASK) return JACK;
		if (theMask == TEN_MASK) return TEN;
		if (theMask == NINE_MASK) return NINE;
		if (theMask == EIGHT_MASK) return EIGHT;
		if (theMask == SEVEN_MASK) return SEVEN;
		if (theMask == SIX_MASK) return SIX;
		if (theMask == FIVE_MASK) return FIVE;
		if (theMask == FOUR_MASK) return FOUR;
		if (theMask == THREE_MASK) return THREE;
		return TWO;
	}
	
	private byte getNumber(long v)
	{
		if ((v & TWO_MASK) != 0L) return TWO;
		if ((v & THREE_MASK) != 0L) return THREE;
		if ((v & FOUR_MASK) != 0L) return FOUR;
		if ((v & FIVE_MASK) != 0L) return FIVE;
		if ((v & SIX_MASK) != 0L) return SIX;
		if ((v & SEVEN_MASK) != 0L) return SEVEN;
		if ((v & EIGHT_MASK) != 0L) return EIGHT;
		if ((v & NINE_MASK) != 0L) return NINE;
		if ((v & TEN_MASK) != 0L) return TEN;
		if ((v & JACK_MASK) != 0L) return JACK;
		if ((v & QUEEN_MASK) != 0L) return QUEEN;
		if ((v & KING_MASK) != 0L) return KING;
		return ACE;
	}
	
	public Suit getSuit()
	{
		return mySuit;
	}
	
	public byte getNumber()
	{
		return myNumber;
	}
	
	public long getValue()
	{
		return myValue;
	}
	
    @Override
	public Card clone()
	{
		return new Card(mySuit, myNumber, myValue);
	}
	
	/**
	 * Deflate always produces a string with two characters. Although the 
	 * produced string is not meant to be human-readable, the first character 
	 * represents the suit; it is the first character of the suit's name. The 
	 * second character represents the number. 'b' represents two, 'c' means 
	 * three, and so on. 'n' represents ace.
	 */
	public String deflate()
	{
		String deflated = "";
		deflated += mySuit.name().substring(0, 1);
		deflated += new String(new char[] {(char)('a' + ((int)myNumber) - 1)});
		return deflated;
	}
	
	public Card inflate(String string) throws Exception
	{
		Suit suit = null;
		for (Suit s : Suit.values())
			if (s.name().startsWith(string.substring(0, 1)))
				suit = s;
		if (suit == null) throw new Exception("Unknown suit");
		byte number = (byte)(string.substring(1, 2).toCharArray()[0] - 'a' + 1);
		return new Card(suit, number);
	}
	
    @Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Card)) return false;
		return myValue == ((Card)o).myValue;
	}

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 67 * hash + (int) (this.myValue ^ (this.myValue >>> 32));
            return hash;
        }
	
	public int compareTo(Card o)
	{
		return (int)(myNumber - o.myNumber);
	}
	
    @Override
	public String toString()
	{
		String n = "";
		switch (myNumber)
		{
		case TWO: n = "Two"; break;
		case THREE: n = "Three"; break;
		case FOUR: n = "Four"; break;
		case FIVE: n = "Five"; break;
		case SIX: n = "Six"; break;
		case SEVEN: n = "Seven"; break;
		case EIGHT: n = "Eight"; break;
		case NINE: n = "Nine"; break;
		case TEN: n = "Ten"; break;
		case JACK: n = "Jack"; break;
		case QUEEN: n = "Queen"; break;
		case KING: n = "King"; break;
		case ACE: n = "Ace"; break;
		default: return "Invalid Card";
		}
		return String.format("%s of %s", n, mySuit.name());
	}
}

