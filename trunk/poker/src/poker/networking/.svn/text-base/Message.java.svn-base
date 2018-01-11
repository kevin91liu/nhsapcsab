/**
 *  Message - Class for messages which get passed between client and server.
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

import poker.gui.IDeflatable;

public class Message implements IMessage
{
  String name;
  IDeflatable value;

  public Message(String name, IDeflatable value)
  {
    this.name = name;
    this.value = value;
  }

  public Message(String name)
  {
    this.name = name;
    this.value = null;
  }

  public String getName()
  {
    return name;
  }

  public IDeflatable getValue()
  {
    return value;
  }

    @Override
  public String toString()
  {
    return name + " " + value;
  }

   
}
