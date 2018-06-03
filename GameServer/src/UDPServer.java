import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPServer
{
    private final int port;
    private final boolean isBlocking;
    private DatagramChannel server;

    public UDPServer(int port, boolean blocking)
    {
        this.port = port;
        this.isBlocking = blocking;
    }
    public boolean initialize()
    {
        try
        {
            server = DatagramChannel.open();
            server.configureBlocking(isBlocking);
            server.socket().bind(new InetSocketAddress(port));
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public SocketAddress receive(ByteBuffer buffer)
    {
        buffer.clear();

        SocketAddress socketAddress = null;
        try
        {
            socketAddress = server.receive(buffer);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return socketAddress;
    }

    public int send(byte[] data, InetSocketAddress receiver)
    {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.clear();
        buffer.put(data);
        buffer.flip();

        try
        {
            return server.send(buffer, receiver);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public int getPort()
    {
        return server.socket().getLocalPort();
    }
}
