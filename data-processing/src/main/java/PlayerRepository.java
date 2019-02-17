import java.io.*;
import java.net.URL;
import java.util.*;

public class PlayerRepository {
    private Map<String, Position> positionCodeMap;

    PlayerRepository() {
        this.positionCodeMap = buildPositionCodeMap();
    }

    List<SeasonStatline> getSeasonStatlines() {
        try {
            List<SeasonStatline> statlines = new ArrayList<>();

            BufferedReader br = getReader();
            br.readLine(); // skip first line (column titles)
            String line = "";
            int i = 0;
            while((line = br.readLine()) != null) {
                String[] seasonStat = line.split(",");

                if(seasonStat.length == 53) {
                    SeasonStatline statline = buildStatline(seasonStat);
                    if(seasonStatIsGood(statline)) {
                        statlines.add(buildStatline(seasonStat));
                    }
                }

                i++;
            }

            return statlines;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException in PlayerRepository.getPlayers() -- input file not found");
        } catch (IOException e) {
            System.out.println("IOException in PlayerRepository.getPlayers() -- problem reading line of input file");
        }

        return null;
    }

    List<Player> getPlayers() {
        Map<String, Player> players = new HashMap<>();

        for(SeasonStatline statline : getSeasonStatlines()) {
            Player player = new Player(statline.playerName, statline.position);
            if(!players.containsKey(player.name)) {
                player.teams = new ArrayList<>();
                player.teams.add(statline.teamCode);
                player.earliestYearPlayed = statline.year;
                player.latestYearPlayed = statline.year;
                player.careerGames = statline.gamesPlayed;
                player.careerPoints = statline.pointsScored;

                players.put(player.name, player);
            } else {
                player = players.get(player.name);

                player.position = statline.position;
                if(!player.teams.contains(statline.teamCode)) {
                    player.teams.add(statline.teamCode);
                }
                player.earliestYearPlayed = Math.min(player.earliestYearPlayed, statline.year);
                player.latestYearPlayed = Math.max(player.latestYearPlayed, statline.year);
                player.careerGames += statline.gamesPlayed;
                player.careerPoints += statline.pointsScored;
            }
        }

        return new ArrayList<>(players.values());
    }

    List<Player> getRelevantPlayers() {
        List<Player> players = getPlayers();

        // at least 6 points per game
        players.removeIf(p -> p.pointsPerGame() < 6);

        // played for multiple years
        players.removeIf(p -> (p.earliestYearPlayed == p.latestYearPlayed && p.latestYearPlayed < 2017));

        return players;
    }

    private BufferedReader getReader() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("season-stats.csv");
        if(resource != null) {
            File file = new File(resource.getFile());
            return new BufferedReader(new FileReader(file));
        } else {
            throw new FileNotFoundException();
        }
    }

    private Map<String, Position> buildPositionCodeMap() {
        Map<String, Position> posCodeMap = new HashMap<>();
        posCodeMap.put("G", Position.SHOOTING_GUARD);
        posCodeMap.put("F", Position.SMALL_FORWARD);
        posCodeMap.put("SG", Position.SHOOTING_GUARD);
        posCodeMap.put("SF", Position.SMALL_FORWARD);
        posCodeMap.put("C", Position.CENTER);
        posCodeMap.put("PG", Position.POINT_GUARD);
        posCodeMap.put("PF", Position.POWER_FORWARD);

        return posCodeMap;
    }

    private SeasonStatline buildStatline(String[] seasonStat) {
        SeasonStatline statline = new SeasonStatline();
        statline.year = intOrNull(seasonStat[1]);
        statline.playerName = stringOrNull(seasonStat[2]);
        statline.position = positionOrNull(seasonStat[3]);
        statline.age = intOrNull(seasonStat[4]);
        statline.teamCode = stringOrNull(seasonStat[5]);
        statline.gamesPlayed = intOrNull(seasonStat[6]);
        statline.gamesStarted = intOrNull(seasonStat[7]);
        statline.minutesPlayed = intOrNull(seasonStat[8]);
        statline.playerEfficiencyRating = floatOrNull(seasonStat[9]);
        statline.trueShootingPercentage = floatOrNull(seasonStat[10]);
        statline.threePointersMade = intOrNull(seasonStat[34]);
        statline.threePointPercentage = floatOrNull(seasonStat[36]);
        statline.boards = intOrNull(seasonStat[46]);
        statline.assists = intOrNull(seasonStat[47]);
        statline.steals = intOrNull(seasonStat[48]);
        statline.blocks = intOrNull(seasonStat[49]);
        statline.pointsScored = intOrNull(seasonStat[52]);

        statline.cleanStatlineData();
        return statline;
    }

    private boolean seasonStatIsGood(SeasonStatline statline) {
        return statline.year != null && statline.playerName != null
                && statline.pointsScored != null && statline.teamCode != null
                && statline.position != null;
    }

    private Integer intOrNull(String data) {
        return !data.isEmpty() ? Integer.valueOf(data) : null;
    }

    private String stringOrNull(String data) {
        return !data.isEmpty() ? data : null;
    }

    private Position positionOrNull(String data) {
        if(!data.isEmpty()) {
            if(data.contains("-")) {
                return positionCodeMap.get(data.split("-")[0]);
            } else {
                return positionCodeMap.get(data);
            }
        }

        return null;
    }

    private Float floatOrNull(String data) {
        return !data.isEmpty() ? Float.valueOf(data) : null;
    }

}
