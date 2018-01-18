package music.service;

import music.model.ProxyIP;
import utils.HttpClientUtil;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/17 14:50
 */
public class DefaultProxyIPQueueService implements ProxyIpQueueService {

    private static Queue<ProxyIP> usableIps =  new ConcurrentLinkedQueue<>();

    @Override
    public boolean addUsableIp(ProxyIP ip) {
        return usableIps.offer(ip);
    }

    @Override
    public ProxyIP getTopUsableIp() {
        ProxyIP ip = usableIps.peek();
        while (!HttpClientUtil.isProxyUsable(ip.getHostName(), ip.getPort())) {
            usableIps.remove(ip);
            ip = usableIps.peek();
        }
        ProxyIP ip1 = usableIps.poll();
        usableIps.offer(ip1);
        return ip1;
    }

    @Override
    public boolean isUsableIpsEmpty() {
        return usableIps.isEmpty();
    }

    @Override
    public int queueSize() {
        return usableIps.size();
    }
}
