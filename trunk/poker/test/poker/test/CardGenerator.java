/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.test;
import poker.engine.*;
import java.util.Random;

/**
 *
 * @author Shawn Xu
 */
public class CardGenerator 
{
    private Card ref = new Card(Card.Suit.CLUBS, Card.ACE);
    private Random rand = new Random();
    public static long[][] pairs =
    {
        {
            0x0000000000000003L,
            0x0000000000000005L,
            0x0000000000000009L,
            0x0000000000000006L,
            0x000000000000000aL,
            0x000000000000000cL,
        },
        {
            0x0000000000000030L,
            0x0000000000000050L,
            0x0000000000000090L,
            0x0000000000000060L,
            0x00000000000000a0L,
            0x00000000000000c0L,
        },
        {
            0x0000000000000300L,
            0x0000000000000500L,
            0x0000000000000900L,
            0x0000000000000600L,
            0x0000000000000a00L,
            0x0000000000000c00L,
        },
        {
            0x0000000000003000L,
            0x0000000000005000L,
            0x0000000000009000L,
            0x0000000000006000L,
            0x000000000000a000L,
            0x000000000000c000L,

        },
        {
            0x0000000000030000L,
            0x0000000000050000L,
            0x0000000000090000L,
            0x0000000000060000L,
            0x00000000000a0000L,
            0x00000000000c0000L,
        },
        {
            0x0000000000300000L,
            0x0000000000500000L,
            0x0000000000900000L,
            0x0000000000600000L,
            0x0000000000a00000L,
            0x0000000000c00000L,
        },
        {
            0x0000000003000000L,
            0x0000000005000000L,
            0x0000000009000000L,
            0x0000000006000000L,
            0x000000000a000000L,
            0x000000000c000000L,
        },
        {
            0x0000000030000000L,
            0x0000000050000000L,
            0x0000000090000000L,
            0x0000000060000000L,
            0x00000000a0000000L,
            0x00000000c0000000L,
        },
        {
            0x0000000300000000L,
            0x0000000500000000L,
            0x0000000900000000L,
            0x0000000600000000L,
            0x0000000a00000000L,
            0x0000000c00000000L,
        },
        {
            0x0000003000000000L,
            0x0000005000000000L,
            0x0000009000000000L,
            0x0000006000000000L,
            0x000000a000000000L,
            0x000000c000000000L,
        },
        {
            0x0000030000000000L,
            0x0000050000000000L,
            0x0000090000000000L,
            0x0000060000000000L,
            0x00000a0000000000L,
            0x00000c0000000000L,
        },
        {
            0x0000300000000000L,
            0x0000500000000000L,
            0x0000900000000000L,
            0x0000600000000000L,
            0x0000a00000000000L,
            0x0000c00000000000L,
        },
        {
            0x0003000000000000L,
            0x0005000000000000L,
            0x0009000000000000L,
            0x0006000000000000L,
            0x000a000000000000L,
            0x000c000000000000L,
        },        
    };
    
    public static long[][] triples =
    {
        {
            0x0000000000000007L,
            0x000000000000000bL,
            0x000000000000000dL,
            0x000000000000000eL,
        },
        {
            0x0000000000000070L,
            0x00000000000000b0L,
            0x00000000000000d0L,
            0x00000000000000e0L,
        },
        {
            0x0000000000000700L,
            0x0000000000000b00L,
            0x0000000000000d00L,
            0x0000000000000e00L,
        },
        {
            0x0000000000007000L,
            0x000000000000b000L,
            0x000000000000d000L,
            0x000000000000e000L,
        },
        {
            0x0000000000070000L,
            0x00000000000b0000L,
            0x00000000000d0000L,
            0x00000000000e0000L,
        },
        {
            0x0000000000700000L,
            0x0000000000b00000L,
            0x0000000000d00000L,
            0x0000000000e00000L,
        },
        {
            0x0000000007000000L,
            0x000000000b000000L,
            0x000000000d000000L,
            0x000000000e000000L,
        },
        {
            0x0000000070000000L,
            0x00000000b0000000L,
            0x00000000d0000000L,
            0x00000000e0000000L,
        },
        {
            0x0000000700000000L,
            0x0000000b00000000L,
            0x0000000d00000000L,
            0x0000000e00000000L,
        },
        {
            0x0000007000000000L,
            0x000000b000000000L,
            0x000000d000000000L,
            0x000000e000000000L,
        },
        {
            0x0000070000000000L,
            0x00000b0000000000L,
            0x00000d0000000000L,
            0x00000e0000000000L,
        },
        {
            0x0000700000000000L,
            0x0000b00000000000L,
            0x0000d00000000000L,
            0x0000e00000000000L,
        },
        {
            0x0007000000000000L,
            0x000b000000000000L,
            0x000d000000000000L,
            0x000e000000000000L,
        }
    };
    
    public static long[][] highcards =
    {
        {
            0x0000000000000001L,
            0x0000000000000002L,
            0x0000000000000004L,
            0x0000000000000008L,
        },
        {
            0x0000000000000010L,
            0x0000000000000020L,
            0x0000000000000040L,
            0x0000000000000080L,
        },
        {
            0x0000000000000100L,
            0x0000000000000200L,
            0x0000000000000400L,
            0x0000000000000800L,
        },
        {
            0x0000000000001000L,
            0x0000000000002000L,
            0x0000000000004000L,
            0x0000000000008000L,
        },
        {
            0x0000000000010000L,
            0x0000000000020000L,
            0x0000000000040000L,
            0x0000000000080000L,
        },
        {
            0x0000000000100000L,
            0x0000000000200000L,
            0x0000000000400000L,
            0x0000000000800000L,
        },
        {
            0x0000000001000000L,
            0x0000000002000000L,
            0x0000000004000000L,
            0x0000000008000000L,
        },
        {
            0x0000000010000000L,
            0x0000000020000000L,
            0x0000000040000000L,
            0x0000000080000000L,
        },
        {
            0x0000000100000000L,
            0x0000000200000000L,
            0x0000000400000000L,
            0x0000000800000000L,
        },
        {
            0x0000001000000000L,
            0x0000002000000000L,
            0x0000004000000000L,
            0x0000008000000000L,
        },
        {
            0x0000010000000000L,
            0x0000020000000000L,
            0x0000040000000000L,
            0x0000080000000000L,
        },
        {
            0x0000100000000000L,
            0x0000200000000000L,
            0x0000400000000000L,
            0x0000800000000000L,
        },
        {
            0x0001000000000000L,
            0x0002000000000000L,
            0x0004000000000000L,
            0x0008000000000000L,
        }
    };
    
    public long[] generatePairs2(int value)
    {
        long[] ret = new long[6];
        int index = 0;
        int i = (4 * (value - 2));
        int max = i + 4;
        for (; i < max - 1; i++)        
            for (int j = i + 1; j < max; j++)            
                ret[index++] = (1L<<i) | (1L<<j);
                  
        return ret;
    }
    
    public long[] generatePairs(int value)
    {
        return pairs[value - 2];
    }
    
    public long[] generateTriples2(int value)
    {
        long[] ret = new long[4];
        int index = 0;
        int i = (4 * (value - 2));
        int max = i + 4;
        for (; i < max - 2; i++)        
            for (int j = i + 1; j < max - 1; j++)   
                for (int k = j + 1; k < max; k++)
                    ret[index++] = (1L<<i) | (1L<<j) | (1L<<k);
                  
        return ret;
    }
    
    public long[] generateTriples(int value)
    {
        return triples[value - 2];
    }
    
    public long generateFours(int value)
    {                        
        return ref.getValueMask((byte) value);
    }
    
    public long[] generateFullHouse(int triple, int pair)
    {
        long[] ret = new long[24];
        long[] _triples = generateTriples(triple);
        long[] _pairs = generatePairs(pair);
        int index = 0;
        for (long t : _triples)
            for (long p : _pairs)
                ret[index++] = t | p;
        return ret;
    }
    
    public long[] generateStraights(int start)
    {
        long[] ret = new long[1020];
        long[] n1s;
        long[] n2s;
        long[] n3s;
        long[] n4s;
        long[] n5s;
        int index = 0;
        if (start == 14)
        {
            n1s = generateHighCards(start);
            n2s = generateHighCards(2);
            n3s = generateHighCards(32);
            n4s = generateHighCards(4);
            n5s = generateHighCards(5);  
        }
        else
        {            
            n1s = generateHighCards(start);
            n2s = generateHighCards(start+1);
            n3s = generateHighCards(start+2);
            n4s = generateHighCards(start+3);
            n5s = generateHighCards(start+4);            
        }        
        for (int i = 0; i < 4; i++)
        {                
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    for (int l = 0; l < 4; l++)
                    {
                        for (int m = 0; m < 4; m++)
                        {
                            if ((i == j) && (i == k) && (i == l) && (i == m)) continue;
                            ret[index++] = n1s[i]|n2s[j]|n3s[k]|n4s[l]|n5s[m];
                        }
                    }
                }
            }
        }
        System.out.println(index);
        return ret;
    }
    
    public long[] generateHighCards2(int value)
    {
        long[] ret = new long[4];
        long cards = generateFours(value);
        int index = 0; 
        while (cards != 0L)
        {
            ret[index] = cards & -cards;
            cards ^= ret[index++];
        }
        return ret;
    }
    
    public long[] generateHighCards(int value)
    {        
        return highcards[value];
    }
    
    public long generateDebris(long cards, int value)
    {        
        long ret = cards;
        int needed = 5 - (Long.bitCount(cards));
        long pool = 0xFFFFFFFFFFFFFFFFL;
        pool ^= ref.getValueMask((byte) value);        
        for (int i = 0; i < needed; i ++)
        {
            long aCard;
            int shift;
            do 
            {
                shift = rand.nextInt(52);
                aCard = 0x1L;
                aCard <<= shift;
            }
            while ((pool & aCard) == 0L);
            ret |= aCard;
            pool ^= ref.getValueMask((byte) ((shift / 4) + 2));
        }
        return ret;
    }    
    
}
