import java.util.List;

public class Player {
    String name;
    Position position;
    List<String> teams;
    int earliestYearPlayed;
    int latestYearPlayed;
    int careerGames;
    int careerPoints;

    Player(String n, Position p) {
        this.name = n;
        this.position = p;
    }

    float pointsPerGame() {
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
        return String.format("%s (%s for %d teams) -- %.1f ppg from %d to %d", name, position,
                teams.size(), pointsPerGame(), earliestYearPlayed, latestYearPlayed);
    }
}
