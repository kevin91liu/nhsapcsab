/**
 *  PokerMessageSocket - A class to represent socket connections in the
 *    poker game.
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

import poker.engine.Hand;
import poker.engine.Money;

import java.io.IOException;
import java.net.Socket;

public abstract class PokerMessageSocket extends MessageSocket
{
  // both
  public static final String HELLO          = "HELO";
  public static final String GOODBYE        = "BYE";

  // server
  public static final String NEWGAME        = "NEW";
  public static final String YOURTURN       = "YOURTURN";
  public static final String INVALIDMOVE    = "INVALIDMOVE";
  public static final String ENDGAME        = "END";

  // client
  public static final String CHECK          = "CHECK";
  public static final String RAISE          = "RAISE";
  public static final String FOLD           = "FOLD";

  protected PokerMessageSocket(Socket sock) throws IOException
  {
    super(sock);
  }

  // send commands

  public final void sendHello()       { send(new Message(HELLO)); }
  public final void sendGoodbye()     { send(new Message(GOODBYE)); }

  // process incoming queue
  
  public final void processMessages()
  {
    IMessage msg;
    while ((msg = poll()) != null)
      handleMessage(msg);
  }

  public final void handleMessage(IMessage msg)
  {
    if      (msg.getName().equals(HELLO))       onHello(msg);
    else if (msg.getName().equals(GOODBYE))     onGoodbye(msg);
    else if (msg.getName().equals(NEWGAME))     onBeginGame(msg);
    else if (msg.getName().equals(YOURTURN))    onBeginTurn(msg);
    else if (msg.getName().equals(INVALIDMOVE)) onInvalidMove(msg);
    else if (msg.getName().equals(ENDGAME))     onEndGame(msg);
    else if (msg.getName().equals(CHECK))       onCheck(msg);
    else if (msg.getName().equals(RAISE))       onRaise(msg);
    else if (msg.getName().equals(FOLD))        onFold(msg);
  }

  // incoming message events -- overridden by ClientSocket and
  // ServerSocket

  protected final void onHello(IMessage msg) { /* do nothing */ }
  protected abstract void onGoodbye(IMessage msg);
  protected abstract void onBeginGame(IMessage msg);
  protected abstract void onBeginTurn(IMessage msg);
  protected abstract void onInvalidMove(IMessage msg);
  protected abstract void onEndGame(IMessage msg);
  protected abstract void onCheck(IMessage msg);
  protected abstract void onRaise(IMessage msg);
  protected abstract void onFold(IMessage msg);

  protected final void invalidMessage(IMessage msg)
  {
    throw new RuntimeException(getClass().getName() + " can not handle Message '" + msg + "'");
  }
}
