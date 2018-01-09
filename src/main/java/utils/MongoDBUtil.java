package utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 * @author lirf
 * @date 2017/11/20 15:16
 */
@Component
public class MongoDBUtil {

    @Autowired
    private String ip;

    @Autowired
    private String port;

    public static MongoDBUtil mongoDBUtil;

    public void init() {
        mongoDBUtil = this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public static void test() {
        System.out.println(mongoDBUtil.ip + ":" + mongoDBUtil.port);
    }
}
