package networkpackets.client;

import networkpackets.JoinResponseType;

import java.io.Serializable;

public class JoinResponse implements Serializable
{
    private final JoinResponseType joinResponseType;
    private final int paddle;

    public JoinResponse(JoinResponseType joinResponseType)
    {
        this.joinResponseType = joinResponseType;
        this.paddle = -1;
    }

    public JoinResponse(JoinResponseType joinResponseType, int paddle)
    {
        this.joinResponseType = joinResponseType;
        this.paddle = paddle;
    }
    public JoinResponseType getJoinResponseType()
    {
        return joinResponseType;
    }

    public int getPaddle()
    {
        return paddle;
    }
}
