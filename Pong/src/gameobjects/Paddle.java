package gameobjects;

import framework.Game;

import gameobjects.fixtures.Rectangle;
import networkpackets.gameobjects.GamePacket;
import networkpackets.gameobjects.PaddlePacket;

import java.awt.image.BufferedImage;

public class Paddle extends GameObject
{
    public Paddle(BufferedImage image, double scale)
    {
        super(image, scale);
        fixture = new Rectangle(image.getWidth() * scale, image.getHeight() * scale);
    }

    @Override
    public void update(double deltaTime)
    {
        position = position.add(velocity.multiply(deltaTime));

        if (position.y < 0)
        {
            position.y = 0;
            velocity = Vector2.zero;
        }
        else if (position.y + getHeight() > Game.PLAY_AREA.height)
        {
            position.y = Game.PLAY_AREA.height - getHeight();
        }
        /*double heightRadius = getHeight() / 2;
        if (position.y - heightRadius < 0)
        {
            position.y = 0 + heightRadius;
            velocity = Vector2.zero;
        }
        else if (position.y + heightRadius > Game.PLAY_AREA.height)
        {
            position.y = Game.PLAY_AREA.height - heightRadius;
            velocity = Vector2.zero;
        }*/
    }

    public PaddlePacket toPacket()
    {
        return new PaddlePacket(position.x, position.y, velocity.x, velocity.y);
    }
}
