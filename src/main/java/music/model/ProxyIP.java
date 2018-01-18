package music.model;

import org.bson.Document;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/17 15:48
 */
public class ProxyIP implements MongoDBModel {

    private String hostName;
    private int port;
    private String status; //值有两个：已用、未用

    public ProxyIP(String hostName, int port, String status) {
        this.hostName = hostName;
        this.port = port;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProxyIP{" +
                "hostName='" + hostName + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public Document model2Document() {
        Document document = new Document();
        document.append("host_name", hostName);
        document.append("port", port);
        document.append("status", status);

        return document;
    }
}
