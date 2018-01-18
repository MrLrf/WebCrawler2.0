package music.service;

import mongodb.MongodbUtil;
import music.model.ProxyIP;

import java.util.*;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/18 10:01
 */
public class MongoDbProxyIpService implements ProxyIpQueueService {

    private static final String COLLECTION_NAME = "proxyips";

    @Override
    public boolean addUsableIp(ProxyIP ip) {
        return MongodbUtil.insertIntoCollection(Arrays.asList(ip.model2Document()), COLLECTION_NAME);
    }

    @Override
    public ProxyIP getTopUsableIp() {
        String updateValue;

        List<Map<String, Object>> result;
        if (isUsableIpsEmpty()) {
            result = MongodbUtil.getDocumentByField(COLLECTION_NAME, "status", "已用");
            updateValue = "未用";
        } else {
            result = MongodbUtil.getDocumentByField(COLLECTION_NAME, "status", "未用");
            updateValue = "已用";
        }
        Map<String, Object> item = result.get(0);
        ProxyIP proxyIP = new ProxyIP(item.get("host_name").toString(),
                Integer.parseInt(item.get("port").toString()), item.get("status").toString());
        MongodbUtil.update(COLLECTION_NAME, "host_name", item.get("host_name").toString(), "status", updateValue);

        return proxyIP;
    }

    @Override
    public boolean isUsableIpsEmpty() {
        List<Map<String, Object>> result = MongodbUtil.getDocumentByField(COLLECTION_NAME, "status", "未用");
        return result.isEmpty();
    }

    @Override
    public int queueSize() {
        List<Map<String, Object>> result = MongodbUtil.getDocumentByField(COLLECTION_NAME, "status", "未用");
        return result.size();
    }
}
