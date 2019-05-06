package playerdata;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import playerdata.models.Player;
import playerdata.models.PlayerVersion;
import playerdata.models.Position;
import playerdata.models.SeasonStatline;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class PlayerRepository {
    private Map<String, Position> positionCodeMap;
    private String modernTeamNamesStr = "ATL BKN BOS CHA CHI CLE DAL DEN DET GSW HOU IND LAC LAL MEM MIA MIL MIN NOH NOP " +
            "NYK OKC ORL PHI PHX POR SAC SAS TOR UTA WAS";
    private Set<String> modernTeamNames = new HashSet<>(Arrays.asList(modernTeamNamesStr.split(" ")));

    PlayerRepository() {
        this.positionCodeMap = buildPositionCodeMap();
    }

    private List<SeasonStatline> getSeasonStatlines() {
        try {
            List<SeasonStatline> statlines = new ArrayList<>();

            BufferedReader br = getReader();
            br.readLine(); // skip first line (column titles)
            String line;
            while((line = br.readLine()) != null) {
                String[] seasonStat = line.split(",");

                if(seasonStat.length == 53) {
                    SeasonStatline statline = buildStatline(seasonStat);
                    if(seasonStatIsGood(statline)) {
                        statlines.add(buildStatline(seasonStat));
                    }
                }
            }

            return statlines;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException in playerdata.PlayerRepository.getPlayers() -- input file not found");
        } catch (IOException e) {
            System.out.println("IOException in playerdata.PlayerRepository.getPlayers() -- problem reading line of input file");
        }

        return null;
    }

    private List<Player> getPlayers() {
        Map<String, Player> players = new HashMap<>();
        Map<String, List<PlayerVersion>> playerVersions = getPlayerVersionsMap();

        for(SeasonStatline statline : getSeasonStatlines()) {
            Player player = new Player(statline.playerName, statline.position);
            if(playerVersions.containsKey(player.name)) {
                if(!players.containsKey(player.name)) {
                    player.earliestYearPlayed = statline.year;
                    player.latestYearPlayed = statline.year;
                    player.careerGames = statline.gamesPlayed;
                    player.careerPoints = statline.pointsScored;
                    player.versions = playerVersions.get(player.name);

                    players.put(player.name, player);
                } else {
                    player = players.get(player.name);

                    player.position = statline.position;
                    player.earliestYearPlayed = Math.min(player.earliestYearPlayed, statline.year);
                    player.latestYearPlayed = Math.max(player.latestYearPlayed, statline.year);
                    player.careerGames += statline.gamesPlayed;
                    player.careerPoints += statline.pointsScored;
                }
            }
        }

        return new ArrayList<>(players.values());
    }

    private static Map<String, List<PlayerVersion>> getPlayerVersionsMap() {
        Map<String, List<PlayerVersion>> playerVersions = new HashMap<>();

        try {
            Path currentPath = Paths.get(System.getProperty("user.dir"));
            Path inFilePath = Paths.get(currentPath.toString(), "src", "main", "resources", "player-versions-clean.csv");
            File csvFile = new File(inFilePath.toUri());
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile)).withSkipLines(1).build();
            List<String[]> records = csvReader.readAll();

            for(String[] record : records) {
                String playerName = record[0];
                PlayerVersion version = new PlayerVersion(record[1], Integer.parseInt(record[2]));
                if(Boolean.parseBoolean(record[3]))
                    version.isCurrent = true;

                List<PlayerVersion> mapVersions = playerVersions.getOrDefault(playerName, new ArrayList<>());
                if(!mapVersions.contains(version)) {
                    mapVersions.add(version);
                    playerVersions.put(playerName, mapVersions);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("player versions size: " + playerVersions.size());
        return playerVersions;
    }

    List<Player> getRelevantPlayers() {
        List<Player> players = getPlayers();

        // at least 4 points per game
        players.removeIf(p -> p.pointsPerGame() < 4);

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
                && statline.position != null && statline.gamesPlayed != null
                && modernTeamNames.contains(statline.teamCode);
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
