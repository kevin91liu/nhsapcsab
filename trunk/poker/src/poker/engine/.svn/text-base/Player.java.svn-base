/**
 * $Id$
 * @author kevin liu (kevin91liu@gmail.com)
 * @author shawn xu
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


public abstract class Player
{
	private String myName;
	
	public Player(String _name)
	{
		myName = _name;
	}
	
	/*
	 * we define a "tournament" to be a series (1+) of "games." each "game"
	 * is a series of rounds. each round players receive a hand and play it out.
	 * thus, a game lasts however many rounds until one player has all the money.
	 * then, another game starts. this continues until the tournament is done.
	 */
	public abstract Action beginTurn(GameInfo gi);
	public abstract void beginRound(GameInfo gi);
	public abstract void endRound(GameInfo gi);
	
	/**
	 * classes extending the Player class must implement this. the Game object should pass a
	 * 2-card hand to the player, at which point it is the player's responsibility to cache it
	 * @param h a 2-card hand
	 */
	public abstract void acceptHand(Hand h);

	
        public final String toString()
        {
            return myName;
        }
}

