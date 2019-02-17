import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JSONWriter {

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
            JSONArray teamsArr = new JSONArray();
            for(String team : player.teams) {
                teamsArr.put(team);
            }
            playerObj.put("teams", teamsArr);
            playerObj.put("earliestYearPlayed", player.earliestYearPlayed);
            playerObj.put("latestYearPlayed", player.latestYearPlayed);
            playerObj.put("careerPPG", player.pointsPerGame());

            playersArr.put(playerObj);
        }
        json.put("players", playersArr);

        // write to file and close resource
        writer.print(json.toString(4));
        writer.close();
    }

}
