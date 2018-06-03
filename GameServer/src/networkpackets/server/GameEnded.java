package networkpackets.server;

import java.io.Serializable;

public class GameEnded implements Serializable
{
    public enum EndReason
    {
        FORFEIT,
        SCORE,
    }

    private final EndReason reason;
    public GameEnded(EndReason reason)
    {
        this.reason = reason;
    }

    public EndReason getReason()
    {
        return reason;
    }

}
