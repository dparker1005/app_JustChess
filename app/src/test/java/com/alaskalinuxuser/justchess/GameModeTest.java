package com.alaskalinuxuser.justchess;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests validating game modes
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameModeTest {
    protected String validBoard = "RNBQKBNRPPPPPPPP********************************pppppppprnbqkbnr";

    //Makes sure valid boards are accepted
    @Test
    public void acceptsValidBoards() {
        assertTrue(GameMode.isValidBoard(validBoard));
    }

    //Makes sure valid boards that are too short or too long are not valid
    @Test
    public void deniesBoardCauseLength() {
        assertFalse(GameMode.isValidBoard(validBoard.substring(0, validBoard.length()-1)));
        assertFalse(GameMode.isValidBoard(validBoard.substring(1)));
    }

    //Makes sure valid boards that are too short or too long are not valid
    @Test
    public void deniesBoardCauseInvalidChar() {
        assertFalse(GameMode.isValidBoard(validBoard.replace('q', 'j')));
    }

    //Makes sure valid boards that are too short or too long are not valid
    @Test
    public void deniesBoardCauseNumKings() {
        assertFalse(GameMode.isValidBoard(validBoard.replace('k', 'r')));
        assertFalse(GameMode.isValidBoard(validBoard.replace('K', 'R')));
        assertFalse(GameMode.isValidBoard(validBoard.replace('q', 'k')));
        assertFalse(GameMode.isValidBoard(validBoard.replace('Q', 'K')));
    }

    //Makes sure valid modes can be created
    @Test
    public void canCreateValidMode() {
        boolean gameModeLegal = true;
        try {
            GameMode gameMode = new GameMode("Test Mode", "Test Description", validBoard);
            if ( ! ( gameMode.getName().equals("Test Mode")
            && gameMode.getDescription().equals("Test Description")
            && gameMode.getNewBoard().equals(validBoard) ) ) {
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
            GameMode gameMode = new GameMode("Test Mode", "Test Description", validBoard.substring(1));
        } catch ( IllegalArgumentException e ) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

}
