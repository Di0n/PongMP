package gameobjects;

import gameobjects.fixtures.Rectangle;

import java.io.Serializable;

public class Body
{
    public Vector2 position;
    public Vector2 velocity;
    public Rectangle fixture;

    public Body()
    {
        position = new Vector2();
        velocity = new Vector2();
        this.fixture = null;
    }
}
