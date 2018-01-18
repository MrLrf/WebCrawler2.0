package music.service;

import music.model.ProxyIP;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/18 9:59
 */
public interface ProxyIpQueueService {

    boolean addUsableIp(ProxyIP ip);

    ProxyIP getTopUsableIp();

    boolean isUsableIpsEmpty();

    int queueSize();
}
