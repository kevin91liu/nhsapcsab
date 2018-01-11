package poker.engine;
import java.io.IOException;

import poker.players.*;

public class Main
{
    
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
    {
        Player a = new Stupid("stupid 1");
        Player b = new Stupid("stupid 2");
        Game g = new Game(a, b);
        g.begin();
        
    }
    
}
