package playerdata;

import org.json.JSONArray;
import org.json.JSONObject;
import playerdata.models.Player;
import playerdata.models.PlayerVersion;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class JSONWriter {

    static void writeAllPlayersToJSONFile(String outputFileName, List<Player> players) throws Exception {
        // establish output path, instantiate PrintWriter
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path outFilePath = Paths.get(currentPath.toString(), "src", "main", "resources", outputFileName);
        PrintWriter writer = new PrintWriter(outFilePath.toString(), "UTF-8");

        JSONObject json = new JSONObject();
        JSONArray playersArr = new JSONArray();
        for(Player player : players) {
            JSONObject playerObj = new JSONObject();
            playerObj.put("name", player.name);
            playerObj.put("position", player.position);
            playerObj.put("earliestYearPlayed", player.earliestYearPlayed);
            playerObj.put("latestYearPlayed", player.latestYearPlayed);
            playerObj.put("careerPPG", player.pointsPerGame());
            JSONArray versions = new JSONArray();
            for(PlayerVersion version : player.versions) {
                JSONObject versionObj = new JSONObject();
                versionObj.put("team", version.team);
                versionObj.put("rating", version.rating);
                if(version.isCurrent) {
                    versionObj.put("isCurrent", true);
                }
                versions.put(versionObj);
            }
            playerObj.put("versions", versions);

            playersArr.put(playerObj);
        }
        json.put("players", playersArr);

        // write to file and close resource
        writer.print(json.toString(4));
        writer.close();
    }

}
