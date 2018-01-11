/**
 *  IClientSocketListener - Defines an interface for classes which will
 *    receive events from the Client socket.
 *  Copyright (C) 2008 Conrad Meyer <konrad@tylerc.org>
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
 */

package poker.networking;

public interface IClientSocketListener
{
  public void onBeginGame(IMessage msg);
  public void onBeginTurn(IMessage msg);
  public void onInvalidMove(IMessage msg);
  public void onEndGame(IMessage msg);
}
