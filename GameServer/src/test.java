import gameobjects.Vector2;
import networkpackets.GameState;
import networkpackets.client.InfoResponse;
import networkpackets.gameobjects.BallPacket;
import networkpackets.gameobjects.PaddlePacket;
import utils.Utils;

public class test
{
    public static void main(String[] args)
    {
       // new test();
        InfoResponse r = new InfoResponse();

        byte[] b = Utils.createByteArray(r);
        System.out.println(b.length);
    }

    public test()
    {
        GameState gp = new GameState(new BallPacket(22, 22, 22, 22),
                new PaddlePacket(22, 22, 22, 22),
                new PaddlePacket(22, 22, 22, 22));



        byte[] bytes = Utils.createByteArray(gp);
        System.out.println(bytes.length);
    }



}