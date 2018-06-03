package networkpackets.server;

import java.io.Serializable;
import java.util.UUID;

public class GameEnded implements Serializable
{
    public UUID getWinner()
    {
        return winner;
    }

    public enum EndReason
    {
        FORFEIT,
        SCORE,
    }

    private final EndReason reason;
    private final UUID winner;

    public GameEnded(EndReason reason)
    {
        this.reason = reason;
        this.winner = null;
    }

    public GameEnded(EndReason reason, UUID winner)
    {
        this.reason = reason;
        this.winner = winner;
    }

    public EndReason getReason()
    {
        return reason;
    }

}
