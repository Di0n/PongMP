package gameobjects;

import java.io.Serializable;

public class Vector2
{
    public static Vector2 zero = new Vector2(0, 0);

    public double x;
    public double y;

    public Vector2()
    {
        this.x = 0;
        this.y = 0;
    }
    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public Vector2 multiply(double d)
    {
        return new Vector2(x * d, y * d);
    }

    public Vector2 add(Vector2 v)
    {
        return new Vector2(x + v.x, y + v.y);
    }

    public Vector2 negative()
    {
        return multiply(-1);
    }

    public double magnitude()
    {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector2 normalize()
    {
        double length = Math.sqrt(x * x + y * y);
        return new Vector2(x / length, y / length);
    }
}
