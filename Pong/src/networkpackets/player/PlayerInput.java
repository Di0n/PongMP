package networkpackets.player;

import java.io.Serializable;

public class PlayerInput implements Serializable
{
    private final PlayerInputType playerInputType;
    public PlayerInput(PlayerInputType playerInputType)
    {
        this.playerInputType = playerInputType;
    }

    public PlayerInputType getPlayerInputType()
    {
        return playerInputType;
    }
}
