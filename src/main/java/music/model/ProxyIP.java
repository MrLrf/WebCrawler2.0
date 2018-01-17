package music.model;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/17 15:48
 */
public class ProxyIP {

    private String hostName;
    private int port;

    public ProxyIP(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ProxyIP{" +
                "hostName='" + hostName + '\'' +
                ", port=" + port +
                '}';
    }
}
