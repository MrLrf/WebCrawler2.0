package music;

import mongodb.MongodbUtil;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/18 10:50
 */
public class MongoDbTest {

    @Test
    public void updateTest() {
        MongodbUtil.update("test", "name", "李瑞锋", "age", "18");
    }

    @Test
    public void getTest() {
        List<Map<String, Object>> result = MongodbUtil.getDocumentByField("test", "name", "李瑞锋");
        System.out.println(result);
    }

}
