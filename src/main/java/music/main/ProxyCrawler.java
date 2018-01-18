package music.main;

import music.service.*;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/17 14:49
 */
public class ProxyCrawler implements Runnable{

    private static ProxyIpQueueService proxyIpQueueService = new MongoDbProxyIpService();

    @Override
    public void run() {
        if (proxyIpQueueService.queueSize() < 10) {
            fillProxyIpQueue();
        }
    }

    public void fillProxyIpQueue() {
        ProxyCrawlerService.parseProxyIp();
    }
}
