import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPClient
{
    private final boolean isBlocking;
    private InetSocketAddress serverAddress;
    private DatagramChannel client;

    public UDPClient(boolean blocking, InetSocketAddress serverAddress)
    {
        this.isBlocking = blocking;
        this.serverAddress = serverAddress;
    }

    public boolean initialize()
    {
        try
        {
            client = DatagramChannel.open();
            client.bind(null);
            client.configureBlocking(isBlocking);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public int receive(ByteBuffer buffer)
    {
        buffer.clear();

        int bytesRead = 0;
        try
        {
            InetSocketAddress address = (InetSocketAddress) client.receive(buffer);
            if (address != null && address.equals(serverAddress))
            {
                bytesRead = buffer.array().length;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return bytesRead;
    }

    public int send(byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.clear();
        buffer.put(data);
        buffer.flip();

        int bytesSent = 0;
        try
        {
            bytesSent = client.send(buffer, serverAddress);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return bytesSent;
    }
}
