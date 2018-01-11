/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.engine;

import java.util.HashMap;

/**
 *
 * @author t-shawnx
 */
public interface IDisplay 
{
    void displayMessage(String s);
    void beginRound(GameState state);
    void preAnte(GameState state);
    void postAnte(GameState state);
    void preDealOnce(GameState state);
    void postDealOnce(GameState state);
    void cardsDealtToPlayers(GameState state, HashMap<Player, Hand> hands);
    void preBettingRound(GameState.State round, GameState state);
    void betsClearedFromTable(GameState state);
    void cardAddedToBoard(GameState state, Card card);
    void cardBurnt(GameState state);
    void roundEnded(GameState state);
    void actionPerformed(GameState state, Player p, Action a);
}
