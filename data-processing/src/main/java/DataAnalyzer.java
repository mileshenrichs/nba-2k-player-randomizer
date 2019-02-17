import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataAnalyzer {

    static List<SeasonStatline> top10PointScoringSeasons(List<SeasonStatline> statlines) {
        Comparator<SeasonStatline> comparator = Comparator.comparing(o -> o.pointsScored);
        statlines.sort(comparator);
        Collections.reverse(statlines);
        return statlines.subList(0, 10);
    }

}
