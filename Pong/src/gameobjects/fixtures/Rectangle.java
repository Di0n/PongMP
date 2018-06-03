package gameobjects.fixtures;

import java.io.Serializable;

public class Rectangle implements Serializable
{
    private double width, height;
    public Rectangle(double width, double height)
    {
        this.width = width;
        this.height = height;
    }
    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }
}
