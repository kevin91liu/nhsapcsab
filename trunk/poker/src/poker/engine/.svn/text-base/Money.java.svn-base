/**
 * @author kevin liu (kevin91liu@gmail.com)
 * @author shawn xu
 * @author wilson lee
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

package poker.engine;

import poker.gui.IDeflatable;


public class Money implements IDeflatable<Money>
{
	private double amount;
	private Currency type;
	
	public Money(double _amount, Currency _type)
	{
		amount = _amount;
		type = _type;
	}
	
	//public Money()
	//{
		//this(42, Currency.CHIPS);
	//}
	
	/**
	 * we can later add different types such as chips or other currencies, if desired
	 *
	 */
	public enum Currency
	{
		DOLLARS, CHIPS
	}

	public double getAmount() 
	{
		return amount;
	}

	public Currency getType() 
	{
		return type;
	}
        
    public Money add(Money m)
    {
        if (m.type != this.type)
            throw new IllegalArgumentException("Must be same type");
        return new Money(this.amount + m.amount, this.type);
    }
    
    public Money subtract(Money m)
    {
        if (m.type != this.type)
            throw new IllegalArgumentException("Must be same type");
        return new Money(this.amount - m.amount, this.type);
    }
    
    public Money divide(int splits)
    {
        return new Money(this.amount/splits, this.type);
    }
    
    public boolean equals(Object x)
    {
    	if (!(x instanceof Money)) return false;
    	Money m = (Money)x;
    	return m.getAmount() == getAmount() && m.getType() == getType();
    }

    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
        hash = 97 * hash + (this.type.ordinal() % 2 == 0 ? this.type.hashCode() : 0);
        return hash;
    }
    
    public String toString()
    {
    	return String.format("%f %s", getAmount(), getType().toString());
    }

    public String deflate() 
    {
    	String result = "" + Integer.toHexString(getType().ordinal());
    	result += Double.toHexString(getAmount());
        return result;
    }

    public Money inflate(String string) throws Exception 
    {
    	int ord = Integer.parseInt(string.substring(0, 1), 16);
        Currency currency = Currency.values()[ord];
        double _amount = Double.parseDouble(string.substring(1));
        return new Money(_amount, currency);
    }
}

