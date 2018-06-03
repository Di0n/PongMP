import gameobjects.Vector2;

import java.io.Serializable;

public class PacketTest implements Serializable
{
    Vector2 ballPosition;
    Vector2 ballVelocity;

    Vector2 leftPaddlePosition;
    Vector2 leftPaddleVelocity;

    Vector2 rightPaddlePosition;
    Vector2 rightPaddleVelocity;

    public PacketTest(Vector2 ballPosition, Vector2 ballVelocity, Vector2 leftPaddlePosition, Vector2 leftPaddleVelocity, Vector2 rightPaddlePosition, Vector2 rightPaddleVelocity)
    {
        this.ballPosition = ballPosition;
        this.ballVelocity = ballVelocity;
        this.leftPaddlePosition = leftPaddlePosition;
        this.leftPaddleVelocity = leftPaddleVelocity;
        this.rightPaddlePosition = rightPaddlePosition;
        this.rightPaddleVelocity = rightPaddleVelocity;
    }
}