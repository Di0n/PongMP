package networkpackets;

import networkpackets.gameobjects.BallPacket;
import networkpackets.gameobjects.PaddlePacket;

import java.io.Serializable;

public class GameState implements Serializable
{
    private final BallPacket ballPacket;
    private final PaddlePacket leftPaddlePacket, rightPaddlePacket;

    public GameState(BallPacket ballPacket, PaddlePacket leftPaddlePacket, PaddlePacket rightPaddlePacket)
    {
        this.ballPacket = ballPacket;
        this.leftPaddlePacket = leftPaddlePacket;
        this.rightPaddlePacket = rightPaddlePacket;
    }

    public BallPacket getBallPacket()
    {
        return ballPacket;
    }

    public PaddlePacket getLeftPaddlePacket()
    {
        return leftPaddlePacket;
    }

    public PaddlePacket getRightPaddlePacket()
    {
        return rightPaddlePacket;
    }

}
