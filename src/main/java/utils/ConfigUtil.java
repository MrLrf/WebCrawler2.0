package utils;

import java.io.*;
import java.util.Properties;

/**
 * 配置文件读取工具类
 * @author lirf
 * @date 2017/12/9 10:19
 */
public class ConfigUtil {

    public static Object getMongodbConfig(String key) {
        InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream("mongodb.properties");
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        Properties props = new Properties();
        try {
            props.load(br);
            return props.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
