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

    public static void parseProxyIp() {
        String result;
        for (Map.Entry<String, String> item : Config.urlRegular.entrySet()) {
            result = HttpClientUtil.getHTML(item.getKey());
            Matcher m = Pattern.compile(item.getValue()).matcher(result);
            while (m.find()) {
                String hostName = m.group(1);
                int port = Integer.parseInt(m.group(2));
                if (HttpClientUtil.isProxyUsable(hostName, port)) {
                    ProxyIP ip = new ProxyIP(hostName, port);
                    System.out.println(ip);
                    ProxyIPQueueService.addUsableIp(ip);
                }
            }
        }
    }

    public static void main(String[] args) {
        parseProxyIp();
    }

}
