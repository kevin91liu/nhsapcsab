/**
 *  ClientMessageSocket - A class to represent the client side of socket
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
import poker.engine.Money;

import java.net.Socket;

public final class ClientMessageSocket extends PokerMessageSocket
{
  private IClientSocketListener listener;

  public ClientMessageSocket(Socket sock, IClientSocketListener listener) throws IOException
  {
    super(sock);
    this.listener = listener;
  }

  public final void sendCheck()
  {
    send(new Message(CHECK));
  }

  public final void sendFold()
  {
    send(new Message(FOLD));
  }

  public final void sendRaise(Money amt)
  {
    send(new Message(RAISE, amt));
  }

  protected final void onGoodbye(IMessage msg)
  {
    // disconnect
  }

  protected final void onBeginGame(IMessage msg)   { listener.onBeginGame(msg); }
  protected final void onBeginTurn(IMessage msg)   { listener.onBeginTurn(msg); }
  protected final void onInvalidMove(IMessage msg) { listener.onInvalidMove(msg); }
  protected final void onEndGame(IMessage msg)     { listener.onEndGame(msg); }

  // Server shouldn't send us these
  protected final void onCheck(IMessage msg) { invalidMessage(msg); }
  protected final void onRaise(IMessage msg) { invalidMessage(msg); }
  protected final void onFold(IMessage msg)  { invalidMessage(msg); }
}
