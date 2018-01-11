/**
 *  MessageSocket - Base class for message-passing sockets.
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

import java.lang.Runnable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSocket implements IMessageSocket, Runnable
{
  Socket socket;
  ObjectOutputStream out;
  Queue<IMessage> messageQueue;

  public MessageSocket(Socket socket) throws IOException
  {
    this.socket = socket;
    this.out = new ObjectOutputStream(socket.getOutputStream());
    this.messageQueue = new LinkedList<IMessage>();
    new Thread(this).start();
  }

  public void send(IMessage msg)
  {
        try {
            out.writeUnshared(msg);
        } catch (IOException ex) {
            Logger.getLogger(MessageSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
  }

  public IMessage peek()
  {
    synchronized(this)
    {
      return messageQueue.peek();
    }
  }

  public IMessage poll()
  {
    synchronized(this)
    {
      return messageQueue.poll();
    }
  }

  public void run()
  {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            for (;;) {
                if (socket.isClosed()) {
                    break;
                }
                Object o = null;
                try {
                    o = in.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MessageSocket.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (o instanceof IMessage) {
                    synchronized (this) {
                        messageQueue.add((IMessage) o);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageSocket.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MessageSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
  }
}
