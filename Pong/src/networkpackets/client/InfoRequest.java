package networkpackets.client;

import java.io.Serializable;

public enum InfoRequest implements Serializable
{
    PING,
    SERVER_INFO,
    SERVER_BANNER
}
