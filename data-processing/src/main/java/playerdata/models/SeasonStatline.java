package playerdata.models;

public class SeasonStatline {
    public Integer year;                  // column 1 (Year)
    public String playerName;             // column 2 (Player)
    public Position position;             // column 3 (Pos)
    public Integer age;                   // column 4 (Age)
    public String teamCode;               // column 5 (Tm)
    public Integer gamesPlayed;           // column 6 (G)
    public Integer gamesStarted;          // column 7 (GS)
    public Integer minutesPlayed;         // column 8 (MP)
    public Float playerEfficiencyRating;  // column 9 (PER)
    public Float trueShootingPercentage;  // column 10 (TS%)
    public Integer threePointersMade;     // column 34 (3P)
    public Float threePointPercentage;    // column 36 (3P%)
    public Integer boards;                // column 46 (TRB)
    public Integer assists;               // column 47 (AST)
    public Integer steals;                // column 48 (STL)
    public Integer blocks;                // column 49 (BLK)
    public Integer pointsScored;          // column 52 (PTS)

    @Override
    public String toString() {
        return String.format("{ %s (%d): %d points in %d games on %s }", playerName, year, pointsScored, gamesPlayed, teamCode);
    }

    public void cleanStatlineData() {
        // clean player name (some have an asterisk at the end)
        if(playerName.charAt(playerName.length() - 1) == '*') {
            playerName = playerName.substring(0, playerName.length() - 1);
        }
    }
}
