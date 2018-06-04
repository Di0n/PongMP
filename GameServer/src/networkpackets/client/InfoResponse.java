package networkpackets.client;


import java.io.Serializable;

public class InfoResponse implements Serializable
{
    public enum OS
    {
        WINDOWS,
        LINUX,
        MAC_OS,
        UNKNOWN
    }
    private String bannerURL;
    private String serverInfo;
    private String[] players;
    private OS serverOS;

    public InfoResponse(){}

    public String getBannerURL()
    {
        return bannerURL;
    }

    public void setBannerURL(String bannerURL)
    {
        this.bannerURL = bannerURL;
    }

    public String getServerInfo()
    {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo)
    {
        this.serverInfo = serverInfo;
    }

    public String[] getPlayers()
    {
        return players;
    }

    public void setPlayers(String[] players)
    {
        this.players = players;
    }

    public OS getServerOS()
    {
        return serverOS;
    }

    public void setServerOS(OS serverOS)
    {
        this.serverOS = serverOS;
    }

}
