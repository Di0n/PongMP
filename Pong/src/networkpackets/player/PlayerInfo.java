package networkpackets.player;

import java.io.Serializable;
import java.util.UUID;

public class PlayerInfo implements Serializable
{
    private final UUID clientID;
    private final String name;
    private final int score;

    public PlayerInfo(UUID clientID, String name, int score)
    {
        this.clientID = clientID;
        this.name = name;
        this.score = score;
    }

    public UUID getClientID()
    {
        return clientID;
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }
}
