package playerdata.models;

import java.util.List;

public class Player {
    public String name;
    public Position position;
    public int earliestYearPlayed;
    public int latestYearPlayed;
    public int careerGames;
    public int careerPoints;
    public List<PlayerVersion> versions;

    public Player(String n, Position p) {
        this.name = n;
        this.position = p;
    }

    public float pointsPerGame() {
        return (float) careerPoints / careerGames;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Player)) return false;
        Player otherPlayer = (Player) o;
        return this.name.equals(otherPlayer.name);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) -- %.1f ppg from %d to %d", name, position,
                pointsPerGame(), earliestYearPlayed, latestYearPlayed);
    }
}
