import framework.DebugDraw;
import framework.FrameRateCounter;
import framework.Game;
import framework.GameKeyListener;
import gameobjects.Ball;
import gameobjects.GameObject;
import gameobjects.Paddle;
import gameobjects.Vector2;
import networkpackets.GameScore;
import networkpackets.GameState;
import networkpackets.gameobjects.BallPacket;
import networkpackets.gameobjects.PaddlePacket;
import networkpackets.player.PlayerInput;
import networkpackets.player.PlayerInputType;
import networkpackets.server.GameEnded;
import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pong extends Game
{
    public static void main(String[] args)
    {
        //new Pong().start();
    }

    private static final int FPS = 144;
    private static final String GAME_NAME = "Pong";

    private final FrameRateCounter frameRateCounter;

    private final GameKeyListener keyListener;
    private final UDPClient socket;
    private final UUID clientID;
    private long lastHeartbeatTime;

    // ** GAME_OBJECTS *** \\
    private BufferedImage background;
    private BufferedImage ballImage;
    private BufferedImage paddleImage;
    private Ball ball;
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private Paddle playerPaddle;

    private ArrayList<GameObject> gameObjects;

    private final int paddleIndex;
    private int myScore;
    private int enemyScore;
    private boolean gameEnded;

    public Pong(UUID clientID, UDPClient socket, int paddleIndex)
    {
        super(GAME_NAME, FPS);
        super.antiAliasing = true;
        super.addKeyboardListener(keyListener = new GameKeyListener());

        this.lastHeartbeatTime = System.currentTimeMillis();
        this.frameRateCounter = new FrameRateCounter();
        this.gameObjects = new ArrayList<>();
        this.paddleIndex = paddleIndex;
        this.clientID = clientID;
        this.socket = socket;
    }

    private void resetPositions()
    {
        ball.position.x = (Game.PLAY_AREA.width / 2) - (ball.getWidth() / 2);                //((ball.getWidth()) / 2) + getWidth()/2;
        ball.position.y = (Game.PLAY_AREA.height / 2) - (ball.getHeight() / 2);
        ball.velocity = Vector2.zero;

        leftPaddle.position.x = 25;                     //((leftPaddle.getWidth()) / 2) + 15;
        leftPaddle.position.y = (Game.PLAY_AREA.height/2) - (leftPaddle.getHeight() / 2);
        leftPaddle.velocity = Vector2.zero;

        rightPaddle.position.x = Game.PLAY_AREA.width - 40;       //((ball.getWidth())/2) + getWidth() - ball.getWidth() - 20;
        rightPaddle.position.y = (Game.PLAY_AREA.height/2) - (rightPaddle.getHeight() / 2);
        rightPaddle.velocity = Vector2.zero;
    }
    @Override
    protected void loadContent()
    {
        try
        {
            background = ImageIO.read(getClass().getResource("/images/background.png"));
            ballImage = ImageIO.read(getClass().getResource("/images/ball.png"));
            paddleImage = ImageIO.read(getClass().getResource("/images/paddle.png"));
        }
        catch (IOException x) {x.printStackTrace();}

        ball = new Ball(ballImage, 1);

        leftPaddle = new Paddle(paddleImage, 1);
        rightPaddle = new Paddle(paddleImage, 1);
        playerPaddle = paddleIndex == 0 ? leftPaddle : rightPaddle;

        gameObjects.add(ball);
        gameObjects.add(leftPaddle);
        gameObjects.add(rightPaddle);


        resetPositions();
    }

    @Override
    protected void update(double deltaTime)
    {
        receiveData();
        handleUserInput(deltaTime);

        for (GameObject gameObject : gameObjects)
        {
            gameObject.update(deltaTime);
        }

        if (ball.hasCollided(leftPaddle))
        {

            System.out.println("Magnitude: "+ball.velocity.magnitude());

            ball.velocity.x -= 50;


            if (ball.velocity.y < 0)
                ball.velocity.y -= 50;
            else
                ball.velocity.y += 50;

            ball.velocity.x *= -1;

            ball.position.x = leftPaddle.position.x + leftPaddle.getWidth();
        }
        else if (ball.hasCollided(rightPaddle))
        {
            System.out.println("right hit");

            ball.velocity.x += 50;


            if (ball.velocity.y < 0)
                ball.velocity.y -= 50;
            else
                ball.velocity.y += 50;

            ball.velocity.x *= -1;

            ball.position.x = rightPaddle.position.x - rightPaddle.getWidth();
        }


        //leftPaddle.velocity = Vector2.zero;
        //rightPaddle.velocity = Vector2.zero;

        frameRateCounter.update(deltaTime);
    }

    private boolean lastUp, lastDown;
    private final Vector2 paddleVelocity = new Vector2(0, 500);
    private void handleUserInput(double deltaTime)
    {
        if (keyListener.isKeyDown(KeyEvent.VK_UP))
        {
            ///leftPaddle.position = leftPaddle.position.add(leftPaddle.velocity.negative().multiply(deltaTime));
            //leftPaddle.velocity = paddleVelocity.negative();
            if (!lastUp)
            {
                playerPaddle.velocity = paddleVelocity.negative();
                PlayerInput playerInput = new PlayerInput(PlayerInputType.MOVE_UP);
                socket.send(Utils.createByteArray(playerInput));
                lastUp = true;
            }
        }
        else if (lastUp)
        {
            playerPaddle.velocity = Vector2.zero;
            PlayerInput playerInput = new PlayerInput(PlayerInputType.STOPPED_MOVING);
            socket.send(Utils.createByteArray(playerInput));
            lastUp = false;
        }
        else if (keyListener.isKeyDown(KeyEvent.VK_DOWN))
        {
            //leftPaddle.velocity = paddleVelocity;
            ///leftPaddle.position = leftPaddle.position.add(leftPaddle.velocity.multiply(deltaTime));
            if (!lastDown)
            {
                playerPaddle.velocity = paddleVelocity;
                PlayerInput playerInput = new PlayerInput(PlayerInputType.MOVE_DOWN);
                socket.send(Utils.createByteArray(playerInput));
                lastDown = true;
            }
        }
        else if (lastDown)
        {
            playerPaddle.velocity = Vector2.zero;
            PlayerInput playerInput = new PlayerInput(PlayerInputType.STOPPED_MOVING);
            socket.send(Utils.createByteArray(playerInput));
            lastDown = false;
        }
    }

    private GameEnded.EndReason endReason;
    @Override
    protected void draw(Graphics2D g2d)
    {
        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        gameObjects.forEach(g -> g.draw(g2d));

        g2d.setColor(Color.WHITE);
        g2d.drawString(""+Math.round(frameRateCounter.getAverageFramesPerSecond()), 10, 10);


        int sideX = (paddleIndex == 0) ? getWidth() / 3 : getWidth() - (getWidth() / 4);
        int enemySideX = (paddleIndex == 0) ? getWidth() - (getWidth() / 4) : getWidth() / 3;
        int sideY = 10;

        g2d.drawString(Integer.toString(myScore), sideX, sideY);
        g2d.drawString(Integer.toString(enemyScore), enemySideX, sideY);

        if (gameEnded)
        {
            if (endReason == GameEnded.EndReason.SCORE)
            {
                if (myScore > enemyScore)
                {
                    Hud.drawTextThisFrame(g2d, "You won!", 50,
                            new Point2D.Double(getWidth() / 2, getHeight() / 2),
                            "Magneto", Font.BOLD);
                } else if (myScore == enemyScore)
                {
                    Hud.drawTextThisFrame(g2d, "It's a tie!", 50,
                            new Point2D.Double(getWidth() / 2, getHeight() / 2),
                            "Magneto", Font.BOLD);
                }
                else
                {
                    Hud.drawTextThisFrame(g2d, "You lost!", 50,
                            new Point2D.Double(getWidth() / 2, getHeight() / 2),
                            "Magneto", Font.BOLD);
                }
            }
            else
                Hud.drawTextThisFrame(g2d, "Enemy left the game!", 50,
                        new Point2D.Double(getWidth() / 2, getHeight() / 2),
                        "Magneto", Font.BOLD);
        }
        //g2d.setColor(Color.RED);
        //DebugDraw.draw(g2d, gameObjects);
    }

    private Map<UUID, Integer> scores = new HashMap<>();
    private ByteBuffer buffer = ByteBuffer.allocate(512);
    private void receiveData()
    {
        int received = socket.receive(buffer);

        if (received != 0)
        {
            Object packet = Utils.createObject(buffer.array());

            if (packet instanceof GameState)
            {
                GameState gamePacket = (GameState) packet;
                //System.out.println(gamePacket.ballPacket.velocityX);
                setGameState(gamePacket);
            }
            else if (packet instanceof GameScore)
            {
                GameScore gs = (GameScore)packet;
                scores = gs.getScores();

                for (Map.Entry<UUID, Integer> entry : scores.entrySet()) {
                    UUID key = entry.getKey();
                    int value = entry.getValue();

                    if (key.equals(clientID))
                        myScore = value;
                    else
                        enemyScore = value;
                    //System.out.println(key.equals(clientID) ? "My score: " : "Enemy score: "+value);
                }
            }
            else if (packet instanceof GameEnded)
            {
                GameEnded ge = (GameEnded)packet;
                if (ge.getReason() == GameEnded.EndReason.SCORE)
                {
                    if (ge.getWinner().equals(clientID))
                        myScore++;
                    else
                        enemyScore++;
                }
                if (!gameEnded)
                {
                    gameEnded = true;
                    endReason = ge.getReason();
                } // Anders is game al klaar
            }
        }
    }

    private void setGameState(final GameState gamePacket)
    {
        //System.out.println("Vel: "+ball.velocity.x + " : "+ball.velocity.y);
        BallPacket bp = gamePacket.getBallPacket();
        PaddlePacket lp = gamePacket.getLeftPaddlePacket();
        PaddlePacket rp = gamePacket.getRightPaddlePacket();


        ball.position = new Vector2(bp.getPositionX(), bp.getPositionY());
        ball.velocity = new Vector2(gamePacket.getBallPacket().getVelocityX(), gamePacket.getBallPacket().getVelocityY());

        leftPaddle.position = new Vector2(lp.getPositionX(), lp.getPositionY());
        leftPaddle.velocity = new Vector2(lp.getVelocityX(), lp.getVelocityY());

        rightPaddle.position = new Vector2(rp.getPositionX(), rp.getPositionY());
        rightPaddle.velocity = new Vector2(rp.getVelocityX(), rp.getVelocityY());
    }

    // Verzend disconnect signaal
    @Override
    protected void onClosing()
    {
        PlayerInput playerInput = new PlayerInput(PlayerInputType.DISCONNECT);
        socket.send(Utils.createByteArray(playerInput));
    }
}
