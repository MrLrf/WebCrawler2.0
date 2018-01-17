package music.main;

import music.service.ProxyCrawlerService;
import music.service.ProxyIPQueueService;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/17 14:49
 */
public class ProxyCrawler implements Runnable {
    @Override
    public void run() {
        if (ProxyIPQueueService.queueSize() < 10) {
            fillProxyIpQueue();
        }
    }

    public void fillProxyIpQueue() {
        ProxyCrawlerService.parseProxyIp();
    }
}
