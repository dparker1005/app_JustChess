package com.alaskalinuxuser.justchess;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests validating game modes
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameModeTest {
    protected String mValidBoard = "RNBQKBNRPPPPPPPP********************************pppppppprnbqkbnr";

    //Makes sure valid boards are accepted
    @Test
    public void acceptsValidBoards() {
        assertTrue(GameMode.isValidBoard(mValidBoard));
    }

    //Makes sure valid boards that are too short or too long are not valid
    @Test
    public void deniesBoardCauseLength() {
        assertFalse(GameMode.isValidBoard(mValidBoard.substring(0, mValidBoard.length()-1)));
        assertFalse(GameMode.isValidBoard(mValidBoard.substring(1)));
    }

    //Makes sure valid boards that are too short or too long are not valid
    @Test
    public void deniesBoardCauseInvalidChar() {
        assertFalse(GameMode.isValidBoard(mValidBoard.replace('q', 'j')));
    }

    //Makes sure valid boards that are too short or too long are not valid
    @Test
    public void deniesBoardCauseNumKings() {
        assertFalse(GameMode.isValidBoard(mValidBoard.replace('k', 'r')));
        assertFalse(GameMode.isValidBoard(mValidBoard.replace('K', 'R')));
        assertFalse(GameMode.isValidBoard(mValidBoard.replace('q', 'k')));
        assertFalse(GameMode.isValidBoard(mValidBoard.replace('Q', 'K')));
    }

    //Makes sure valid modes can be created
    @Test
    public void canCreateValidMode() {
        boolean gameModeLegal = true;
        try {
            GameMode gameMode = new GameMode("Test Mode", "Test Description", mValidBoard);
            if ( ! ( gameMode.getName().equals("Test Mode")
            && gameMode.getDescription().equals("Test Description")
            && gameMode.getNewBoard().equals(mValidBoard) ) ) {
                gameModeLegal = false;
            }
        } catch ( IllegalArgumentException e ) {
            gameModeLegal = false;
        }
        assertTrue(gameModeLegal);
    }

    //Makes sure invalid boards throw exceptions during GameMode creation
    @Test
    public void cannotCreateInvalidMode() {
        boolean exceptionCaught = false;
        try {
            GameMode gameMode = new GameMode("Test Mode", "Test Description", mValidBoard.substring(1));
        } catch ( IllegalArgumentException e ) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

    //Makes sure engine can be started with custom mode
    @Test
    public void startGameWithMode() {
        String boardWithExtraPawns = mValidBoard.replace('*', 'p');
        TheEngine.terminal("newGame." + boardWithExtraPawns);
        assertTrue(TheEngine.stringBoard.equals(boardWithExtraPawns));
        TheEngine.terminal("newGame." + mValidBoard);
        assertTrue(TheEngine.stringBoard.equals(mValidBoard));
    }
}
