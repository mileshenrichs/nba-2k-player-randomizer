import java.util.List;
import java.util.Random;

public class Driver {

    public static void main(String[] args) {
        PlayerRepository playerRepository = new PlayerRepository();

        List<Player> players = playerRepository.getRelevantPlayers();
        System.out.println(players.size() + " players found\n");
        for(int i = 0; i < 10; i++) {
            System.out.println(pickPlayer(players));
        }

        try {
            // write all players to JSON file for frontend application
            JSONWriter.writeAllPlayersToJSONFile("players.json", players);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printStatList(List<SeasonStatline> statlines) {
        for(SeasonStatline statline : statlines) {
            System.out.println(statline);
        }
    }

    private static SeasonStatline pickStatline(List<SeasonStatline> statlines) {
        Random random = new Random();
        return statlines.get(random.nextInt(statlines.size()));
    }

    private static Player pickPlayer(List<Player> players) {
        Random random = new Random();
        return players.get(random.nextInt(players.size()));
    }

}
