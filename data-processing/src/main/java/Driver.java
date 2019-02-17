import java.util.List;
import java.util.Random;

public class Driver {

    public static void main(String[] args) {
        PlayerRepository playerRepository = new PlayerRepository();
        List<SeasonStatline> statlines = playerRepository.getSeasonStatlines();
        System.out.println(statlines.size() + " statlines collected");
//        statlines.removeIf(statline -> {
//            return !statline.playerName.equals("LeBron James");
//        });

//        List<SeasonStatline> top10SeasonScores = DataAnalyzer.top10PointScoringSeasons(statlines);
//        printStatList(top10SeasonScores);

        for(int i = 0; i < 20; i++) {
            System.out.println(pickStatline(statlines));
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

}
