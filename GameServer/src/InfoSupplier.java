import networkpackets.client.InfoRequest;
import networkpackets.client.InfoResponse;
import utils.Utils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class InfoSupplier implements Runnable
{
    private final UDPServer socket;
    public InfoSupplier(Engine engine)
    {
        socket = new UDPServer(14444, true);
        socket.initialize();
        buffer = ByteBuffer.allocate(512);
    }
    private ByteBuffer buffer;
    @Override
    public void run()
    {
        InetSocketAddress socketAddress = (InetSocketAddress) socket.receive(buffer);

        Object packet = Utils.createObject(buffer.array());

        if (packet instanceof InfoRequest)
        {
            InfoRequest ir = (InfoRequest)packet;

            InfoResponse infoResponse = new InfoResponse();

            switch (ir.getFlags())
            {
                case InfoRequest.SERVER_INFO:
                    infoResponse.setServerInfo("Server info sdkljaskljdklasjdlkasdklasd.");
                    break;
                case InfoRequest.PING:
                    // stuur leeg pakket
                    break;
                case InfoRequest.SERVER_BANNER:
                    infoResponse.setBannerURL("http://brandmark.io/logo-rank/random/pepsi.png");
                    break;
            }
        }
    }
}
