package networkpackets;

import java.io.Serializable;

public enum JoinResponseType implements Serializable
{
    OK,
    DENIED,
    FULL,
    BANNED,
    ERROR,
    PROTOCOL
}
