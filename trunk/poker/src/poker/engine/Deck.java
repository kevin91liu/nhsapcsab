/**
 * $Id$
 * @author kevin liu (kevin91liu@gmail.com)
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

import java.util.Random;

public final class Deck 
{
	private long cards;
	private Random rand;
	
	private static final long FULLDECK = 0xFFFFFFFFFFFFFL; //52 card deck
	
	public Deck()
	{
		rand = new Random();
		cards = FULLDECK;
	}
	
	public void reset()
	{
		cards = FULLDECK;
	}
	
	/**
	 * 
	 * @return a randomly chosen card from the deck. null if deck is empty.
	 */
	public Card drawCard()
	{
		long card = drawCardLong();
                if (card == 0L)
                    return null;
		return new Card(card);
	}
	
	public long drawCardLong()
	{
            if (cards == 0L)
                return 0L;
            long aCard;
            int position;
            do
            {
                position = rand.nextInt(52);
                aCard = 0x1L << position;
            }
            while ((cards | aCard) != cards);
            
            cards ^= aCard;
            return aCard;
	}
        
        public int size()
        {
            return Hand.countBits(cards);
        }
}

