import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBA2KRatingsScraper {

    public static void main(String args[]) {
        buildPlayerVersionsCSV();
    }

    private static void buildPlayerVersionsCSV() {
        List<String> teamLinks = getTeamLinks();
        Map<String, List<PlayerVersion>> playerVersions = new HashMap<>();

        for(String teamLink : teamLinks) {
            List<PlayerLinkAndName> playerLinks = getPlayerLinksFromTeam(teamLink);
            for(PlayerLinkAndName playerLink : playerLinks) {
                List<PlayerVersion> versions = getVersionsFromPlayer(playerLink.link);
                List<PlayerVersion> mapVersions = playerVersions.getOrDefault(playerLink.name, new ArrayList<>());
                for(PlayerVersion version : versions) {
                    if(!mapVersions.contains(version)) {
                        mapVersions.add(version);
                        // todo: write to CSV at this point
                    }
                }

                playerVersions.put(playerLink.name, mapVersions);
            }
        }
    }

    private static List<PlayerVersion> getVersionsFromPlayer(String playerUrl) {
        List<PlayerVersion> versions = new ArrayList<>();
        String playerPageHtml = HTTPRequestUtil.getPageHTML(playerUrl);
        if(playerPageHtml != null) {
            Document doc = Jsoup.parse(playerPageHtml);

            // get main rating
            String mainRatingStr = doc.selectFirst(".player-rating").text().substring(0, 2);
            int mainRating = Integer.parseInt(mainRatingStr);

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

            // add other versions if they exist
            Element otherVersionsTable = doc.selectFirst(".version-section");
            if(otherVersionsTable != null) {
                Elements versionRows = otherVersionsTable.select("tr");
                for(int i = 1; i < versionRows.size(); i++) {
                    Element version = versionRows.get(i);
                    String team = version.selectFirst("a").text();
                    int rating = Integer.parseInt(version.selectFirst(".roster-rating").text());
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

    private static List<String> getTeamLinks() {
        List<String> links = new ArrayList<>();
        Document doc = Jsoup.parse(getAllTeamsListHTML());
        Elements teamLinks = doc.select(".list-group-item a");
        for(Element link : teamLinks) {
            links.add(link.attr("abs:href"));
        }

        System.out.println(links.size() + " team links collected");
        return links;
    }

    private static String getAllTeamsListHTML() {
        return HTTPRequestUtil.getPageHTML("https://www.2kratings.com/nba-2k19-teams");
    }

    private static class PlayerLinkAndName {
        String link;
        String name;

        PlayerLinkAndName(String link, String name) {
            this.link = link;
            this.name = name;
        }
    }

}
