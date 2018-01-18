package music.service;

import music.model.ProxyIP;
import utils.Config;
import utils.HttpClientUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/16 15:55
 */
public class ProxyCrawlerService {

    private static ProxyIpQueueService proxyIpQueueService = new MongoDbProxyIpService();

    public static void parseProxyIp() {
        String result;
        for (Map.Entry<String, String> item : Config.urlRegular.entrySet()) {
            result = HttpClientUtil.getHTML(item.getKey());
            Matcher m = Pattern.compile(item.getValue()).matcher(result);
            while (m.find()) {
                String hostName = m.group(1);
                int port = Integer.parseInt(m.group(2));
                if (HttpClientUtil.isProxyUsable(hostName, port)) {
                    ProxyIP ip = new ProxyIP(hostName, port, "未用");
                    System.out.println(ip);
                    proxyIpQueueService.addUsableIp(ip);
                }
            }
        }
    }

    public static void main(String[] args) {
        parseProxyIp();
    }

}
