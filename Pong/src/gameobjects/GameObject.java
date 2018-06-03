package gameobjects;


import gameobjects.fixtures.Rectangle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class GameObject extends Body
{
    protected BufferedImage image;
    protected double scale;

    public GameObject(BufferedImage image, double scale)
    {
        this.image = image;
        this.scale = scale;
    }

    public void draw(Graphics2D g2d)
    {
        if (image == null) return;

        AffineTransform ot = g2d.getTransform();


        AffineTransform tx = new AffineTransform();
        tx.translate(position.x, position.y);
        //tx.rotate(super.rotation);
        //tx.scale(scale, -scale); //?
        //tx.translate(0,0); // ?
        //tx.translate(-image.getWidth() /2, -image.getHeight() / 2);

        g2d.drawImage(image, tx, null);
        //g2d.drawImage(image, (int)position.x, (int)position.y, (int)(image.getWidth()*scale), (int)(image.getHeight()*scale), null);
        //g2d.setTransform(ot);
    }

    public abstract void update(double deltaTime);

    public boolean hasCollided(GameObject gameObject)
    {
        return new Rectangle2D.Double(position.x, position.y, getWidth(), getHeight()).intersects(
                gameObject.position.x, gameObject.position.y, gameObject.getWidth(), gameObject.getHeight());
        //return new Rectangle2D.Double(position.x, position.y, getWidth() / 2, getHeight()/2).intersects
          //      (gameObject.position.x, gameObject.position.y, gameObject.getWidth() / 2, gameObject.getHeight() / 2);
    }
    public BufferedImage getImage()
    {
        return image;
    }

    public double getScale()
    {
        return scale;
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
