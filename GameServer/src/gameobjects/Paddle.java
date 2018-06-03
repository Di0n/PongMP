package gameobjects;

import gameobjects.fixtures.Rectangle;
import networkpackets.gameobjects.BallPacket;
import networkpackets.gameobjects.GamePacket;
import networkpackets.gameobjects.PaddlePacket;

import java.awt.*;

public class Paddle extends GameObject
{
    public Paddle(double width, double height)
    {
        super(width, height);
        fixture = new Rectangle(width, height);
    }

    @Override
    public void update(double deltaTime, Dimension area)
    {
        position = position.add(velocity.multiply(deltaTime));

        if (position.y < 0)
        {
            position.y = 0;
            velocity = Vector2.zero;
        }
        else if (position.y + getHeight() > area.height)
        {
            position.y = area.height - getHeight();
        }
    }

    public PaddlePacket toPacket()
    {
        return new PaddlePacket(position.x, position.y, velocity.x, velocity.y);
    }
}
