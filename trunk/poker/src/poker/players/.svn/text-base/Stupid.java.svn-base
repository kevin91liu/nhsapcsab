/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.players;

import poker.engine.*;

/**
 *
 * @author t-shawnx
 */
public class Stupid extends Player
{
    public Stupid(String name)
    {
        super(name);
    }

    @Override
    public Action beginTurn(GameInfo gi) 
    {
        Money bet = new Money(1, Money.Currency.DOLLARS);
        
        Action a = new Raise(gi.getId(this), bet);
        if (gi.isValid(a))
            return a;
        else if (gi.isValid(a = new Call(gi.getId(this))))
        {
            return a;
        }
        else return new Check(gi.getId(this));
    }

    @Override
    public void beginRound(GameInfo gi) 
    {
        
    }

    @Override
    public void endRound(GameInfo gi) 
    {
        
    }

    @Override
    public void acceptHand(Hand h)
    {
        
    }

}
