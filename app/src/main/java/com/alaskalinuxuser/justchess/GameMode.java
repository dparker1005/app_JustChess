package com.alaskalinuxuser.justchess;

public class GameMode {
    private String mName;
    private String mDescription;
    private String mNewBoard;

    /**
     * Holds contents for a game mode
     * @param name of gamemode
     * @param description for gamemode
     * @param newBoard starting piece locations
     * @throws RuntimeException if board is not valid
     */
    public GameMode(String name, String description, String newBoard) {
        mName = name;
        mDescription = description;
        if ( isValidBoard( newBoard ) ) {
            mNewBoard = newBoard;
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * Returns if a board is valid
     * @param board to be considered
     * @return bool, true if board is valid
     */
    static boolean isValidBoard( String board ) {
        if ( board.length() != 64 ) {
            return false;
        }
        String validChars = "*RNBQPrnbqp";
        boolean wKingFound = false;
        boolean bKingFound = false;
        for ( int i = 0; i < board.length(); i++ ){
            char c = board.charAt(i);
            if ( validChars.indexOf(c) != -1 ) {
                if ( c == 'K' && ! wKingFound ) {
                    wKingFound = true;
                } else if ( c == 'k' && ! bKingFound ) {
                    bKingFound = true;
                } else {
                    return false;
                }
            }
        }

        return ( wKingFound && bKingFound );
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getNewBoard() {
        return mNewBoard;
    }
}
