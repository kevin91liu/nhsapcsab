/**
 *  ServerMessageSocket - A class to represent the server side of socket
 *    connections in the poker game.
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

import java.io.IOException;
import poker.engine.Hand;
import poker.engine.Money;

import java.net.Socket;

public final class ServerMessageSocket extends PokerMessageSocket
{
  private IServerSocketListener listener;

  public ServerMessageSocket(Socket sock, IServerSocketListener listener) throws IOException
  {
    super(sock);
    this.listener = listener;
  }

  public final void sendInvalidMove()
  {
    send(new Message(INVALIDMOVE));
  }

  public final void sendEndGame()
  {
    send(new Message(ENDGAME));
  }
  
  public final void sendNewGame(Hand hand)
  {
    send(new Message(NEWGAME, hand));
  }
  
  public final void sendYourTurn()
  {
    send(new Message(YOURTURN));
  }
  
  public final void sendYourTurn(Money amt)
  {
    send(new Message(YOURTURN, amt));
  }

  protected final void onGoodbye(IMessage msg)
  {
    // disconnect
  }
  
  protected final void onCheck(IMessage msg) { listener.onCheck(msg); }
  protected final void onRaise(IMessage msg) { listener.onRaise(msg); }
  protected final void onFold(IMessage msg)  { listener.onFold(msg);  }

  // Client shouldn't send us these
  protected final void onBeginGame(IMessage msg)   { invalidMessage(msg); }
  protected final void onBeginTurn(IMessage msg)   { invalidMessage(msg); }
  protected final void onInvalidMove(IMessage msg) { invalidMessage(msg); }
  protected final void onEndGame(IMessage msg)     { invalidMessage(msg); }
}
