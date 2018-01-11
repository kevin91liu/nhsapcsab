/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.engine;

/**
 *
 * @author t-shawnx
 *
 * this class will double for the Bet action in poker. a player can either use raise for an initial bet
 * or raise even more after a previous player has raised/betted
 */
public class Raise extends Action
{
        private Money amount;
        public Raise(Integer playerId, Money _amount)
        {
                super(playerId);
                amount = _amount;
        }
        
        public Money getAmount()
        {
            return amount;
        }
        
    @Override
        public String toString()
        {
            return getName(this.getPlayerId()) + 
                   " " + this.getPlayerId() + 
                   " Raised by $" + amount.getAmount();
        }
}
