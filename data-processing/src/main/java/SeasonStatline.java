public class SeasonStatline {
    Integer year;                  // column 1 (Year)
    String playerName;             // column 2 (Player)
    Position position;             // column 3 (Pos)
    Integer age;                   // column 4 (Age)
    String teamCode;               // column 5 (Tm)
    Integer gamesPlayed;           // column 6 (G)
    Integer gamesStarted;          // column 7 (GS)
    Integer minutesPlayed;         // column 8 (MP)
    Float playerEfficiencyRating;  // column 9 (PER)
    Float trueShootingPercentage;  // column 10 (TS%)
    Integer threePointersMade;     // column 34 (3P)
    Float threePointPercentage;    // column 36 (3P%)
    Integer boards;                // column 46 (TRB)
    Integer assists;               // column 47 (AST)
    Integer steals;                // column 48 (STL)
    Integer blocks;                // column 49 (BLK)
    Integer pointsScored;          // column 52 (PTS)

    @Override
    public String toString() {
        return String.format("{ %s (%d): %d points in %d games on %s }", playerName, year, pointsScored, gamesPlayed, teamCode);
    }

    void cleanStatlineData() {
        // clean player name (some have an asterisk at the end)
        if(playerName.charAt(playerName.length() - 1) == '*') {
            playerName = playerName.substring(0, playerName.length() - 1);
        }
    }
}
