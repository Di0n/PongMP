package framework;

import gameobjects.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DebugDraw
{
    public static void draw(Graphics2D g2d, ArrayList<GameObject> gameObjects)
    {
        AffineTransform ot = g2d.getTransform();
        for (GameObject gameObject : gameObjects)
        {
            AffineTransform tx = new AffineTransform();
            tx.translate(gameObject.position.x, gameObject.position.y);
            g2d.transform(tx);

            g2d.draw(new Rectangle2D.Double(0,0, gameObject.getWidth(), gameObject.getHeight()));

            g2d.setTransform(ot);
        }
    }
}
