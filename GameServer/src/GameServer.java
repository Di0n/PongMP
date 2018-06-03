import gameobjects.Ball;
import gameobjects.GameObject;
import gameobjects.Paddle;
import gameobjects.Vector2;
import networkpackets.GameState;
import networkpackets.player.PlayerInputType;
import networkpackets.JoinResponseType;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;

public class GameServer extends Engine
{
    public static void main(String[] args)
    {
        new GameServer(30).start();
    }

    private final int MAX_PACKET_SIZE = 512;
    private final Dimension AREA = new Dimension(1200, 600);
    private UDPServer socket;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Ball ball;
    private Paddle leftPaddle, rightPaddle;


    public GameServer(int tickRate)
    {
        super(tickRate);
        ball = new Ball(20);

        leftPaddle = new Paddle(20, 100);
        rightPaddle = new Paddle(20, 100);

        gameObjects.add(ball);
        gameObjects.add(leftPaddle);
        gameObjects.add(rightPaddle);
    }

    private void resetPositions()
    {
        ball.position.x = (AREA.width / 2) - (ball.getWidth() / 2);                //((ball.getWidth()) / 2) + getWidth()/2;
        ball.position.y = (AREA.height / 2) - (ball.getHeight() / 2);
        ball.velocity = Vector2.zero;

        leftPaddle.position.x = 25;                     //((leftPaddle.getWidth()) / 2) + 15;
        leftPaddle.position.y = (AREA.height/2) - (leftPaddle.getHeight() / 2);
        leftPaddle.velocity = Vector2.zero;

        rightPaddle.position.x = AREA.width - 40;       //((ball.getWidth())/2) + getWidth() - ball.getWidth() - 20;
        rightPaddle.position.y = (AREA.height/2) - (rightPaddle.getHeight() / 2);
        rightPaddle.velocity = Vector2.zero;
    }

    private boolean started;
    @Override
    public void update(double deltaTime)
    {
        if (players.size() == 0) return; // Players size
        if (!started)
        {
            onGameStart();
            started = true;
        }

        if (ball.hasCollided(leftPaddle))
        {

            System.out.println("Collided");

            ball.velocity.x -= 50;


            if (ball.velocity.y < 0)
                ball.velocity.y -= 50;
            else
                ball.velocity.y += 50;

            ball.velocity.x *= -1;

            ball.position.x = leftPaddle.position.x + leftPaddle.getWidth();
        }

        gameObjects.forEach(o -> o.update(deltaTime, AREA));

        sendGameData();
    }

    public void sendGameData()
    {
        GameState gp = new GameState(ball.toPacket(), leftPaddle.toPacket(), rightPaddle.toPacket());
        sendToAllPlayers(Utils.createByteArray(gp));
    }

    private void onGameStart()
    {
        // Verstuur start signaal aan spelers
        // * Welke positie? Rechts/links
        // * Bal positie
        resetPositions();
        ball.velocity = new Vector2(200, 100);
    }
    @Override
    public void onPlayerInput(Player player, PlayerInputType playerInput)
    {
        System.out.println("Player input received from: "+player.getName() + "\nInput: "+playerInput);

        int paddle = players.indexOf(player);
        switch (playerInput)
        {
            case MOVE_UP:
            {
                if (paddle == 0)
                    leftPaddle.velocity = new Vector2(0, -500);
                else if (paddle == 1)
                    rightPaddle.velocity = new Vector2(0, -500);
            }
            break;
            case MOVE_DOWN:
            {
                if (paddle == 0)
                    leftPaddle.velocity = new Vector2(0, 500);
                else if (paddle == 1)
                    rightPaddle.velocity = new Vector2(0, 500);
            }
            break;
            case STOPPED_MOVING:
            {
                if (paddle == 0)
                    leftPaddle.velocity = Vector2.zero;
                else if (paddle == 1)
                    rightPaddle.velocity = Vector2.zero;
            }
            break;
            default:
                break;
        }
    }

    public void onPlayerConnect(Player player, JoinResponseType responseType)
    {
        System.out.println(player.getName() + " wants to connect.");
        if (players.size() < 2)
        {
            super.onPlayerConnect(player, JoinResponseType.OK);
            System.out.println(player.getName() + " connected.");
        }
        else if (players.size() > 1)
        {
            super.onPlayerConnect(player, JoinResponseType.FULL);
            System.out.println(player.getName() + " rejected\nReason: Server full.");
        }
    }

    public void onPlayerDisconnect(Player player, DisconnectReason reason)
    {
        super.onPlayerDisconnect(player, reason);
        System.out.println(player.getName()+ " disconnected from the server.\nReason: "+reason);
    }


    public void simulate()
    {

    }

    /*
    public void handleReceive()
    {
        InetSocketAddress receiverAddress = (InetSocketAddress)socket.receive(buffer);

        Object packetObject = Utils.createObject(buffer.array());

        if (packetObject instanceof JoinRequest)
        {
            JoinRequest jr = (JoinRequest)packetObject;

            JoinResponse response = null;
            if (players.size() < 2)
            {
                Player player = new Player(jr.getClientID(), receiverAddress);

                players.put(player, players.size()+1);

                response = new JoinResponse(JoinResponseType.OK);
            }
            else
            {
                response = new JoinResponse(JoinResponseType.FULL);
            }

            byte[] bytes = Utils.createByteArray(response);

            int sent = socket.send(bytes, receiverAddress);
            System.out.println("Sent: "+ bytes.length + " bytes of data.");
        }
        else if (packetObject instanceof PlayerInputType)
        {
            PlayerInputType playerInputType = (PlayerInputType)packetObject;

            switch (playerInputType)
            {

            }

        }
    }*/



}
