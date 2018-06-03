import networkpackets.client.JoinRequest;
import networkpackets.client.JoinResponse;
import utils.Utils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

public class ServerBrowser
{
    public static void main(String[] args)
    {
        new ServerBrowser().start();
    }

    public ServerBrowser()
    {

        // Maak gui aan met servers
    }
    private void start()
    {
        // set visible
        String local = "localhost";
        String server = "server.anveon.nl";
        UDPClient socket = new UDPClient(false, new InetSocketAddress(local, 14567));
        socket.initialize();

        UUID clientID = UUID.randomUUID();
        JoinRequest request = new JoinRequest(clientID, "Dion");

        byte[] bytes = Utils.createByteArray(request);

        int sent = socket.send(bytes);
        System.out.println("Sent join request: "+sent);

        ByteBuffer buffer = ByteBuffer.allocate(512);
        int received = 0;
        do
        {
            received = socket.receive(buffer);

            if (received > 0)
            {
                Object object = Utils.createObject(buffer.array());

                if (object instanceof JoinResponse)
                {
                    JoinResponse response = (JoinResponse)object;

                    System.out.println("Join response: "+response.getJoinResponseType());
                }
            }
        } while (received == 0);

        new Pong(socket).start();
    }
}
