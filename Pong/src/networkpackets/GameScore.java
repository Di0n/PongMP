package networkpackets;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class GameScore implements Serializable
{
    private final Map<UUID, Integer> scores;

    public GameScore(Map<UUID, Integer> scores)
    {
        this.scores = scores;
    }

    public Map<UUID, Integer> getScores()
    {
        return scores;
    }
}
