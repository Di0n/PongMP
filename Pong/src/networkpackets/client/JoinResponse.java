package networkpackets.client;

import networkpackets.JoinResponseType;

import java.io.Serializable;

public class JoinResponse implements Serializable
{
    private final JoinResponseType joinResponseType;
    public JoinResponse(JoinResponseType joinResponseType)
    {
        this.joinResponseType = joinResponseType;
    }

    public JoinResponseType getJoinResponseType()
    {
        return joinResponseType;
    }
}
