import gameobjects.Vector2;
import networkpackets.GameState;
import networkpackets.gameobjects.BallPacket;
import networkpackets.gameobjects.PaddlePacket;
import utils.Utils;

public class test
{
    public static void main(String[] args)
    {
       // new test();
    }

    public test()
    {
        GameState gp = new GameState(new BallPacket(22, 22, 22, 22),
                new PaddlePacket(22, 22, 22, 22),
                new PaddlePacket(22, 22, 22, 22));



        PacketTest pt = new PacketTest(new Vector2(20, 20), new Vector2(9, 9), new Vector2(9, 2),
                new Vector2(2, 2), new Vector2(22, 22), new Vector2(20, 20));
        byte[] bytes = Utils.createByteArray(gp);
        System.out.println(bytes.length);
    }



}