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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import poker.engine.GameState.State;

public class GameInfo
{
    private Card[] board; //the face up cards on the table
    private HashMap<Integer, Money> bets; //how much each player has betted
    private HashMap<Integer, Money> stash; // how much each player has
    private HashMap<Player, Integer> identity;
    private HashMap<Integer, Player> players;
    private Money potValue; //the value of the pot
    private Money minimumBid;
    private State state;
    private LinkedList<Action> actions;
    private Set<Integer> activePlayerIds;
    private Set<Integer> foldedPlayerIds;
    private Set<Integer> loserIds;
    private int deckSize;
    private Card turn;
    private Card river;
    
    private LinkedList<Player> winners;
    
    public GameInfo(
            Card[] board, 
            HashMap<Integer, Money> bets,
            HashMap<Integer, Money> stash,
            HashMap<Player, Integer> identity,
            HashMap<Integer, Player> players,
            Money potValue,
            Money minimumBid,
            GameState.State state,
            LinkedList<Action> actions,
            Set<Integer> activePlayerIds,
            Set<Integer> foldedPlayerIds,
            Set<Integer> loserIds,
            int _deckSize,
            Card _turn,
            Card _river
            )
    {
        this.board = board;
        this.bets = bets;
        this.stash = stash;
        this.identity = identity;
        this.players = players;
        this.potValue = potValue;
        this.minimumBid = minimumBid;
        this.state = state;
        this.actions = actions;
        this.activePlayerIds = activePlayerIds;
        this.foldedPlayerIds = foldedPlayerIds;
        this.loserIds = loserIds;
        this.deckSize = _deckSize;
        this.turn = _turn;
        this.river = _river;
    }
    
    //special constructor to be used for giving info at end of round. includes info on winners
    public GameInfo(
            Card[] board, 
            HashMap<Integer, Money> bets,
            HashMap<Integer, Money> stash,
            HashMap<Player, Integer> identity,
            HashMap<Integer, Player> players,
            Money potValue,
            Money minimumBid,
            GameState.State state,
            LinkedList<Action> actions,
            Set<Integer> activePlayerIds,
            Set<Integer> foldedPlayerIds,
            Set<Integer> loserIds,
            int _deckSize,
            Card _turn,
            Card _river,
            LinkedList<Player> _winners
            )
    {
        this.board = board;
        this.bets = bets;
        this.stash = stash;
        this.identity = identity;
        this.players = players;
        this.potValue = potValue;
        this.minimumBid = minimumBid;
        this.state = state;
        this.actions = actions;
        this.activePlayerIds = activePlayerIds;
        this.foldedPlayerIds = foldedPlayerIds;
        this.loserIds = loserIds;
        this.deckSize = _deckSize;
        this.turn = _turn;
        this.river = _river;
        this.winners = _winners;
    }
    
    public int deckSize()
    {
        return deckSize;
    }
    
    public LinkedList<Integer> getWinnerIDs()
    {
    	if (winners == null) return null;
    	LinkedList<Integer> ret = new LinkedList<Integer>();
    	for (Player winner : winners)
    	{
    		ret.add(identity.get(winner));
    	}
    	return ret;
    }
    
    public boolean isValid(Action a)
        {
            if (a instanceof Call)
            {
                // you can only call if someone bet
                double minimum = minimumBid.getAmount();
                return !(minimum <= GameConstants.ZERO.getAmount());
            }
            else if (a instanceof Check)
            {                
                // you can only check if no one bet
                return (minimumBid.getAmount() == GameConstants.ZERO.getAmount());
            }
            else if (a instanceof Raise)
            {
                // must raise over the minimum
            	//since we changed minimum to = 0, you can not raise by 0. it must be >0
                Raise r = (Raise)a;
                if (r.getAmount().getAmount() <= GameConstants.MINIMUMBET.getAmount())
                    return false;
                // must have enough money
                if (getStash(a.getPlayerId()).getAmount() < 
                        (r.getAmount().getAmount()
                        + minimumBid.getAmount()
                        - bets.get(a.getPlayerId()).getAmount()))
                    return false;
            }
            return true;
        }
    
    public Hand getBoard()
    {
    	Hand h = new Hand(5);
    	for (Card c : board)    	
    		h.add(c);
    	
        return h;
    }
    
    public int getNumberOfPlayersThatStartedThisRound()
    {
    	return activePlayerIds.size() + foldedPlayerIds.size();
    }
    
    public Money getBet(Integer id)
    {
        return bets.get(id);
    }
    
    //just money put in for this betting round.
    //TODO: need money put in for the entire round
    public Money getBet(Player p)
    {
        return bets.get(identity.get(p));
    }
    
    public Money getStash(Integer id)
    {
        return stash.get(id);
    }
    
    public Money getStash(Player p)
    {
        return stash.get(identity.get(p));
    }
    
    public Integer getId(Player p)
    {
        return identity.get(p);
    }
    
    public Collection<Integer> getIds()
    {
        return identity.values();
    }
    
    public String getPlayerName(Integer id)
    {
        return players.get(id).toString();
    }
    
    public int getNumberOfPlayers()
    {
        return identity.size();
    }        
    
    public Money getPotValue()
    {
        return potValue;
    }
    
    public Money getMinimumCallAmount()
    {
        return minimumBid;
    }
    
    public State getCurrentState()
    {
        return state;
    }
    
    public LinkedList<Action> getActions()
    {
        return actions;
    }
    
    public Set<Integer> getActivePlayerIds()
    {
        return activePlayerIds;
    }
    
    public Set<Integer> getFoldedPlayerIds()
    {
        return foldedPlayerIds;
    }
    
    public Set<Integer> getLoserIds()
    {
        return loserIds;
    }
    
    /**
     * returns the turn card. if the turn card hasn't come out yet, returns null
     */ 
    public Card getTurn()
    {
        return turn;
    }
    
    /**
     * returns the river card. if the river hasn't come out yet, returns null
     */ 
    public Card getRiver()
    {
        return river;
    }

}
