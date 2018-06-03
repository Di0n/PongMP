package networkpackets.player;

import java.io.Serializable;

public enum PlayerInputType implements Serializable
{
    MOVE_UP,
    MOVE_DOWN,
    STOPPED_MOVING,
    DISCONNECT,
    HEARTBEAT,
}