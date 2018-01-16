package mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mongodb.query.*;
import org.bson.Document;
import utils.ConfigUtil;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

/**
 * MongoDB数据库操作类
 * @author lirf
 * @date 2017/12/9 13:53
 */
public class MongodbUtil {

    private static String ip;
    private static int port;
    private static String username;
    private static String password;
    private static String databaseName;

    /**
     * 从配置文件中加载MongoDB配置
     */
    public static void loadConfig() {
        ip = (String) ConfigUtil.getMongodbConfig("ip");
        port = Integer.parseInt((String) ConfigUtil.getMongodbConfig("port"));
        databaseName = (String) ConfigUtil.getMongodbConfig("databaseName");
        username = (String) ConfigUtil.getMongodbConfig("username");
        password = (String) ConfigUtil.getMongodbConfig("password");
    }

    /**
     * 根据配置获取MongoDB连接
     * @return
     */
    public static MongoClient getMongoClient() {
        MongoClient mongoClient;
        loadConfig();
        if (username == null) {
            mongoClient = new MongoClient(ip, port);
        } else {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            //ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress(ip, port);
            List<ServerAddress> addresses = new ArrayList<>();
            addresses.add(serverAddress);

            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
            MongoCredential credential = MongoCredential.createScramSha1Credential(username, databaseName, password.toCharArray());
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(credential);

            //通过连接认证获取MongoDB连接
            mongoClient = new MongoClient(addresses, credentials);
        }
        return mongoClient;
    }

    /**
     * 插入到MongoDB数据库中数据
     * @param data 存储Document的List，要插入到数据库中的数据
     * @param collectionName 将数据插入的collection
     * @return
     */
    public static boolean insertIntoCollection(List<Document> data, String collectionName) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);

        boolean result;
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            collection.insertMany(data);
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            mongoClient.close();
        }
        return result;
    }

    /**
     * 根据条件删除collection中的第一个document
     * @param collectionName 删除数据的collection
     * @param searchQuery 查询条件
     * @return
     */
    public static boolean deleteOne(String collectionName, String searchQuery) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
        boolean result;
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            ConditionExpressionFactory factory = new DefaultConditionExpressionFactory();
            ConditionExpression expression = factory.createConditionExpression(searchQuery);
            collection.deleteOne(expression.toBson());
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            mongoClient.close();
        }
        return result;
    }

    public static boolean deleteAll(String collectionName, String searchQuery) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
        boolean result;
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            ConditionExpressionFactory factory = new DefaultConditionExpressionFactory();
            ConditionExpression expression = factory.createConditionExpression(searchQuery);
            collection.deleteMany(expression.toBson());
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            mongoClient.close();
        }
        return result;
    }

    public static Map<String, Object> getTopDocument(String collectionName) {
        return getDocumentByLimit(collectionName, 1).get(0);
    }

    public static List<Map<String, Object>> getDocumentByLimit(String collectionName, int count) {
        List<Map<String, Object>> result = new ArrayList<>();

        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);

        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

        Block<Document> toListBlock = document -> result.add(document2Map(document));
        collection.find().limit(count).forEach(toListBlock);
        mongoClient.close();
        return result;
    }

    public static List<Map<String, Object>> getDocumentByField(String collectionName, String fieldName, String value) {
        List<Map<String, Object>> result = new ArrayList<>();

        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);

        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

        Block<Document> toListBlock = document -> result.add(document2Map(document));
        collection.find(and(eq(fieldName, value), gt("age", 20))).forEach(toListBlock);
        mongoClient.close();
        return result;
    }

    public List<Map<String, Object>> getCollectionData(String collectionName) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);

        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        List<Map<String, Object>> result = mongoCollection2List(collection);
        mongoClient.close();
        return result;
    }

    public static List<Map<String, Object>> mongoCollection2List(MongoCollection<Document> mongoCollection) {
        List<Map<String, Object>> result = new LinkedList<>();
        for (Document doc : mongoCollection.find()) {
            Map<String, Object> map = document2Map(doc);
            result.add(map);
        }
        return result;
    }

    public static Map<String, Object> document2Map(Document doc) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : doc.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Document) {
                Document docValue = (Document) value;
                Map<String, Object> mapValue = document2Map(docValue);
                result.put(key, mapValue);
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

}
