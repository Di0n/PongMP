package networkpackets.client;

import java.io.Serializable;
import java.util.UUID;

public class JoinRequest implements Serializable
{
    private final UUID clientID;
    private final String name;
    public JoinRequest(UUID clientID, String name)
    {
        this.clientID = clientID;
        this.name = name;
    }

    public UUID getClientID()
    {
        return clientID;
    }

    public String getName()
    {
        return name;
    }
}
