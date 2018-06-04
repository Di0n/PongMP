import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

public class Player implements Comparable<Player>
{
    private final UUID clientID;
    private final InetSocketAddress socketAddress;
    private final String name;
    private long lastPacketReceived;


    public Player(UUID clientID, String name, InetSocketAddress socketAddress)
    {
        this.clientID = clientID;
        this.name = name;
        this.socketAddress = socketAddress;
        this.lastPacketReceived = System.currentTimeMillis();
    }

    public UUID getClientID()
    {
        return clientID;
    }

    public InetSocketAddress getSocketAddress()
    {
        return socketAddress;
    }

    public String getName()
    {
        return name;
    }

    public long getLastPacketReceived()
    {
        return lastPacketReceived;
    }

    public void setLastPacketReceived(long lastPacketReceived)
    {
        this.lastPacketReceived = lastPacketReceived;
    }

    @Override
    public String toString()
    {
        return "Player{" +
                "clientID=" + clientID +
                ", socketAddress=" + socketAddress +
                ", name='" + name + '\'' +
                ", lastPacketReceived=" + lastPacketReceived +
                '}';
    }

    @Override
    public int compareTo(Player p)
    {
        return this.name.compareTo(p.name);
    }

    /* @Override
    public boolean equals(Object object)
    {
        if (object == null) return false;
        if (object == this) return true;
        if ((!(object instanceof Player)) && !(object instanceof SocketAddress)) return false;

        if (object instanceof Player)
        {

        }
        Player p = (Player)object;
        if (this.socketAddress == p.getSocketAddress() || this.clientID == p.getClientID())
            return true;
        else
            return false;
    }*/
}
