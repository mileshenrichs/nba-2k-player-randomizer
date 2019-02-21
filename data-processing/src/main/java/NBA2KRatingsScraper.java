import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBA2KRatingsScraper {
    private static CSVWriter writer;

    public static void main(String args[]) throws IOException {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path outFilePath = Paths.get(currentPath.toString(), "src", "main", "resources", "player-versions.csv");
        File csvFile = new File(outFilePath.toUri());
//        boolean fileExisted = csvFile.exists();
//        writer = new CSVWriter(new FileWriter(csvFile, fileExisted));
//        if(!fileExisted) {
//            writeHeader();
//        }
//
//        TeamRange teamRange = new TeamRange().fromIndex(100).toEnd();
//        buildPlayerVersionsCSV(teamRange);

        cleanData(csvFile);
    }

    private static void buildPlayerVersionsCSV(TeamRange teamRange) {
        List<String> teamLinks = getTeamLinks(teamRange);
        Map<String, List<PlayerVersion>> playerVersions = new HashMap<>();

        for(String teamLink : teamLinks) {
            List<PlayerLinkAndName> playerLinks = getPlayerLinksFromTeam(teamLink);
            for(PlayerLinkAndName playerLink : playerLinks) {
                List<PlayerVersion> versions = getVersionsFromPlayer(playerLink.link);
                List<PlayerVersion> mapVersions = playerVersions.getOrDefault(playerLink.name, new ArrayList<>());
                for(PlayerVersion version : versions) {
                    if(!mapVersions.contains(version)) {
                        mapVersions.add(version);
                        // write version to CSV
                        writeCSVRow(playerLink.name, version);
                    }
                }

                playerVersions.put(playerLink.name, mapVersions);
            }
        }

        closeWriter();
    }

    private static List<PlayerVersion> getVersionsFromPlayer(String playerUrl) {
        System.out.println("Collecting versions for " + playerUrl);

        List<PlayerVersion> versions = new ArrayList<>();
        String playerPageHtml = HTTPRequestUtil.getPageHTML(playerUrl);
        if(playerPageHtml != null) {
            Document doc = Jsoup.parse(playerPageHtml);

            // make sure player is available in game
            Element playerNotAvailableElement = doc.selectFirst(".table-player p");
            if(playerNotAvailableElement != null
                    && playerNotAvailableElement.text().equals("This player is not available on NBA 2K19.")) {
                System.out.println("INFO: Player not available in game: " + playerUrl);
                return versions;
            }

            boolean mainPlayerVersionAvailable = playerNotAvailableElement == null ||
                    !playerNotAvailableElement.text().contains("This player is not playable");

            if(mainPlayerVersionAvailable) {
                // get main rating
                String mainRatingStr = doc.selectFirst(".player-rating").text().substring(0, 2);
                int mainRating = -1;
                try {
                    mainRating = Integer.parseInt(mainRatingStr);
                } catch(NumberFormatException e) {
                    System.out.println("INFO: Rating was ?? for " + playerUrl);
                }

                // get main team
                String mainTeam = doc.selectFirst(".table-player a").text();

                // check if is current edition
                Element playerTable = doc.selectFirst(".table-player table");
                Elements tableRows = playerTable.select("tr");
                Element firstRow = tableRows.get(0);
                Element editionCell = firstRow.selectFirst("td");
                String editionCellValue = editionCell.text();
                boolean mainIsCurrent = editionCellValue.equals("Current");

                // create this main version and add it to versions list
                PlayerVersion mainVersion = new PlayerVersion(mainTeam, mainRating);
                if(mainIsCurrent) mainVersion.isCurrent = true;
                versions.add(mainVersion);
            }

            // add other versions if they exist
            Element otherVersionsTable = doc.selectFirst(".version-section");
            if(otherVersionsTable != null) {
                Elements versionRows = otherVersionsTable.select("tr");
                for(int i = 1; i < versionRows.size(); i++) {
                    Element version = versionRows.get(i);
                    String team = version.selectFirst("a").text();
                    String ratingStr = version.selectFirst(".roster-rating").text();
                    int rating = -1;
                    try {
                        rating = Integer.parseInt(ratingStr);
                    } catch(NumberFormatException e) {
                        System.out.println("INFO: Rating was ?? for " + playerUrl);
                    }
                    versions.add(new PlayerVersion(team, rating));
                }
            }
        } else {
            System.out.println("Player page was null for " + playerUrl);
        }

        return versions;
    }

    private static List<PlayerLinkAndName> getPlayerLinksFromTeam(String teamUrl) {
        List<PlayerLinkAndName> links = new ArrayList<>();
        String teamPageHtml = HTTPRequestUtil.getPageHTML(teamUrl);
        if(teamPageHtml != null) {
            Document doc = Jsoup.parse(teamPageHtml);
            Elements playerLinks = doc.select("td a");
            for(Element link : playerLinks) {
                PlayerLinkAndName plan = new PlayerLinkAndName(link.attr("abs:href"), link.text());
                links.add(plan);
            }
        } else {
            System.out.println("Team page HTML was null for " + teamUrl);
        }

        System.out.println(links.size() + " player links collected from " + teamUrl);
        return links;
    }

    private static List<String> getTeamLinks(TeamRange teamRange) {
        List<String> links = new ArrayList<>();
        Document doc = Jsoup.parse(getAllTeamsListHTML());
        Elements teamLinks = doc.select(".list-group-item a");
        for(Element link : teamLinks) {
            links.add(link.attr("abs:href"));
        }

        if(teamRange.toIndex < Integer.MAX_VALUE) {
            links = links.subList(teamRange.fromIndex, teamRange.toIndex);
        } else {
            links = links.subList(teamRange.fromIndex, links.size());
        }
        System.out.println(links.size() + " team links collected");
        return links;
    }

    private static String getAllTeamsListHTML() {
        return HTTPRequestUtil.getPageHTML("https://www.2kratings.com/nba-2k19-teams");
    }

    private static void writeHeader() {
        String[] header = {"Name", "Team", "Rating", "Is Current"};
        writer.writeNext(header);
    }

    private static void writeCSVRow(String playerName, PlayerVersion version) {
        String[] row = {playerName, version.team, String.valueOf(version.rating), String.valueOf(version.isCurrent)};
        writer.writeNext(row);
        System.out.println("   Player version written for " + playerName);
    }

    private static void closeWriter() {
        System.out.println("Closing CSV writer");
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR!!! IOException while attempting to close writer.");
        }
    }

    private static class PlayerLinkAndName {
        String link;
        String name;

        PlayerLinkAndName(String link, String name) {
            this.link = link;
            this.name = name;
        }
    }

    private static class TeamRange {
        int fromIndex;
        int toIndex;

        TeamRange fromIndex(int f) {
            this.fromIndex = f;
            return this;
        }

        TeamRange toIndex(int t) {
            this.toIndex = t;
            return this;
        }

        TeamRange toEnd() {
            this.toIndex = Integer.MAX_VALUE;
            return this;
        }
    }

    private static void cleanData(File dataFile) throws IOException {
        FileReader reader = new FileReader(dataFile);
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        List<String[]> records = csvReader.readAll();

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path outFilePath = Paths.get(currentPath.toString(), "src", "main", "resources", "player-versions-clean.csv");
        File outFile = new File(outFilePath.toUri());
        writer = new CSVWriter(new FileWriter(outFile));

        Map<String, List<PlayerVersion>> playerVersions = new HashMap<>();
        for(String[] record : records) {
            String playerName = record[0];
            PlayerVersion version = new PlayerVersion(record[1].replace("Current ", ""), Integer.parseInt(record[2]));
            if(Boolean.parseBoolean(record[3]))
                version.isCurrent = true;

            List<PlayerVersion> mapVersions = playerVersions.getOrDefault(playerName, new ArrayList<>());
            if(!mapVersions.contains(version) && !version.team.contains("Free Agent") && version.rating != -1) {
                mapVersions.add(version);
                // write version to CSV
                writeCSVRow(playerName, version);
                playerVersions.put(playerName, mapVersions);
            }
        }

        closeWriter();
    }

}