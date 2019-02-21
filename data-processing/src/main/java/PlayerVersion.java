public class PlayerVersion {
    String team;
    int rating;
    boolean isCurrent;

    PlayerVersion(String team, int rating) {
        this.team = team;
        this.rating = rating;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("rated %d on %s", rating, team));
        if(isCurrent) {
            builder.append(" (is current version)");
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof PlayerVersion)) return false;

        PlayerVersion pv = (PlayerVersion) o;
        return this.team.equals(pv.team) && this.rating == pv.rating;
    }
}
