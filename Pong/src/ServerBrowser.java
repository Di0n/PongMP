import networkpackets.JoinResponseType;
import networkpackets.client.JoinRequest;
import networkpackets.client.JoinResponse;
import utils.Utils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
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
        UDPClient socket = new UDPClient(false, new InetSocketAddress(server, 14567));
        socket.initialize();

        UUID clientID = UUID.randomUUID();
        JoinRequest request = new JoinRequest(clientID, "Dion");

        byte[] bytes = Utils.createByteArray(request);

        int sent = socket.send(bytes);
        System.out.println("Sent join request: "+sent);

        ByteBuffer buffer = ByteBuffer.allocate(512);
        int received = 0;
        int paddleIndex = -1;
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
                    if (response.getJoinResponseType() != JoinResponseType.OK)
                        return;

                    paddleIndex = response.getPaddle();
                    System.out.println("Paddle index: "+paddleIndex);
                }
            }
        } while (received == 0);

        new Pong(clientID, socket, paddleIndex).start();
       /*AbstractMap.SimpleEntry<JoinResponseType, Integer> resp = join(socket);


       if (resp.getKey() == JoinResponseType.OK)
           new Pong(clientID, socket, resp.getValue()).start();*/
    }

    private ByteBuffer buffer = ByteBuffer.allocate(512);
    private AbstractMap.SimpleEntry<JoinResponseType, Integer> join(UDPClient socket)
    {
        int received = socket.receive(buffer);

        if (received > 0)
        {
            int paddleIndex = -1;
            Object object = Utils.createObject(buffer.array());

            if (object instanceof JoinResponse)
            {
                JoinResponse response = (JoinResponse)object;
                System.out.println("Join response: "+response.getJoinResponseType());
                if (response.getJoinResponseType() != JoinResponseType.OK)
                    return new AbstractMap.SimpleEntry<JoinResponseType, Integer>(response.getJoinResponseType(), paddleIndex);

                paddleIndex = response.getPaddle();
                System.out.println("Paddle index: "+paddleIndex);

                return new AbstractMap.SimpleEntry<JoinResponseType, Integer>(response.getJoinResponseType(), paddleIndex);
            }
        }
            return join(socket);
    }
}
