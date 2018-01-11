/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.engine;

/**
 *
 * @author t-shawnx
 */
public class Check extends Action
{
        public Check(Integer playerId)
        {
                super(playerId);
        }
        
    @Override
        public String toString()
        {
            return getName(this.getPlayerId()) + 
                   " " + this.getPlayerId() + " Checked.";
        }
}
