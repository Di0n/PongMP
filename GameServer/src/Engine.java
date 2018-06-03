import networkpackets.JoinResponseType;
import networkpackets.client.InfoRequest;
import networkpackets.client.InfoResponse;
import networkpackets.client.JoinRequest;
import networkpackets.client.JoinResponse;
import networkpackets.player.PlayerInput;
import networkpackets.player.PlayerInputType;
import utils.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Engine
{
    private final InetSocketAddress MASTER_SERVER_ADDRESS = new InetSocketAddress("master.anveon.nl", 45555);
    private final int MAX_PACKET_SIZE = 512;
    private final int SERVER_PORT = 14567;
    private final long DC_TIME = 1000000; // 1000 seconden

    private final String serverBannerURL = "http://brandmark.io/logo-rank/random/pepsi.png";
    private final String serverInfo = "Server info...";

    private final Timer gameTimer;
    private final UDPServer socket;
    private final int tickRate;
    private ByteBuffer buffer;

    protected ArrayList<Player> players;

    public Engine(int tickRate)
    {
        this.gameTimer = new Timer();
        this.tickRate = tickRate;
        players = new ArrayList<>();
        buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
        socket = new UDPServer(SERVER_PORT, false);
        socket.initialize();
    }

    public final void start()
    {
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                Engine.this.run();
            }
        };
        gameTimer.schedule(task, 100, 1000 / tickRate);

        System.out.println("Started server on " + System.getProperty("os.name"));
    }

    public synchronized final void stop()
    {
        gameTimer.cancel();
        // Server shutting down
    }

    private long lastTime = System.nanoTime();

    private void run()
    {
        receive();
        long time = System.nanoTime();
        double deltaTime = (time - lastTime) / 1e9; // of 1000000 voor ms
        lastTime = time;

        checkForDisconnect();
        update(deltaTime);
    }

    private void receive()
    {
        InetSocketAddress receiverAddress = (InetSocketAddress) socket.receive(buffer);

        if (receiverAddress == null) return;

        Object packetObject = Utils.createObject(buffer.array());

        if (packetObject instanceof JoinRequest)
        {
            JoinRequest jr = (JoinRequest) packetObject;

            Player player = new Player(jr.getClientID(), jr.getName(), receiverAddress);
            onPlayerConnect(player, JoinResponseType.DENIED);   // Standaard weigeren
        } else if (packetObject instanceof PlayerInput)   // Alleen geverifieërde players kunnen dit
        {
            Player player = getPlayerFromAddress(receiverAddress);
            if (player == null) return;

            player.setLastPacketReceived(System.currentTimeMillis());

            PlayerInput playerInput = (PlayerInput) packetObject;

            switch (playerInput.getPlayerInputType())
            {
                case MOVE_UP:
                case MOVE_DOWN:
                case STOPPED_MOVING:
                    onPlayerInput(player, playerInput.getPlayerInputType());
                    break;

                case HEARTBEAT:
                    onHeartbeat(player);
                    break;

                case DISCONNECT:
                    onPlayerDisconnect(player, DisconnectReason.LEFT);
                    break;

                default:
                    break;
            }
        } else if (packetObject instanceof InfoRequest)
        {
            // TODO info teruggeven

        }
    }

    private void checkForDisconnect()
    {
        long currentTimeMs = System.currentTimeMillis();

        ArrayList<Player> disconnectedPlayers = new ArrayList<>();
        for (Player player : players)
        {
            if (currentTimeMs - player.getLastPacketReceived() >= DC_TIME)
                disconnectedPlayers.add(player);
        }

        disconnectedPlayers.forEach(dp -> onPlayerDisconnect(dp, DisconnectReason.NO_RESPONSE));
    }

    // *** Events *** \\
    public abstract void update(final double deltaTime);

    public abstract void onPlayerInput(final Player player, final PlayerInputType playerInput);

    public void onHeartbeat(final Player player)
    {
        player.setLastPacketReceived(System.currentTimeMillis());
    }

    public void onPlayerConnect(final Player player, JoinResponseType joinResponseType)
    {
        JoinResponse jr = joinResponseType == JoinResponseType.OK ?
                new JoinResponse(joinResponseType, players.size()) :
                new JoinResponse(joinResponseType);

        byte[] buffer = Utils.createByteArray(jr);
        socket.send(buffer, player.getSocketAddress());

        if (joinResponseType == JoinResponseType.OK)
            players.add(player);
    }

    //private void onPlayerDisconnect()
    public void onPlayerDisconnect(final Player player, final DisconnectReason reason)
    {
        players.remove(player);
    }


    // Intern \\
    private final void onRequestInfo(InetSocketAddress requester, InfoRequest infoRequest)
    {
        final int flags = infoRequest.getFlags();

        InfoResponse response = new InfoResponse();
        if ((flags & InfoRequest.SERVER_BANNER) == InfoRequest.SERVER_BANNER)
        {
            response.setBannerURL("http://brandmark.io/logo-rank/random/pepsi.png");
        }
        if ((flags & InfoRequest.SERVER_INFO) == InfoRequest.SERVER_INFO)
        {
            response.setServerInfo("Server info vlalbalblal");
        }
        if ((flags & InfoRequest.PLAYER_INFO) == InfoRequest.PLAYER_INFO)
        {
            String[] playerNames = new String[players.size()];
            for (int i = 0; i < playerNames.length; i++ )
            {
                playerNames[i] = players.get(i).getName();
            }
            response.setPlayers(playerNames);
        }
        if ((flags & InfoRequest.OS) == InfoRequest.OS)
        {
            response.setServerOS(Utils.getOS());
        }

        socket.send(Utils.createByteArray(response), requester);
        // packet terugsturen met: Spelers, ping, Server msg, Server banner;
    }

    private final Player getPlayerFromAddress(InetSocketAddress address)
    {
        for (Player player : players)
        {
            if (player.getSocketAddress().equals(address))
                return player;
        }
        return null;
    }

    private final void publishToMasterServer()
    {
        // Packet voor aanmaken
        socket.send(null, MASTER_SERVER_ADDRESS);
    }

    public final void sendToAllPlayers(byte[] bytes)
    {
        for (Player player : players)
        {
            socket.send(bytes, player.getSocketAddress());
        }
    }

    public final void sendToPlayer(byte[] bytes, Player player)
    {
        socket.send(bytes, player.getSocketAddress());
    }

    public final synchronized ArrayList<Player> getPlayers()
    {
        return (ArrayList<Player>)players.clone();
    }
}
