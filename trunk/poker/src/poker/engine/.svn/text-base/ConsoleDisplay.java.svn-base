/**
 * $Id$
 * @author shawn xu (talkingraisin@gmail.com)
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import poker.engine.GameState.State;

public class ConsoleDisplay implements IDisplay
{
    GameInfo info;
    GameState gs;
    
    // If this is true, then every action (Ante, Deal, etc) 
    // will wait for input before continuing
    boolean stepByStep;
    boolean mute; //if true, will mute prints for cleaner console.
        
    public ConsoleDisplay()
    {
        stepByStep = false; mute = false;
    }
    
    public ConsoleDisplay(boolean _stepByStep, boolean _mute)
    {
        stepByStep = _stepByStep;
        mute = _mute;
    }
    
    private void print(String s)
    {        
        if (!mute) System.out.println(s);            
    }            
    
    private void waitForInput()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        try
        {            
            s = br.readLine();
        } 
        catch (IOException ex)
        {                
            ex.printStackTrace();
        }

        if (s.equals("stash"))
        {
            System.out.println("Stashes:");
            for (Integer p : info.getActivePlayerIds())   
            {                
                System.out.println(info.getPlayerName(p) + " " + p + ":  $" + info.getStash(p).getAmount());                
            }
            waitForInput();
        }
        else if (s.equals("bets"))
        {
            System.out.println("Bets:");
            for (Integer p : info.getActivePlayerIds())                
                System.out.println(info.getPlayerName(p) + " " + p + ":  $" + info.getBet(p).getAmount());
            waitForInput();
        }
        else if (s.equals("potValue"))
        {
            System.out.println("PotValue:");
            System.out.println("$" + info.getPotValue().getAmount());
            waitForInput();
        }
        else if (s.equals("minimumBid"))
        {
            System.out.println("Minimum Bid:");
            System.out.println("$" + info.getMinimumCallAmount().getAmount());
            waitForInput();
        }
        else if (s.equals("players"))
        {
            System.out.println("Active players:");
            for (Integer p : info.getActivePlayerIds())
            {
                System.out.println(info.getPlayerName(p));
            }
        }
    }

    public void displayMessage(String s) 
    {
        print(s);
        if (stepByStep)
            waitForInput();
    }

    public void beginRound(GameState state) 
    {
        info = state.getInfo(false);
        gs = state;
        print("Beginning New Round...");
        if (stepByStep)
            waitForInput();
    }

    public void preAnte(GameState state) 
    {
        info = state.getInfo(false);
        gs = state;
        print("Ante...");
    }

    public void postAnte(GameState state) 
    {
        info = state.getInfo(false);
        gs = state;
        if (stepByStep)
            waitForInput();
    }

    public void preDealOnce(GameState state)
    {
        info = state.getInfo(false);
        gs = state;
        print("Dealing Once...");
    }

    public void postDealOnce(GameState state)
    {
        info = state.getInfo(false);
        gs = state;
        if (stepByStep)
            waitForInput();
    }

    public void cardsDealtToPlayers(GameState state, HashMap<Player, Hand> hands)
    {        
        info = state.getInfo(false);
        gs = state;
    }

    public void preBettingRound(State round, GameState state)
    {
        info = state.getInfo(false);
        gs = state;
        print("Starting " + round.toString() + " round of betting...");
        if (stepByStep)
            waitForInput();
    }

    public void betsClearedFromTable(GameState state) 
    {
        info = state.getInfo(false);
        gs = state;
    }

    public void cardAddedToBoard(GameState state, Card card)
    {
        info = state.getInfo(false);
        gs = state;
        print("Card added to Board: " + card.toString());
    }

    public void cardBurnt(GameState state)
    {
        info = state.getInfo(false);
        gs = state;
        print("Burning a card...");
    }

    public void roundEnded(GameState state)
    {
        info = state.getInfo(false);
        gs = state;
        if (!gs.onePlayerLeft())
        {
            for(Integer i : info.getActivePlayerIds())            
                print(gs.getPlayer(i).toString() + " " + i + " has "
                     + gs.getHand(gs.getPlayer(i)).toString());            
        }
        print("Winner(s): ");
        for (Player p : gs.getLastWinners())        
            print(p.toString() + " " + info.getId(p)); 
        if (stepByStep)
            waitForInput();
    }

    public void actionPerformed(GameState state, Player p, Action a)
    {
        info = state.getInfo(false);
        gs = state; 
        print(info.getActions().getLast().toString());
        if (stepByStep)
            waitForInput();
    }    
    
}
