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

package poker.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import poker.engine.Action;
import poker.engine.Call;
import poker.engine.Check;
import poker.engine.Fold;
import poker.engine.GameInfo;
import poker.engine.Hand;
import poker.engine.Money;
import poker.engine.Player;
import poker.engine.Raise;

/**
 *
 * @author t-shawnx
 */
public class ConsoleHuman extends Player
{
    private Integer id;
    private String title;
    
    private void print(String s)
    {
        // for now, we might want a textbox later
        System.out.println(s);
    }
    
    private Action waitForInput(GameInfo info)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        try { s = br.readLine(); } 
        catch (IOException ex)
        {
        	ex.printStackTrace();
        	System.exit(0xBAD);
        }
        
        if (s.equals("fold"))
        {
            return new Fold(id);
        }
        else if (s.equals("check"))
        {
            return new Check(id);
        }
        else if (s.equals("call"))
        {
            return new Call(id);
        }
        else if (s.equals("raise"))
        {
            print("By how much? $");
            try 
            {
                String amount = br.readLine();
                return new Raise(id, 
                        new Money(Double.valueOf(amount), 
                        Money.Currency.DOLLARS));
            } 
            catch (NumberFormatException n)
            {
                return waitForInput(info);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        if (s.equals("stash"))
        {
            System.out.println("Stashes:");
            for (Integer p : info.getActivePlayerIds())   
            {
                if (p == null) continue;
                System.out.println(info.getPlayerName(p) + " " + p + ":  " + info.getStash(p));                
            }
            return waitForInput(info);
        }
        else if (s.equals("bets"))
        {
            System.out.println("Bets:");
            for (Integer p : info.getActivePlayerIds())                
                System.out.println(info.getPlayerName(p) + " " + p + ": " + info.getBet(p));
            return waitForInput(info);
        }
        else if (s.equals("potValue"))
        {
            System.out.println("PotValue:");
            System.out.println(info.getPotValue());
            return waitForInput(info);
        }
        else if (s.equals("minimumBid"))
        {
            System.out.println("Minimum Bid:");
            System.out.println(info.getMinimumCallAmount());
            return waitForInput(info);
        }
        return waitForInput(info);
    }
    
    public ConsoleHuman(String name)
    {
        super(name);
        print("You are Player: " + name + ".\nWelcome to Holdem!");
    }
    
    @Override
    public Action beginTurn(GameInfo gi) 
    {
        print(title);
        print("What would you like to do?");
        return waitForInput(gi);        
    }

    @Override
    public void beginRound(GameInfo gi) 
    {
        id = gi.getId(this);
        title = this + " " + id + ": ";
        print(title);
        print("You currently have: " + gi.getStash(id));        
    }

    @Override
    public void endRound(GameInfo gi)
    {
        // not being called right now
    }

    @Override
    public void acceptHand(Hand h) 
    {
        print(title);
        print("Your hand is: " + h);
    }

}
