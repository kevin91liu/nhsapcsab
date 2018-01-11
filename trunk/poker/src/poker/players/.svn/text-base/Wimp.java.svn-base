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
public class Wimp extends Player
{
    public Wimp(String name)
    {
        super(name);
    }

    @Override
    public Action beginTurn(GameInfo gi) 
    {        
        return new Fold(gi.getId(this));
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
