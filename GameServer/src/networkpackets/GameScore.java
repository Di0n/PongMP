package networkpackets;

import networkpackets.player.PlayerInfo;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.UUID;

public class GameScore implements Serializable
{
    private final UUID client;

    public GameScore(UUID clientID)
    {
        this.client = clientID;
    }

    public UUID getClient()
    {
        return client;
    }
}
