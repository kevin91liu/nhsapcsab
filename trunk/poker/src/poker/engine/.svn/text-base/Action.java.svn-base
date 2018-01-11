/**
 * $Id$
 * @author kevin liu (kevin91liu@gmail.com)
 * @author shawn xu
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

import java.util.HashMap;

public abstract class Action 
{
	private Integer id; //the player whose action this is
	
	public Action(Integer _playerId)
	{
		id = _playerId;
	}
	
        public Integer getPlayerId()
        {
            return id;
        }
        
    @Override
        public abstract String toString();
        
        private static boolean initialized = false;
        
        private static HashMap<Integer, String> idToName;
        
        static void initialize(HashMap<Integer, String> _idToName)
        {
            if (initialized) return;
            
            idToName = _idToName;
            
            initialized = true;
        }
        
        protected String getName(Integer id)
        {
            return idToName.get(id);
        }
}

