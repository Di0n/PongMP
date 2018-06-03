package networkpackets.states;

import java.io.Serializable;

public class ObjectsState implements Serializable
{
    public double ballPositionX, ballPositionY;
    public double ballVelocityX, ballVelocityY;

    public double leftPaddlePositionX, leftPaddlePositionY;
    public double leftPaddleVelocityX, leftPaddleVelocityY;

    public double rightPaddlePositionX, rightPaddlePositionY;
    public double rightPaddleVelocityX, rightPaddleVelocityY;
}
