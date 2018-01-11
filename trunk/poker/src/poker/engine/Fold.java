/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.engine;

/**
 *
 * @author t-shawnx
 */
public class Fold extends Action
{
        public Fold(Integer playerId)
        {
                super(playerId);
        }
        
    @Override
        public String toString()
        {
            return getName(this.getPlayerId()) + 
                   " " + this.getPlayerId() + " Folded.";
        }
}
