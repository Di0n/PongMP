package networkpackets.client;

import java.io.Serializable;

public class InfoRequest implements Serializable
{
    public static final int PING = 1;
    public static final int SERVER_INFO = 2;
    public static final int SERVER_BANNER = 4;
    public static final int PLAYER_INFO = 8;
    public static final int OS = 16;

    private final int flags;
    public InfoRequest(int flags)
    {
        this.flags = flags;
    }

    public int getFlags()
    {
        return flags;
    }
}