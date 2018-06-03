package gameobjects;


import gameobjects.fixtures.Rectangle;
import networkpackets.gameobjects.BallPacket;
import networkpackets.gameobjects.GamePacket;

import java.awt.*;

public class Ball extends GameObject
{
    private final double MAX_SPEED = 1500;

    public Ball(double size)
    {
        super(size, size);
        fixture = new Rectangle(size, size);
    }

    @Override
    public void update(double deltaTime, final Dimension area)
    {
        if (velocity.magnitude() > getMaxSpeed())
            velocity = velocity.normalize().multiply(getMaxSpeed());

        position = position.add(velocity.multiply(deltaTime)); // Move

        double maxX = area.width - getWidth();
        double maxY = area.height - getHeight();

        if (position.x < 0)
        {
            position.x = 0;
            velocity.x *= -1;
        }
        else if (position.x > maxX)
        {
            position.x = maxX;
            velocity.x *= -1;
        }
        if (position.y < 0)
        {
            position.y = 0;
            velocity.y *= -1;
        }
        else if (position.y > maxY)
        {
            position.y = maxY;
            velocity.y *= -1;
        }
    }

    public double getMaxSpeed()
    {
        return MAX_SPEED;
    }


    public BallPacket toPacket()
    {
        return new BallPacket(position.x, position.y, velocity.x, velocity.y);
    }
}
