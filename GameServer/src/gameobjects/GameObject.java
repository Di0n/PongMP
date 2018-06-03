package gameobjects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class GameObject extends Body
{
    protected double width, height;
    public GameObject(double width, double height)
    {
        this.width = width;
        this.height = height;
    }

    public abstract void update(double deltaTime, final Dimension area);

    public boolean hasCollided(GameObject gameObject)
    {
        return new Rectangle2D.Double(position.x, position.y, getWidth(), getHeight()).intersects
                (gameObject.position.x, gameObject.position.y, gameObject.getWidth(), gameObject.getHeight());
    }

    public double getWidth()
    {
        return fixture.getWidth();
    }

    public double getHeight()
    {
        return fixture.getHeight();
    }
}
