package networkpackets.gameobjects;

import java.io.Serializable;

public class BallPacket implements Serializable
{
    private final double positionX, positionY;
    private final double velocityX, velocityY;

    public BallPacket(double positionX, double positionY, double velocityX, double velocityY)
    {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public double getPositionX()
    {
        return positionX;
    }
    public double getPositionY()
    {
        return positionY;
    }

    public double getVelocityX()
    {
        return velocityX;
    }

    public double getVelocityY()
    {
        return velocityY;
    }
}