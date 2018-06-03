package networkpackets.gameobjects;

import java.io.Serializable;

public class GamePacket implements Serializable
{
    public double getPositionX()
    {
        return positionX;
    }

    public double positionX, positionY;
    public double velocityX, velocityY;

    public GamePacket(double positionX, double positionY, double velocityX, double velocityY)
    {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}
