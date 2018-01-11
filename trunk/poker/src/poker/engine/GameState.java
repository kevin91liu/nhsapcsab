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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class GameState 
{
        public enum State
        {
            BEGIN,
            ANTE,
            DEAL, 
            FIRSTBET, 
            FLOP, 
            SECONDBET, 
            TURN, 
            THIRDBET, 
            RIVER, 
            FINALBET,
            ENDROUND                               
        }
    
        private HashMap<Integer, Money> bets; //how much each player has betted
        private HashMap<Integer, Money> stash; // how much each player has
        private HashMap<Player, Integer> player_to_id;
        private HashMap<Integer, Player> id_to_player;
        private Money potValue; //the value of the pot
        private Money minimumBid; //within a given round, the amount you have betted that round 
        // + the additional amount you need to put in must be >= minimumBid
        private State state;
        private LinkedList<Action> actions; //history of actions within a single round
        private HashMap<Player, Hand> privateHands; //each player's hand of 2 cards
        private Deck theDeck;
        private int dealerIdx; //to keep track of the player that is the "dealer" so we know who should bet first
        private ArrayList<Player> activePlayers; //players in the round who are still competing for the pot
        private Set<Player> out; //players who have no more money and can not play anymore
        private Set<Player> folded; //players who have folded in a round but still have chips left to play
        private Hand board; //the face up cards on the table   
        private IDisplay display;
        private LinkedList<Player> lastWinners;
        private Card turn;
        private Card river;
        
        /*************************
         *       Constructor
         *************************/
        
        public GameState(Player[] _players, IDisplay _display)
        {
            
            activePlayers = new ArrayList<Player>();
            for (Player p : _players)                
                activePlayers.add(p);                
            
            dealerIdx = 0;
            
            out = new HashSet<Player>();
            folded = new HashSet<Player>();
            
            // each player is assigned an id based on the order they were added
            player_to_id = new HashMap<Player, Integer>();
            for (int i = 0; i < activePlayers.size(); i++)                
                player_to_id.put(activePlayers.get(i), i);
            
            id_to_player = new HashMap<Integer, Player>();
            for (Player p : player_to_id.keySet())            
                id_to_player.put(player_to_id.get(p), p);
            stash = new HashMap<Integer, Money>();
            bets = new HashMap<Integer, Money>();
            for (Integer i : id_to_player.keySet())
            {
                stash.put(i, GameConstants.STARTSTASH);
                bets.put(i, GameConstants.ZERO);
            }
            potValue = GameConstants.ZERO;
            state = State.ANTE;
            actions = new LinkedList<Action>();
            board = new Hand(5);
            theDeck = new Deck();
            display = _display;          
            updateMinimum(GameConstants.ZERO);
            
            HashMap<Integer, String> idToName = new HashMap<Integer, String>();
            for (Integer i : id_to_player.keySet())            
                idToName.put(i, id_to_player.get(i).toString());
            Action.initialize(idToName);
            
            turn = null;
            river = null;
        }                       
        
        /*************************
         * Methods used by engine
         *************************/
        
        public void updateState(State s)
        {
            state = s;
        }
        
        public void beginRound()
        {            
            for (Player p : activePlayers)                          
                p.beginRound(getInfo(false));            
            display.beginRound(this);
        }
        
        public void ante()
        {                    
            display.preAnte(this);
            for (int i = 0; i < activePlayers.size(); i++)        
            {
            	if (!allocateMoney(getId(activePlayers.get(i)), GameConstants.ANTE))
            	{ //if they can't pay ante, they're out
            		takeOutPlayer(activePlayers.get(i));
            	}
            }
            // clear the bets
            for (Integer id : bets.keySet())            
                bets.put(id, GameConstants.ZERO); 
            display.postAnte(this);
        }
        
        public void dealOnce()
        {                   
            display.preDealOnce(this);
            int temp = 0;
            for (int i = (dealerIdx+1)%activePlayers.size(); 
                temp < activePlayers.size(); i = ++i % activePlayers.size())
            {
                long card = theDeck.drawCardLong();
                privateHands.get(activePlayers.get(i)).add(card);
                temp++;
            }
            display.postDealOnce(this);          
        }
        
        public void giveOutCards()
        {
            for (Player p : activePlayers)                
                p.acceptHand(privateHands.get(p).clone());    
            display.cardsDealtToPlayers(this, privateHands);
        }
        
        public void bettingRound(GameState.State state)
        {                      
            updateMinimum(GameConstants.ZERO); 
            updateState(state);
            display.preBettingRound(state, this);                                   
            bet(dealerIdx, false);     
            // clear the bets
            for (Integer id : bets.keySet())            
                bets.put(id, GameConstants.ZERO); 
            display.betsClearedFromTable(this);
        }
        
        public void addCardToBoard()
        {            
            Card card;
            board.add(card = new Card(theDeck.drawCardLong()));
            if (State.TURN.equals(state))
            {
                turn = card;
            }
            else if (State.RIVER.equals(state))
            {
                river = card;
            }
            display.cardAddedToBoard(this, card);
        }
        
        public void burnCard()
        {
            theDeck.drawCardLong();
            display.cardBurnt(this);
        }
        
        public void showHands()
        {
            updateState(State.ENDROUND);
            LinkedList<Player> winners = new LinkedList<Player>();
            if (!onePlayerLeft())
            {
                Hand bestHand= null;
                for (Player p : activePlayers)
                {
                    if (winners.isEmpty())
                    {
                        winners.add(p);
                        bestHand = Hand.getHighestHand(privateHands.get(p), board);
                        continue;
                    }
                    Hand pHand = Hand.getHighestHand(privateHands.get(p), board);
                    int compare = pHand.compareTo(bestHand);
                    if (compare > 0)
                    {
                        winners.clear();
                        winners.add(p);
                    }
                    else if (compare == 0)
                    {
                        winners.add(p);
                    }
                }
            }
            else
            {
                winners.add(activePlayers.get(0));
            }
            // now that we know who won
            emptyBets();
            Money winnings = potValue.divide(winners.size());
            // pay the winner
            for (Player winner : winners)
            {
                pay(getId(winner), winnings);
            }
            lastWinners = winners;
            for (Player p : activePlayers)
            {
            	p.endRound(getInfo(true)); 
            }
            for (Player p : folded)
            {
            	p.endRound(getInfo(true)); 
            }
            //not calling endRound on players that are out even if they are "spectating"
            //because: 1) i don't see much justification in that right now and
            // 2) they're out anyways so meh =\  3) we can add the feature in if necessary
            potValue = GameConstants.ZERO;
            // clear the minimum bet
            updateMinimum(GameConstants.ZERO);
            display.roundEnded(this);                   
        }
        
        public void reset()
        {
            for (Player p : folded)            
                activePlayers.add(p);  
            folded.clear();
            
            for (int i = 0; i < activePlayers.size(); i++)
            {
                if (stash.get(getId(activePlayers.get(i))).getAmount() < GameConstants.ANTE.getAmount())
                    takeOutPlayer(activePlayers.get(i--));
            }
            // reset hands
            privateHands = new HashMap<Player, Hand>();
            for (Player curP : activePlayers)		
                privateHands.put(curP, new Hand((byte)2));
            
            theDeck.reset();             
            // appoint the next player to be the dealer
            dealerIdx = ++dealerIdx % activePlayers.size(); 
            
            // reset the table
            board.clear();
            // reset the action chain
            actions.clear();
            turn = null;
            river = null;
        }                
        
        public boolean onePlayerLeft()
        {
            return activePlayers.size() == 1;
        }                                  
        
        
        public LinkedList<Player> getLastWinners()
        {
            return lastWinners;
        }
        
        /*************************
         * Private Methods
         *************************/
        
        private Integer getId(Player p)
        {
            return player_to_id.get(p);
        }
               
        private void addAction(Action a)
        {
            actions.add(a);
        }
                
	private void updateMinimum(Money minimum)
        {
            minimumBid = minimum;
        }
                        
        private boolean allocateMoney(Integer id, Money amount)
        {            
            Money before = stash.get(id);
                
            if (before.getAmount() < amount.getAmount())
                return false;
                        
            bets.put(id, bets.get(id).add(amount));            
            stash.put(id, before.subtract(amount));                                
            potValue = potValue.add(amount); 
            return true;                
        }                
                                  
        private void takeOutPlayer(Player p)
        {
            activePlayers.remove(p);
            out.add(p);
            Integer id = player_to_id.get(p);
            stash.put(id, null);
            bets.put(id, null);
            display.displayMessage("Player " + p.toString() + " " + player_to_id.get(p) + " is out.");
        }
        
        // this will recursive call until no one raises for a complete loop
        // will return false if only 1 player remains, therefore end prematurely
        // else it will return true
        private void bet(int raiser, boolean raised)
        {            
            int count = 0;       
            Action a = null;
            for (int i = (raiser + 1) % activePlayers.size();
                count < (raised ? activePlayers.size() - 1 : activePlayers.size()); 
                i = ((a instanceof Fold) ? i : ++i) % activePlayers.size())
            {                        
                count++;
                Player p = activePlayers.get(i);
                a = p.beginTurn(getInfo(false));
                if (!isValid(a))   
                {
                    display.displayMessage("Player " + p.toString() 
                            + " "+ getId(p) + ": Action is invalid: " + a.toString());
                    a = new Fold(getId(p));                
                }              
                executeAction(a);
                display.actionPerformed(this, p, a);
                
                if (activePlayers.size() == 1)
                    return;
                
                // if someone raised, then we must go into a new betting loop
                if (a instanceof Raise)
                {
                    bet(activePlayers.indexOf(p), true);
                    return;
                }
            }
        }
        
        public boolean isValid(Action a)
        {
        	if (a == null)
        		return false;
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
                
        private void executeAction(Action a) 
        {
            if (a instanceof Call)
            {                               
                Money curStash = getStash(a.getPlayerId());
                // all in
                if (minimumBid.getAmount() > curStash.getAmount())                                    
                    allocateMoney(a.getPlayerId(), curStash);                
                else                
                    allocateMoney(a.getPlayerId(), minimumBid.subtract(bets.get(a.getPlayerId())));                                  
            } 
            else if (a instanceof Fold)
            {
                Player p = getPlayer(a.getPlayerId());
                activePlayers.remove(p);
                folded.add(p);                       
            }
            else if (a instanceof Raise)
            {
                Money amount = ((Raise)a).getAmount();               
                // update the minimum
                updateMinimum(minimumBid.add(amount)); 
                 // allocate the money
                allocateMoney(a.getPlayerId(), minimumBid.subtract(bets.get(a.getPlayerId())));                 
            }
            addAction(a);
        }
        
        private Money getStash(Integer id)
        {
            return stash.get(id);
        }
                
        private void pay(Integer winner, Money amount)
        {
            stash.put(winner, stash.get(winner).add(amount));
        }	
		        
        private void emptyBets()
        {
            for (Integer id : bets.keySet())            
                bets.put(id, GameConstants.ZERO);
        }
        
        public Player getPlayer(Integer id)
        {
            return id_to_player.get(id);
        }
              
        /*************************
         *       Accessors
         *************************/
        
        public Hand getHand(Player p)
        {
            return privateHands.get(p);
        }
        
        @SuppressWarnings("unchecked")
		public GameInfo getInfo(boolean endOfRound)
        {
            Set<Integer> active = new HashSet<Integer>();
            Set<Integer> _folded = new HashSet<Integer>();
            Set<Integer> losers = new HashSet<Integer>();
            
            for (Player p : activePlayers)            
                active.add(getId(p));
            
            for (Player p : this.folded)            
                _folded.add(getId(p));
            
            for (Player p : this.out)            
                losers.add(getId(p));
            
            if (endOfRound)
            	return new GameInfo(
                        board.getCards(),
                        (HashMap<Integer, Money>) bets.clone(),
                        (HashMap<Integer, Money>) stash.clone(),
                        (HashMap<Player, Integer>) player_to_id.clone(),
                        (HashMap<Integer, Player>) id_to_player.clone(),
                        potValue,
                        minimumBid,
                        state,
                        actions,
                        active,
                        _folded,
                        losers,
                        theDeck.size(),
                        turn,
                        river,
                        lastWinners
                        );
            //note to self: lastWinners linkedlist is exposed here instead of being cloned
            //so make sure in GameInfo it is not accessible
            
            return new GameInfo(
                board.getCards(),
                (HashMap<Integer, Money>) bets.clone(),
                (HashMap<Integer, Money>) stash.clone(),
                (HashMap<Player, Integer>) player_to_id.clone(),
                (HashMap<Integer, Player>) id_to_player.clone(),
                potValue,
                minimumBid,
                state,
                actions,
                active,
                _folded,
                losers,
                theDeck.size(),
                turn,
                river
                );
        }                                        
        	
}

