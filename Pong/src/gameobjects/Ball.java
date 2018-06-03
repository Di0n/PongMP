package gameobjects;

import framework.Game;

import gameobjects.fixtures.Rectangle;
import networkpackets.gameobjects.BallPacket;
import networkpackets.gameobjects.GamePacket;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ball extends GameObject
{
    private final double MAX_SPEED = 1500;

    public Ball(BufferedImage image, double scale)
    {
        super(image, scale);
        fixture = new Rectangle(image.getWidth() * scale, image.getHeight() * scale);
    }

    @Override
    public void update(double deltaTime)
    {
        if (velocity.magnitude() > getMaxSpeed())
            velocity = velocity.normalize().multiply(getMaxSpeed());

        position = position.add(velocity.multiply(deltaTime)); // Move

        double maxX = Game.PLAY_AREA.width - getWidth();
        double maxY = Game.PLAY_AREA.height - getHeight();

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

        /*
        double widthRadius = getWidth() / 2;
        double heightRadius = getHeight() / 2;

        if (position.x - widthRadius < 0)
        {
            position.x = 0 + widthRadius;
            velocity.x *= -1;
        }
        else if (position.x + widthRadius > Game.PLAY_AREA.width)
        {
            position.x = Game.PLAY_AREA.width - widthRadius;
            position.x *= -1;
        }
        if (position.y - heightRadius < 0)
        {
            position.y = 0 + heightRadius;
            position.y *= -1;
        }
        else if (position.y + heightRadius > Game.PLAY_AREA.height)
        {
            position.y = Game.PLAY_AREA.height - heightRadius;
            position.y *= -1;
        }
*/
      /* if (position.x - widthRadius < 0 || position.x + widthRadius > Game.PLAY_AREA.width)
            velocity.x *= -1;

        if (position.y - heightRadius < 0 || position.y + heightRadius > Game.PLAY_AREA.height)
            velocity.y *= -1;*/
    }

    public void checkBoundaries(double deltaTime, ArrayList<GameObject> objects)
    {
        double widthRadius = getWidth() / 2;
        double heightRadius = getHeight() / 2;
        if (position.x - widthRadius < 0)
            position.x = 0 + widthRadius;
        else if (position.x + widthRadius > Game.PLAY_AREA.width)
            position.x = Game.PLAY_AREA.width - widthRadius;
        if (position.y - heightRadius < 0)
            position.y = 0 + heightRadius;
        else if (position.y + heightRadius > Game.PLAY_AREA.height)
            position.y = Game.PLAY_AREA.height - heightRadius;
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
