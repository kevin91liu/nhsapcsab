/**
 * $Id$
 * @author kevin liu (kevin91liu@gmail.com)
 *         shawn xu (talkingraisin@gmail.com)
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

import poker.engine.GameState.State;

public class Game 
{
	//placed here for good reason. -kevin
    private static short[] hash_adjust = new short[512];
    private static short[] flushes = new short[7937];
    private static short[] unique5 = new short[7937];
    private static short[] hash_values = new short[8192];
    
    public final static int MAXIMUM_PLAYER = 12;
    public final static int MINIMUM_PLAYER = 2;
	
	private GameState gs;		        
                                                
	/**
	 * 
	 * @param _players an array of valid activePlayers. no nulls. there may not be more than 12 activePlayers.
	 * 		           the reason for this limit is that if there are too many activePlayers, the deck will
	 * 				   run low on cards, and its drawCard() method will slow down. also, in real life, 
	 * 				   usually there are not >12 activePlayers sitting at any one table
	 */
	public Game(Player... _players) throws IOException
	{
            if (_players.length > MAXIMUM_PLAYER || _players.length < MINIMUM_PLAYER) 
                throw new IllegalArgumentException("incorrect # of players");
            try
            {            
            	populateArrayFromPCT("hash_adjust.pct", hash_adjust);
                populateArrayFromPCT("flushes.pct", flushes);         
                populateArrayFromPCT("unique5.pct", unique5);        
                populateArrayFromPCT("hash_values.pct", hash_values);           
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                System.err.println("needed file(s) not found");
                System.exit(-1);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.exit(-2);
            }
            gs = new GameState(_players, new ConsoleDisplay(false, GameConstants.MUTE_CONSOLE_OUTPUT));                     
            gs.reset();                        
	}
                                
	private void runRound()
	{                           
            gs.updateState(State.BEGIN);
            gs.beginRound();
            
            gs.updateState(State.ANTE);
            gs.ante();
            if (gs.onePlayerLeft()) return;   
            
            gs.updateState(State.DEAL);
            gs.dealOnce();
            gs.dealOnce();            
            // give out cards
            gs.giveOutCards();                
            // bets            
            gs.bettingRound(State.FIRSTBET);
            if (gs.onePlayerLeft()) return;            
            //flop                
            gs.updateState(State.FLOP);
            gs.burnCard();
            gs.addCardToBoard();
            gs.addCardToBoard();
            gs.addCardToBoard();           
            // bets          
            gs.bettingRound(State.SECONDBET);
            if (gs.onePlayerLeft()) return;
            //turn		            
            gs.updateState(State.TURN);
            gs.burnCard();
            gs.addCardToBoard();	           
            //bets            
            gs.bettingRound(State.THIRDBET);
            if (gs.onePlayerLeft()) return;
            //river            
            gs.updateState(State.RIVER);
            gs.burnCard();
            gs.addCardToBoard();	           
            //bets                  
            gs.bettingRound(State.FINALBET);
            if (gs.onePlayerLeft()) return;                     		
	}
        
        public void begin()
        {
            while (!gs.onePlayerLeft())
            {
                runRound();
                gs.showHands();
                gs.reset();
            }
            
            // the remaining player is the winner;
            LinkedList<Player> w = gs.getLastWinners();
            if (w.size() == 1)
                System.out.println(w.getFirst() + " wins!");
            else
                throw new IllegalStateException();
            
        }
		
        public static short hash_adjust(int idx)
        {
        	return hash_adjust[idx];
        }
        
        public static short flushes(int idx)
        {
        	return flushes[idx];
        }
        
        public static short unique5(int idx)
        {
        	return unique5[idx];
        }
        
        public static short hash_values(int idx)
        {
        	return hash_values[idx];
        }
        
        private void populateArrayFromPCT(String filename, short[] array) throws IOException
        {
            BufferedReader f = getBR(filename);
            String curLine;
            int index = 0;
            while ((curLine = f.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(curLine);
                while (st.hasMoreTokens())            
                    array[index++] = Short.parseShort(st.nextToken());            
            }
        }
        
        private BufferedReader getBR(String filename)
        {
            InputStream is = this.getClass().getResourceAsStream(filename);
            InputStreamReader isr = new InputStreamReader(is);
            return new BufferedReader(isr);
        }
        
}
